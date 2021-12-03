package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_CLASS_DEC extends AST_DEC {
    public AST_CLASS_DEC cd;

    public AST_CLASS_DEC() {

    }

    public AST_CLASS_DEC(AST_CLASS_DEC cd) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.cd = cd;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE CLASS_DEC\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (cd != null) cd.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("classDec"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cd.SerialNumber);
    }

    public TYPE SemantMe() {
        return this.cd.SemantMe();
    }
    public TYPE GetSignature() {
        return this.cd.GetSignature();
    }

    public boolean
}
