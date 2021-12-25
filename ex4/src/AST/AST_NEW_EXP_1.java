package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_NEW_EXP_1 extends AST_NEW_EXP {
    public AST_TYPE t;

    public AST_NEW_EXP_1(AST_TYPE t, int lineNumber) {
        this.lineNumber = lineNumber;
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

    public TEMP IRme(){
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        (AST_TYPE_ID) tc = (AST_TYPE_ID) t; //must be type class , otherwise it will fail at semantic level

        String name = tc.name;
        IR.getInstance().AddIRcommand(new IRcommand_New_Class(dst,name));
        return dst;
    }

}
