package AST;

public class AST_VAR_DEC_1 extends AST_VAR_DEC {
    public AST_TYPE_WITH_ID type_with_id1;

    public AST_VAR_DEC_1(String id_name1, AST_TYPE type1) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.format("====================== varDec -> type ID (%s) ;\n", id_name1);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type_with_id1 = new AST_TYPE_WITH_ID(type1, id_name1);
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE VAR_DEC_1\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (type_with_id1 != null) type_with_id1.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("varDec_1"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type_with_id1.SerialNumber);

    }

}
