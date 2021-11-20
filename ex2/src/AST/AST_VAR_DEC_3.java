package AST;

public class AST_VAR_DEC_3 extends AST_VAR_DEC {
    public AST_TYPE type1;
    public String id_name1;
    public AST_NEW_EXP ne;

    public AST_VAR_DEC_3(String id_name1, AST_TYPE type1, AST_NEW_EXP ne) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.format("====================== varDec -> type ID (%s) ASSIGN newExp ;\n", id_name1);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type1 = type1;
        this.id_name1 = id_name1;
        this.ne = ne;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE VAR_DEC_3\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (type1 != null) type1.PrintMe();
        if (ne != null) ne.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("varDec_3\ntype ID (%s) ASSIGN newExp;\n", id_name1));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type1.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,ne.SerialNumber);

    }

}
