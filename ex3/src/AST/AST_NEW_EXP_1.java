package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_NEW_EXP_1 extends AST_NEW_EXP {
    public AST_TYPE t;

    public AST_NEW_EXP_1(AST_TYPE t) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== newExp -> NEW type\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.t = t;
    }
    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE NEW_EXP_1\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (t != null) t.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("newExp_1"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,t.SerialNumber);

    }

    public TYPE SemantMe() {
        return GetSignature(this.t);
    }

}
