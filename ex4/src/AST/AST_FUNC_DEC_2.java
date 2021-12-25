package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_FUNC_DEC_2 extends AST_FUNC_DEC{
    public AST_TYPE_WITH_ID type_with_id1;
    public AST_TYPE_WITH_ID type_with_id2;
    public AST_STMT_LIST stmtList;

    public AST_FUNC_DEC_2(AST_TYPE type1, String id_name1, AST_TYPE type2, String id_name2, AST_STMT_LIST stmtList, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== funcDec -> type  ID (type ID) { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type_with_id1 = new AST_TYPE_WITH_ID(type1, id_name1, this.lineNumber);
        this.type_with_id2 = new AST_TYPE_WITH_ID(type2, id_name2, this.lineNumber);
        this.stmtList = stmtList;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE FUNC_DEC_2\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (type_with_id1 != null) type_with_id1.PrintMe();
        if (type_with_id2 != null) type_with_id2.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("funcDec_2\n"));


        if (stmtList != null) stmtList.PrintMe(SerialNumber);

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type_with_id1.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type_with_id2.SerialNumber);
    }

    public TYPE SemantMe() {
        SYMBOL_TABLE symbol_table = SYMBOL_TABLE.getInstance();

        /**************************************/
        /* [0] Check That Function Name does NOT exist */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().findInLastScope(this.type_with_id1.id_name) != null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_2: function name %s already exists in scope\n",type_with_id1.id_name);
            System.exit(0);
        }

        TYPE_FUNCTION function_signature = (TYPE_FUNCTION) this.GetSignature();

        symbol_table.enter(this.type_with_id1.id_name, function_signature);

        symbol_table.beginScope();

        // here we know that scope only opned, thus type_with_id2.id_name doesn't exist and type is valid(checked in GetSignature() line 73)
        symbol_table.enter(type_with_id2.id_name,   function_signature.params.head);


        AST_Node.retTypesList = new TYPE_LIST(null, null);
        AST_Node.retStmtList = new AST_STMT_LIST(null, null, -1);

        this.stmtList.SemantMe();

        if(!CheckReturnTypes(function_signature.returnType)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_2 : Return Type exception");
            System.exit(0);
        }

        AST_Node.retTypesList = null;
        AST_Node.retStmtList = null;

        symbol_table.endScope();

        return null;
    }

    public TYPE GetSignature() {
        TYPE return_type = GetSignature(type_with_id1.t);
        if(return_type == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_2 : Return Type doesn't exist");
            System.exit(0);
        }

        TYPE arg1_type = GetSignature(type_with_id2.t);
        if(arg1_type == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_2 : first parameter type doesn't exist");
            System.exit(0);
        }
        if (arg1_type instanceof TYPE_VOID) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_2: first parameter type can't be void");
            System.exit(0);
        }

        return new TYPE_FUNCTION(return_type, this.type_with_id1.id_name, new TYPE_LIST(arg1_type, null));
    }

    public TEMP IRme()
    {
        IR.getInstance().Add_IRcommand(new IRcommand_Label(type_with_id1.name));
        if (body != null) body.IRme();
        return null;
    }

}
