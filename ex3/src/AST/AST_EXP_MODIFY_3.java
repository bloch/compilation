package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_EXP_MODIFY_3 extends AST_EXP {
    public String id_name;
    public AST_EXP e;
    public AST_PSIK_EXP_LIST l;

    public AST_EXP_MODIFY_3(String id_name, AST_EXP e, AST_PSIK_EXP_LIST l, int lineNumber) {

        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== exp -> ID ( exp psikExpList );\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name = id_name;
        this.e = e;
        this.l = l;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE EXP_MODIFY_3\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (e != null) e.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("EXP_MODIFY_3\nID(%s);\n", id_name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);

        this.l.PrintMe(SerialNumber);

    }


    public TYPE SemantMe() {
        TYPE t = SYMBOL_TABLE.getInstance().find(this.id_name);
        if (!(t instanceof TYPE_FUNCTION)) {
            System.out.println(">> ERROR EXP_MODIFY_3: not a function");
            System.exit(0);
            return null;
        }
        TYPE_FUNCTION t_func = (TYPE_FUNCTION) t;
        if (t_func.params == null) {
            System.out.println(">> ERROR EXP_MODIFY_3: shouldn't have parameters");
            System.exit(0);
            return null;
        }
        if (t_func.params.tail == null) {
            System.out.println(">> ERROR EXP_MODIFY_3: function called with 2+ parameters but should have 1 parameters");
            System.exit(0);
            return null;
        }
        //first parameter type checking
        TYPE t_head = t_func.params.head;
        TYPE exp_type = e.SemantMe();
        if (exp_type != t_head) {
            System.out.println(">> ERROR EXP_MODIFY_3: first parameter doesn't match");
            System.exit(0);
            return null;
        }

        TYPE_LIST l_type_list = l.GetSignatures();
        TYPE_LIST tmp_l = l_type_list;
        TYPE_LIST tmp_p = t_func.params.tail;
        while(tmp_l != null && tmp_p != null) {
            if (tmp_l.head != tmp_p.head)
            {
                System.out.println(">> ERROR EXP_MODIFY_3: some parameters don't match");
                System.exit(0);
                return null;
            }
            tmp_l = tmp_l.tail;
            tmp_p = tmp_p.tail;
        }
        if(tmp_l != null && tmp_p == null)
        {
            System.out.println(">> ERROR EXP_MODIFY_3: too many parameters given for function");
            System.exit(0);
            return null;
        }
        else if (tmp_l == null && tmp_p != null)
        {
            System.out.println(">> ERROR EXP_MODIFY_3: not enough parameters given for function");
            System.exit(0);
            return null;
        }
        // tmp_l and tmp_p now should be null both
        return t_func.returnType;
    }
}
