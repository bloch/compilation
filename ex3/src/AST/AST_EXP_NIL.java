package AST;

public class AST_EXP_NIL extends AST_EXP{

    public AST_EXP_NIL()
    {
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
    public String SemantMe()
    {
        return "nil";// "nil" will be an important keyward for input for new instances , functions , arrays and etc.
    }
}
