package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_STMT_ASSIGN_NEW extends AST_STMT
{
    public AST_VAR var;
    public AST_NEW_EXP ne;

    /*******************/
    /*  CONSTRUCTOR(S) */
    /*******************/
    public AST_STMT_ASSIGN_NEW(AST_VAR var,AST_NEW_EXP ne)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> var ASSIGN newExp SEMICOLON\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
        this.ne = ne;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE NEW ASSIGN STMT\n");

        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (var != null) var.PrintMe();
        if (ne != null) ne.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "NEW ASSIGN\nleft := right\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,ne.SerialNumber);
    }

    public TYPE SemantMe() {
        TYPE t1 = null;
        TYPE t2 = null;

        if (var != null) t1 = var.SemantMe();
        if (t1 == null) {
            System.out.format(">> ERROR [%d:%d] illegal access of var\n",6,6);
            System.exit(0);
        }

        if (ne != null) t2 = ne.SemantMe();

        if (t1 != t2) {
            System.out.format(">> ERROR [%d:%d] type mismatch for var := newExp\n",6,6);
            System.exit(0);
        }
        return null;
    }

}
