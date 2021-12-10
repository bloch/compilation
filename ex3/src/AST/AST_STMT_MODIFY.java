package AST;

public class AST_STMT_MODIFY extends AST_STMT {
    public AST_VAR v;
    public String id_name;
    public AST_EXP e;
    public AST_PSIK_EXP_LIST l;

    public AST_STMT_MODIFY(AST_VAR v,String id_name, AST_EXP e, AST_PSIK_EXP_LIST l, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (v == null && e == null) System.out.print("====================== stmt -> ID ( ); \n");
        if (v == null && e != null && l == null) System.out.print("====================== stmt -> ID ( exp );\n");
        if (v == null && e != null && l != null) System.out.print("====================== stmt -> ID ( exp psikExpList );\n");

        if (v != null && e == null) System.out.print("====================== stmt -> var . ID ( ); \n");
        if (v != null && e != null && l == null) System.out.print("====================== stmt -> var . ID ( exp );\n");
        if (v != null && e != null && l != null) System.out.print("====================== stmt -> var . ID ( exp psikExpList );\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.v = v;
        this.id_name = id_name;
        this.e = e;
        this.l = l;
    }

}
