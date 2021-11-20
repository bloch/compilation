package AST;

public class AST_VAR_DEC extends AST_DEC {
    public AST_VAR_DEC vd;

    public AST_VAR_DEC() {

    }

    public AST_VAR_DEC(AST_VAR_DEC vd) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.vd = vd;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */

    /*********************************************************/
    public void PrintMe() {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE VAR_DEC\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (vd != null) vd.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("varDec"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, vd.SerialNumber);

    }
}


//    public String id_name;
//    public AST_TYPE type;
//    public AST_EXP e;
//    public AST_NEW_EXP ne;
//
//    public AST_VAR_DEC(String id_name, AST_TYPE type, AST_EXP e, AST_NEW_EXP ne) {
//        /******************************/
//        /* SET A UNIQUE SERIAL NUMBER */
//        /******************************/
//        SerialNumber = AST_Node_Serial_Number.getFresh();
//
//        /***************************************/
//        /* PRINT CORRESPONDING DERIVATION RULE */
//        /***************************************/
//        if (e == null && ne == null) System.out.format("====================== varDec -> type ID (%s) ;\n", id_name);
//        if (e != null && ne == null) System.out.format("====================== varDec -> type ID (%s) ASSIGN exp ;\n", id_name);
//        if (e == null && ne != null) System.out.format("====================== varDec -> type ID (%s) ASSIGN newExp ;\n", id_name);
//
//        /*******************************/
//        /* COPY INPUT DATA NENBERS ... */
//        /*******************************/
//        this.id_name = id_name;
//        this.type = type;
//        this.e = e;
//        this.ne = ne;
//    }
