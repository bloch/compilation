package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_VAR_DEC extends AST_DEC {
    public AST_VAR_DEC vd;

    public AST_VAR_DEC() {

    }

    public AST_VAR_DEC(AST_VAR_DEC vd) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.vd = vd;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */

    /*********************************************************/
    public void PrintMe() {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE VAR_DEC\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (vd != null) vd.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("varDec"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, vd.SerialNumber);

    }

    public TYPE GetSignature(AST_TYPE t) {
        TYPE return_type = null;                                // null to be removed in future
        if (t instanceof AST_TYPE_INT) {
            return_type = TYPE_INT.getInstance();
        }
        if (t instanceof AST_TYPE_STRING) {
            return_type = TYPE_STRING.getInstance();
        }
        if (t instanceof AST_TYPE_VOID) {
            return_type = TYPE_VOID.getInstance();
        }
        // TODO: add conversion of AST_TYPE_ID also..

        return return_type;
    }

}
