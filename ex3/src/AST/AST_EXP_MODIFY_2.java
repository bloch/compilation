package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_EXP_MODIFY_2 extends AST_EXP {
    public String id_name;
    public AST_EXP e;

    public AST_EXP_MODIFY_2(String id_name, AST_EXP e) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== exp -> ID ( exp );\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name = id_name;
        this.e = e;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE EXP_MODIFY_2\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (e != null) e.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("EXP_MODIFY_2\nID(%s);\n", id_name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);

    }

    public TYPE SemantMe() {
        TYPE t = SYMBOL_TABLE.getInstance().find(this.id_name);
        if (!(t instanceof TYPE_FUNCTION)) {
            System.out.println(">> ERROR STMT_MODIFY_2: not a function");
            System.exit(0);
            return null;
        }
        TYPE_FUNCTION t_func = (TYPE_FUNCTION) t;
        if (t_func.params == null) {
            System.out.println(">> ERROR STMT_MODIFY_2: shouldn't have parameters");
            System.exit(0);
            return null;
        }
        if (t_func.params.tail != null) {
            System.out.println(">> ERROR STMT_MODIFY_2: should have atleast 2+ parameters");
            System.exit(0);
            return null;
        }
        TYPE t_head = t_func.params.head;
        TYPE exp_type = e.SemantMe();
        if (exp_type != t_head) {
            System.out.println(">> ERROR STMT_MODIFY_2: (only) parameter doesn't match");
            System.exit(0);
            return null;
        }
        return t_func.returnType;
    }

}
