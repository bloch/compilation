package AST;

public class AST_FUNC_DEC_3 extends AST_FUNC_DEC {
    public AST_TYPE type1;
    public String id_name1;
//    public AST_TYPE type2;
//    public String id_name2;
    public AST_TYPE_WITH_ID type_with_id;
    public AST_PSIK_TYPE_ID_LIST ptil;
    public AST_STMT_LIST stmtList;

    public AST_FUNC_DEC_3(AST_TYPE type1, String id_name1, AST_TYPE type2, String id_name2, AST_PSIK_TYPE_ID_LIST ptil, AST_STMT_LIST stmtList) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== funcDec -> type  ID (type ID psikTypeIdList) { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type1 = type1;
        this.id_name1 = id_name1;
//        this.type2 = type2;
//        this.id_name2 = id_name2;
        this.type_with_id = new AST_TYPE_WITH_ID(type2, id_name2);
        this.ptil = ptil;
        this.stmtList = stmtList;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE FUNC_DEC_3\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (type1 != null) type1.PrintMe();
        if (type_with_id != null) type_with_id.PrintMe();


        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("funcDec_3\ntype1 ID1(%s) (psikTypeIdList) { stmtList } \n", id_name1));

        if (stmtList != null) stmtList.PrintMe(SerialNumber);
        if (ptil != null) ptil.PrintMe(SerialNumber);

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type1.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type_with_id.SerialNumber);
        //AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,ptil.SerialNumber);
        //AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,stmtList.SerialNumber);
    }
}
