package AST;

public class AST_TYPE_INT extends AST_TYPE {

    public AST_TYPE_INT(int lineNumber)
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
        System.out.print("AST NODE TYPE_INT\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPE_INT\n"));
    }
}