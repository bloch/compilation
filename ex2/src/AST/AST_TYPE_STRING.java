package AST;

public class AST_TYPE_STRING extends AST_TYPE {

    public AST_TYPE_STRING()
    {
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        System.out.print("AST NODE TYPE_STRING\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPE_STRING\n"));
    }
}
