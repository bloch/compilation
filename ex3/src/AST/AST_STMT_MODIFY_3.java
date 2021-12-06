package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_STMT_MODIFY_3 extends AST_STMT{
    public String id_name1;
    public AST_EXP exp;
    public AST_PSIK_EXP_LIST l;

    public AST_STMT_MODIFY_3(String id_name1, AST_EXP exp, AST_PSIK_EXP_LIST l) {
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
        TYPE t = SYMBOL_TABLE.getInstance().find(this.id_name1);
        if (!(t instanceof TYPE_FUNCTION)) {
            System.out.println(">> ERROR STMT_MODIFY_3: not a function");
            System.exit(0);
            return null;
        }
        TYPE_FUNCTION t_func = (TYPE_FUNCTION) t;
        if (t_func.params == null) {
            System.out.println(">> ERROR STMT_MODIFY_3: should have parameters");
            System.exit(0);
            return null;
        }
        if (t_func.params.tail == null) {
            System.out.println(">> ERROR STMT_MODIFY_3: function called with 2+ parameters but should have 1 parameters");
            System.exit(0);
            return null;
        }
        //first parameter type checking
        TYPE t_head = t_func.params.head;
        TYPE exp_type = exp.SemantMe();
        if (exp_type != t_head) {
            System.out.println(">> ERROR STMT_MODIFY_3: first parameter doesn't match");
            System.exit(0);
            return null;
        }

        TYPE_LIST l_type_list = l.GetSignatures();
        TYPE_LIST tmp_l = l_type_list;
        TYPE_LIST tmp_p = t_func.params.tail;
        while(tmp_l != null && tmp_p != null) {
            if (tmp_l.head != tmp_p.head)
            {
                System.out.println(">> ERROR STMT_MODIFY_3: some parameters don't match");
                System.exit(0);
                return null;
            }
            tmp_l = tmp_l.tail;
            tmp_p = tmp_p.tail;
        }
        if(tmp_l != null && tmp_p == null)
        {
            System.out.println(">> ERROR STMT_MODIFY_3: too many parameters given for function");
            System.exit(0);
            return null;
        }
        else if (tmp_l == null && tmp_p != null)
        {
            System.out.println(">> ERROR STMT_MODIFY_3: not enough parameters given for function");
            System.exit(0);
            return null;
        }
        // tmp_l and tmp_p now should be null both
        return null;
    }

}
