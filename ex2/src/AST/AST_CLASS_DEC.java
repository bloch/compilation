package AST;

public class AST_CLASS_DEC extends AST_Node {
    public String id_name1;
    public String id_name2;
    public AST_CFIELD_LIST cfl;

    public AST_CLASS_DEC(String id_name1, String id_name2, AST_CFIELD_LIST cfl) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (id_name2 == null) System.out.print("====================== classDec -> CLASS ID { cFieldList }\n");
        if (id_name2 != null) System.out.print("====================== classDec -> CLASS ID EXTENDS ID { cFieldList }\n");


        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name1 = id_name1;
        this.id_name2 = id_name2;
        this.cfl = cfl;
    }
}
