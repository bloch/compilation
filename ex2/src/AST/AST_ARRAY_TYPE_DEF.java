package AST;

public class AST_ARRAY_TYPE_DEF extends AST_DEC {
    public AST_ARRAY_TYPE_DEF atd;

    public AST_ARRAY_TYPE_DEF() {

    }

    public AST_ARRAY_TYPE_DEF(AST_ARRAY_TYPE_DEF atd) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.atd = atd;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE ARRAY_TYPE_DEF\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (atd != null) atd.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("arrayTypeDef"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,atd.SerialNumber);

    }

}
