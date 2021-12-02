package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_VAR_DEC extends AST_CFIELD{
    public AST_VAR_DEC vd;

    public AST_CFIELD_VAR_DEC(AST_VAR_DEC vd) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== cField -> varDec\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.vd = vd;
    }
    /***********************************************/
    /* The default message for an exp var AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = EXP VAR AST NODE */
        /************************************/
        System.out.print("AST NODE CFIELD_VAR_DEC\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (vd != null) vd.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "CFIELD\nVAR_DEC");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,vd.SerialNumber);
    }
    public TYPE SemantMe() { return this.vd.SemantMe(); }
    public TYPE GetSignature() {
        return this.vd.GetSignature();
    }
}
