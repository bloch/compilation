package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_STMT_MODIFY_3 extends AST_STMT{
    public String id_name1;
    public AST_EXP exp;
    public AST_PSIK_EXP_LIST l;

    public AST_STMT_MODIFY_3(String id_name1, AST_EXP exp, AST_PSIK_EXP_LIST l, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== stmt -> ID ( exp psikExpList );\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name1 = id_name1;
        this.exp = exp;
        this.l = l;
    }

    /***********************************************/
    /* The default message for an exp var AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = EXP VAR AST NODE */
        /************************************/
        System.out.print("AST NODE STMT_MODIFY_3\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (exp != null) exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STMT_MODIFY_3\nID(%s)\n(exp psikExpList);\n", id_name1));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);

        this.l.PrintMe(SerialNumber);

    }

    public TYPE SemantMe() {
        TYPE t = SYMBOL_TABLE.getInstance().findNotInGlobalScope(this.id_name1);
        if(t == null) {
            System.out.format(">> STMT_MODIFY_3: %s not in local scopes, then looking in father..\n", this.id_name1);
            t = isFuncInClassFields(this.id_name1);
            if (t == null) {
                System.out.format(">> STMT_MODIFY_3: %s not in local scopes & fathers, then looking in global..\n", this.id_name1);
                t = SYMBOL_TABLE.getInstance().find(this.id_name1);
                if (t == null) {
                    AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                    AST_Node.file_writer.close();
                    System.out.format(">> ERROR STMT_MODIFY_3: illegal ID name(not in global and not in fathers and locals)\n");
                    System.exit(0);
                }
            }
        }
        if (!(t instanceof TYPE_FUNCTION)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_3: not a function");
            System.exit(0);
            return null;
        }
        TYPE_FUNCTION t_func = (TYPE_FUNCTION) t;
        if (t_func.params == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_3: shouldn't have parameters");
            System.exit(0);
            return null;
        }
        if (t_func.params.tail == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_3: function called with 2+ parameters but should have 1 parameters");
            System.exit(0);
            return null;
        }
        //first parameter type checking
        TYPE t_head = t_func.params.head;
        TYPE exp_type = exp.SemantMe();
        if (exp_type == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_3: first parameter for function doesn't exist");
            System.exit(0);
            return null;
        }
        if (!isT1SubInstanceT2(exp_type, t_head)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_3: first parameter doesn't match");
            System.exit(0);
            return null;
        }

        TYPE_LIST l_type_list = l.GetSignatures();
        TYPE_LIST tmp_l = l_type_list;
        TYPE_LIST tmp_p = t_func.params.tail;
        while(tmp_l != null && tmp_p != null) {
            if (tmp_l.head == null) {
                AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                AST_Node.file_writer.close();
                System.out.println(">> ERROR STMT_MODIFY_3: some parameter(second or higher) for function doesn't exist");
                System.exit(0);
                return null;
            }
            if (!isT1SubInstanceT2(tmp_l.head, tmp_p.head)) {
                AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                AST_Node.file_writer.close();
                System.out.println(">> ERROR STMT_MODIFY_3: some parameters don't match");
                System.exit(0);
                return null;
            }
            tmp_l = tmp_l.tail;
            tmp_p = tmp_p.tail;
        }
        if(tmp_l != null && tmp_p == null)
        {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_3: too many parameters given for function");
            System.exit(0);
            return null;
        }
        else if (tmp_l == null && tmp_p != null)
        {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_3: not enough parameters given for function");
            System.exit(0);
            return null;
        }
        // tmp_l and tmp_p now should be null both
        return null;
    }

    public TEMP IRme()
    {
        TEMP param1 = exp.IRme();
        TEMP_LIST params_list = new TEMP_LIST(param1, null);
        AST_PSIK_EXP_LIST tmp = l;
        while(tmp != null) {
            TEMP next_param = tmp.head.IRme();
            params_list.AddToTEMPList(next_param);
            tmp = tmp.tail;
        }
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Call(null, id_name1, params_list));
        return null;
    }

}
