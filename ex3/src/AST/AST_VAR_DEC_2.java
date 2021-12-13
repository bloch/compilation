package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_VAR_DEC_2 extends AST_VAR_DEC {
    public AST_TYPE_WITH_ID type_with_id1;
    public AST_EXP exp;

    public AST_VAR_DEC_2(String id_name1, AST_TYPE type1, AST_EXP exp, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.format("====================== varDec -> type ID (%s) ASSIGN exp;\n", id_name1);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type_with_id1 = new AST_TYPE_WITH_ID(type1, id_name1, this.lineNumber);
        this.exp = exp;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE VAR_DEC_2\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (type_with_id1 != null) type_with_id1.PrintMe();
        if (exp != null) exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("varDec_2\n"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type_with_id1.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);

    }

    public TYPE SemantMe() {
        // We want to check if id_name1 is a good variable name, if not - error
        // if yes - enter to symbol table

        TYPE type = GetSignature(type_with_id1.t);

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        TYPE type_of_var = SYMBOL_TABLE.getInstance().find(type.name);

        if (type_of_var == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_VAR_DEC_2: [%d:%d] non existing type %s\n",2,2,type.name);
            System.exit(0);
            return null;
        }

        if (type_of_var instanceof TYPE_VOID) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_VAR_DEC_2: ILLEGAL TO DEFINE VAR WITH VOID TYPE");
            System.exit(0);
        }

        /**************************************/
        /* [2] Check That Name does NOT exist */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().findInLastScope(type_with_id1.id_name) != null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_VAR_DEC_2:  [%d:%d] variable %s already exists in scope\n",2,2,type_with_id1.id_name);
            System.exit(0);
            return null;
        }

        TYPE exp_type = exp.SemantMe();
        if (exp_type == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR AST_VAR_DEC_2: illegal exp");
            System.exit(0);
            return null;
        }

        if (!isT1SubInstanceT2(exp_type, type_of_var)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_VAR_DEC_2: illegal assignment");
            System.exit(0);
            return null;
        }

        /***************************************************/
        /* [3] Enter the Function Type to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(type_with_id1.id_name,type_of_var);

        /*********************************************************/
        /* [4] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;

    }

    public TYPE GetSignature() {
        return GetSignature(type_with_id1.t);
    }

}
