package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;
public class AST_EXP_NIL extends AST_EXP{

    public AST_EXP_NIL(int lineNumber)
    {
        this.lineNumber = lineNumber;
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE EXP_NIL\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("EXP_NIL\n"));
    }

    public TYPE SemantMe() {
        return TYPE_NIL.getInstance();
    }

    public TEMP IRme()
    {
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommandConstInt(t,0));
        return t;
    }

}
