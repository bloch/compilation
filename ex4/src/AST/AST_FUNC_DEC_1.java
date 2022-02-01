package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_FUNC_DEC_1 extends AST_FUNC_DEC {
    public AST_TYPE_WITH_ID type_with_id1;
    public AST_STMT_LIST stmtList;

    public AST_FUNC_DEC_1(AST_TYPE type1, String id_name1, AST_STMT_LIST stmtList, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== funcDec -> type  ID () { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/

        this.type_with_id1 = new AST_TYPE_WITH_ID(type1, id_name1, this.lineNumber);
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
        System.out.print("AST NODE FUNC_DEC_1\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (type_with_id1 != null) type_with_id1.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("funcDec_1\n"));

        if (stmtList != null) stmtList.PrintMe(SerialNumber);


        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type_with_id1.SerialNumber);

    }

    public TYPE SemantMe() {

        SYMBOL_TABLE symbol_table = SYMBOL_TABLE.getInstance();

        /**************************************/
        /* [0] Check That Function Name does NOT exist */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().findInLastScope(this.type_with_id1.id_name) != null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_1: function name %s already exists in scope\n",type_with_id1.id_name);
            System.exit(0);
        }

        TYPE_FUNCTION function_signature = (TYPE_FUNCTION) this.GetSignature();

        symbol_table.enter(this.type_with_id1.id_name, function_signature);

        symbol_table.beginScope();

        AST_Node.local_offset = -44;
        AST_Node.param_offset = 8;

        AST_Node.retTypesList = new TYPE_LIST(null, null);
        AST_Node.retStmtList = new AST_STMT_LIST(null, null, -1);

        this.stmtList.SemantMe();

        if(!CheckReturnTypes(function_signature.returnType)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_1 : Return Type exception");
            System.exit(0);
        }

        AST_Node.retTypesList = null;
        AST_Node.retStmtList = null;
        symbol_table.endScope();
        this.calc_var_decs(this.stmtList);
        return null;
    }
    public TYPE GetSignature() {
        TYPE return_type = GetSignature(type_with_id1.t);
        if(return_type == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_1 : Return Type doesn't exist");
            System.exit(0);
        }
        return new TYPE_FUNCTION(return_type, this.type_with_id1.id_name, null);
    }

    public TEMP IRme()
    {
        String function_label;
        if(AST_Node.cur_class == null) {
            if (type_with_id1.id_name.equals("main")) {
                function_label = "user_main";
            } else {
                function_label = type_with_id1.id_name + "_function";
            }
        }
        else {
            if (type_with_id1.id_name.equals("main")) {
                function_label = AST_Node.cur_class.name + "_" + "user_main";
            } else {
                function_label = AST_Node.cur_class.name + "_" + type_with_id1.id_name;
            }
        }

        IR.getInstance().Add_IRcommand(new IRcommand_Label(function_label));
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Prologue(function_label, Math.max(16, 4*this.num_locals)));

        AST_Node.cur_function_label = function_label;
        if (stmtList != null) stmtList.IRme();
        AST_Node.cur_function_label = null;

        IR.getInstance().Add_IRcommand(new IRcommand_Function_Epilogue(function_label));

        return null;
    }

}
