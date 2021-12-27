package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_VAR_DEC_1 extends AST_VAR_DEC {
    public AST_TYPE_WITH_ID type_with_id1;

    public AST_VAR_DEC_1(String id_name1, AST_TYPE type1, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.format("====================== varDec -> type ID (%s) ;\n", id_name1);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type_with_id1 = new AST_TYPE_WITH_ID(type1, id_name1, this.lineNumber);
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE VAR_DEC_1\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (type_with_id1 != null) type_with_id1.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("varDec_1"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type_with_id1.SerialNumber);
    }

    public TYPE SemantMe() {
        // We want to check if id_name1 is a good variable name, if not - error
        // if yes - enter to symbol table

        TYPE type = GetSignature(type_with_id1.t);
        if (type == null){
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_VAR_DEC_1: non existing type\n");
            System.exit(0);
        }

        if (type instanceof TYPE_VOID) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_VAR_DEC_1: ILLEGAL TO DEFINE VAR WITH VOID TYPE");
            System.exit(0);
        }

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        TYPE type_of_var = SYMBOL_TABLE.getInstance().find(type.name);
        if (type_of_var == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type.name);
            System.exit(0);
        }

        /**************************************/
        /* [2] Check That Name does NOT exist */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().findInLastScope(type_with_id1.id_name) != null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,type_with_id1.id_name);
            System.exit(0);
        }

        /***************************************************/
        /* [3] Enter the Function Type to the Symbol Table */
        /***************************************************/
        //SYMBOL_TABLE.getInstance().enter(type_with_id1.id_name, type_of_var);
        if(this.in_function) {
            SYMBOL_TABLE.getInstance().enter(type_with_id1.id_name, type_of_var, AST_Node.local_offset);
            AST_Node.local_offset -= 4;
        }
        else {
            SYMBOL_TABLE.getInstance().enter(type_with_id1.id_name, type_of_var);
        }
        /*********************************************************/
        /* [4] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;

    }

    public TYPE GetSignature() {
        return GetSignature(type_with_id1.t);
    }


    public TEMP IRme(){
        String name = type_with_id1.id_name;
        TYPE t = SYMBOL_TABLE.getInstance().find(name);
        if(t != null) {
          // in global
          IR.getInstance().Add_IRcommand(new IRcommand_Allocate(name));
        }

        return null;
    }

}
