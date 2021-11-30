package AST;

public abstract class AST_DEC extends AST_Node {

}



//public class AST_DEC extends AST_Node {
//    public AST_VAR_DEC vd;
//    public AST_FUNC_DEC fd;
//    public AST_CLASS_DEC cd;
//    public AST_ARRAY_TYPE_DEF atd;
//
//    public AST_DEC(AST_VAR_DEC vd, AST_FUNC_DEC fd, AST_CLASS_DEC cd, AST_ARRAY_TYPE_DEF atd)
//    {
//        /******************************/
//        /* SET A UNIQUE SERIAL NUMBER */
//        /******************************/
//        SerialNumber = AST_Node_Serial_Number.getFresh();
//
//        /***************************************/
//        /* PRINT CORRESPONDING DERIVATION RULE */
//        /***************************************/
//        if (vd  != null) System.out.print("====================== dec -> varDec\n");
//        if (fd  != null) System.out.print("====================== dec -> funcDec\n");
//        if (cd  != null) System.out.print("====================== dec -> classDec\n");
//        if (atd != null) System.out.print("====================== dec -> arrayTypedef \n");
//
//        /*******************************/
//        /* COPY INPUT DATA NENBERS ... */
//        /*******************************/
//        this.vd = vd;
//        this.fd = fd;
//        this.cd = cd;
//        this.atd = atd;
//    }
//
//    /*********************************************************/
//    /* The printing message for an assign statement AST node */
//    /*********************************************************/
//    public void PrintMe()
//    {
//        /********************************************/
//        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
//        /********************************************/
//        System.out.print("AST NODE DEC\n");
//
//        /***********************************/
//        /* RECURSIVELY PRINT varDec + funcDec + classDec + arrayTypedef ... */
//        /***********************************/
//        if (this.vd != null) this.vd.PrintMe();
//        if (this.fd != null) this.fd.PrintMe();
//        if (this.cd != null) this.cd.PrintMe();
//        if (this.atd != null) this.atd.PrintMe();
//
//        /***************************************/
//        /* PRINT Node to AST GRAPHVIZ DOT file */
//        /***************************************/
//        if (this.vd != null) AST_GRAPHVIZ.getInstance().logNode(
//                SerialNumber,
//                "dec := varDec\n");
//
//        if (this.fd != null) AST_GRAPHVIZ.getInstance().logNode(
//                SerialNumber,
//                "dec := funcDec\n");
//
//        if (this.cd != null) AST_GRAPHVIZ.getInstance().logNode(
//                SerialNumber,
//                "dec := classDec\n");
//
//        if (this.atd != null) AST_GRAPHVIZ.getInstance().logNode(
//                SerialNumber,
//                "dec := arrayTypedef\n");
//
//        /****************************************/
//        /* PRINT Edges to AST GRAPHVIZ DOT file */
//        /****************************************/
//        if (this.vd != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,this.vd.SerialNumber);
//        if (this.fd != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,this.fd.SerialNumber);
//        if (this.cd != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,this.cd.SerialNumber);
//        if (this.atd != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,this.atd.SerialNumber);
//    }
//
//}
