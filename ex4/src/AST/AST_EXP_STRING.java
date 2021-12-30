package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_EXP_STRING extends AST_EXP{
    public String value;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_STRING(String value , int lineNumber)
    {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== exp -> STRING( %s )\n", value);

        /*******************************/
        /* COPY INPUT DATA STRING ... */
        /*******************************/
        this.value = value.substring(1, value.length() - 1);
    }

    /************************************************/
    /* The printing message for an STRING EXP AST node */
    /************************************************/
    public void PrintMe ()
    {
        /*******************************/
        /* AST NODE TYPE = AST STRING EXP */
        /*******************************/
        System.out.format("AST NODE STRING( %s )\n", value);

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STRING(\'%s\')", value));
    }

    public TYPE SemantMe() {
        return TYPE_STRING.getInstance();
    }

    public TEMP IRme()
    {
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        // store the const string in data section and load the label's address to t
        IR.getInstance().Add_IRcommand(new IRcommandContString(t,value));
        return t;
    }
}
