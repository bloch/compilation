package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_NEW_EXP_2 extends AST_NEW_EXP {
    public AST_TYPE t;
    public AST_EXP e;

    public AST_NEW_EXP_2(AST_TYPE t, AST_EXP e) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== newExp -> NEW type [ exp ]\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.t = t;
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
        System.out.print("AST NODE NEW_EXP_2\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (t != null) t.PrintMe();
        if (e != null) e.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("newExp_2"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,t.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);

    }

    public TYPE SemantMe() {
        /****************************/
        /* [0] Semant the expression */
        /****************************/
        if (this.e.SemantMe() != TYPE_INT.getInstance()) {
            System.out.format(">> ERROR [%d:%d] expression inside BRACKETS is not integral\n",2,2);
        }

        return GetSignature(this.t);
    }

}
