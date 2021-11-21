package AST;

public class AST_CLASS_DEC extends AST_DEC {
    public AST_CLASS_DEC cd;

    public AST_CLASS_DEC() {

    }

    public AST_CLASS_DEC(AST_CLASS_DEC cd) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.cd = cd;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE CLASS_DEC\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (cd != null) cd.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("classDec"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cd.SerialNumber);
    }


//    public String id_name1;
//    public String id_name2;
//    public AST_CFIELD_LIST cfl;
//
//    public AST_CLASS_DEC(String id_name1, String id_name2, AST_CFIELD_LIST cfl) {
//        /******************************/
//        /* SET A UNIQUE SERIAL NUMBER */
//        /******************************/
//        SerialNumber = AST_Node_Serial_Number.getFresh();
//
//        /***************************************/
//        /* PRINT CORRESPONDING DERIVATION RULE */
//        /***************************************/
//        if (id_name2 == null) System.out.print("====================== classDec -> CLASS ID { cFieldList }\n");
//        if (id_name2 != null) System.out.print("====================== classDec -> CLASS ID EXTENDS ID { cFieldList }\n");
//
//
//        /*******************************/
//        /* COPY INPUT DATA NENBERS ... */
//        /*******************************/
//        this.id_name1 = id_name1;
//        this.id_name2 = id_name2;
//        this.cfl = cfl;
//    }
}
