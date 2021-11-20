package AST;

public class AST_VAR_DEC_2 extends AST_VAR_DEC {
    public AST_TYPE type1;
    public String id_name1;
    public AST_EXP exp;

    public AST_VAR_DEC_2(String id_name1, AST_TYPE type1, AST_EXP exp) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.format("====================== varDec -> type ID (%s) ASSIGN exp;\n", id_name1);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type1 = type1;
        this.id_name1 = id_name1;
        this.exp = exp;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE VAR_DEC_2\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (type1 != null) type1.PrintMe();
        if (exp != null) exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("varDec_2\ntype ID (%s) ASSIGN exp;\n", id_name1));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type1.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);

    }

}
