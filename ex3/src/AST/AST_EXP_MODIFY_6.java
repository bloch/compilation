package AST;

public class AST_EXP_MODIFY_6 extends AST_EXP {
    public AST_VAR var;
    public String id_name;
    public AST_EXP e;
    public AST_PSIK_EXP_LIST l;

    public AST_EXP_MODIFY_6(AST_VAR var, String id_name, AST_EXP e, AST_PSIK_EXP_LIST l) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== exp -> var . ID ( exp psikExpList );\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
        this.id_name = id_name;
        this.e = e;
        this.l = l;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE EXP_MODIFY_6\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (var != null) var.PrintMe();
        if (e != null) e.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("EXP_MODIFY_6\nID(%s);\n", id_name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);

        this.l.PrintMe(SerialNumber);

    }

}
