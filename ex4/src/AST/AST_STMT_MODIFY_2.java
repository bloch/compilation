package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_STMT_MODIFY_2 extends AST_STMT {
    public String id_name1;
    public AST_EXP exp;

    public AST_STMT_MODIFY_2(String id_name1, AST_EXP exp, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== stmt -> ID ( exp );\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name1 = id_name1;
        this.exp = exp;
    }

    /***********************************************/
    /* The default message for an exp var AST node */

    /***********************************************/
    public void PrintMe() {
        /************************************/
        /* AST NODE TYPE = EXP VAR AST NODE */
        /************************************/
        System.out.print("AST NODE STMT_MODIFY_2\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (exp != null) exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STMT_MODIFY_2\nID(%s)\n(exp);\n", id_name1));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);

    }

    public TYPE SemantMe() {
        TYPE t = SYMBOL_TABLE.getInstance().findNotInGlobalScope(this.id_name1);
        if(t == null) {
            t = isFuncInClassFields(this.id_name1);
            if (t == null) {
                t = SYMBOL_TABLE.getInstance().find(this.id_name1);
                if (t == null) {
                    AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                    AST_Node.file_writer.close();
                    System.out.format(">> ERROR STMT_MODIFY_2: illegal ID name(not in global and not in fathers and locals)\n");
                    System.exit(0);
                }
            }
        }

        if (!(t instanceof TYPE_FUNCTION)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_2: not a function");
            System.exit(0);
            return null;
        }
        TYPE_FUNCTION t_func = (TYPE_FUNCTION) t;
        if (t_func.params == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_2: shouldn't have parameters");
            System.exit(0);
            return null;
        }
        if (t_func.params.tail != null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_2: should have atleast 2+ parameters");
            System.exit(0);
            return null;
        }
        TYPE t_head = t_func.params.head;
        TYPE exp_type = exp.SemantMe();
        if (exp_type == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_2: single parameter for function doesn't exist");
            System.exit(0);
            return null;
        }
        if (!isT1SubInstanceT2(exp_type, t_head)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_2: signgle parameter doesn't match");
            System.exit(0);
            return null;
        }
        return null;
    }

    public TEMP IRme()
    {
        TEMP param1 = exp.IRme();
        TEMP_LIST params_list = new TEMP_LIST(param1, null);
        if(id_name1.equals("PrintInt")) {
            IR.getInstance().Add_IRcommand(new IRcommand_PrintInt(param1));
        }
        else if(id_name1.equals("PrintString")) {
            IR.getInstance().Add_IRcommand(new IRcommand_PrintString(param1));
        }
        else {
            IR.getInstance().Add_IRcommand(new IRcommand_Function_Call(null, id_name1, params_list));
        }
        return null;
    }

}
