package AST;

public class AST_FUNC_DEC extends AST_Node {
    public AST_TYPE type1;
    public String id_name1;
    public AST_TYPE type2;
    public String id_name2;
    public AST_PSIK_TYPE_ID_LIST ptil;
    public AST_STMT_LIST stmtList;

    public AST_FUNC_DEC(AST_TYPE type1, String id_name1, AST_TYPE type2, String id_name2,AST_PSIK_TYPE_ID_LIST ptil, AST_STMT_LIST stmtList) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (type2 == null) System.out.print("====================== funcDec -> type  ID () { stmtList }\n");
        if (type2 != null && ptil == null) System.out.print("====================== funcDec -> type  ID (type ID) { stmtList }\n");
        if (type2 != null && ptil != null) System.out.print("====================== funcDec -> type  ID (type ID psikTypeIdList) { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type1 = type1;
        this.id_name1 = id_name1;
        this.type2 = type2;
        this.id_name2 = id_name2;
        this.ptil = ptil;
        this.stmtList = stmtList;
    }
}
