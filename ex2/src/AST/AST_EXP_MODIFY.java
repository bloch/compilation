package AST;

public class AST_EXP_MODIFY extends AST_EXP {
    public AST_VAR v;
    public String id_name;
    public AST_EXP e;
    public AST_PSIK_EXP_LIST l;

    public AST_EXP_MODIFY(AST_VAR v,String id_name, AST_EXP e, AST_PSIK_EXP_LIST l) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (v == null && e == null) System.out.print("====================== exp -> ID ( ); \n");
        if (v == null && e != null && l == null) System.out.print("====================== exp -> ID ( exp );\n");
        if (v == null && e != null && l != null) System.out.print("====================== exp -> ID ( exp psikExpList );\n");

        if (v != null && e == null) System.out.print("====================== exp -> var . ID ( ); \n");
        if (v != null && e != null && l == null) System.out.print("====================== exp -> var . ID ( exp );\n");
        if (v != null && e != null && l != null) System.out.print("====================== exp -> var . ID ( exp psikExpList );\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.v = v;
        this.id_name = id_name;
        this.e = e;
        this.l = l;
    }
}
