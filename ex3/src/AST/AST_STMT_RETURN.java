package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_STMT_RETURN extends AST_STMT{

    public AST_STMT_RETURN()
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== stmt -> RETURN;\n");

    }
    /**************************************************/
    /* The printing message for a STMT RETURN AST node */
    /**************************************************/
    public void PrintMe()
    {
        /**********************************/
        /* AST NODE TYPE = AST STMT RETURN */
        /**********************************/
        System.out.format("AST NODE STMT RETURN \n");

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STMT\nRETURN\n"));
    }

    public TYPE SemantMe() {
        return null;
    }
}
