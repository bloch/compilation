package AST;

public class AST_NEW_EXP extends AST_Node {
    public String id_name;
    public AST_EXP e;

    public AST_NEW_EXP(String id_name, AST_EXP e) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (e == null) System.out.print("====================== newExp -> NEW ID\n");
        if (e != null) System.out.print("====================== newExp -> NEW ID [ exp ]\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name = id_name;
        this.e = e;
    }
}
