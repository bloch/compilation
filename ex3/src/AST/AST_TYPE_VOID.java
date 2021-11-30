package AST;

public class AST_TYPE_VOID extends AST_TYPE {

    public AST_TYPE_VOID() {
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        System.out.print("AST NODE TYPE_VOID\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPE_VOID\n"));
    }
}
