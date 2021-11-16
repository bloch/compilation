package AST;

public class AST_VAR_DEC extends AST_Node {

    public String id_name;
    public AST_TYPE type;
    public AST_EXP e;
    public AST_NEW_EXP ne;

    public AST_VAR_DEC(String id_name, AST_TYPE type, AST_EXP e, AST_NEW_EXP ne) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (e == null && ne == null) System.out.format("====================== varDec -> type ID (%s) ;\n", id_name);
        if (e != null && ne == null) System.out.format("====================== varDec -> type ID (%s) ASSIGN exp ;\n", id_name);
        if (e == null && ne != null) System.out.format("====================== varDec -> type ID (%s) ASSIGN newExp ;\n", id_name);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name = id_name;
        this.type = type;
        this.e = e;
        this.ne = ne;
    }

}
