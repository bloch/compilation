package AST;

import java.util.ArrayList;

public class AST_PROGRAM extends AST_Node{
    public AST_DEC_LIST l;

    /*******************/
    /*  CONSTRUCTOR(S) */
    /*******************/
    public AST_PROGRAM(AST_DEC_LIST l)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== Program -> decList\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.l = l;
    }

//    /******************************************************/
//    /* The printing message for a statement list AST node */
//    /******************************************************/
//    public void PrintMe()
//    {
//        /**************************************/
//        /* AST NODE TYPE = AST STATEMENT LIST */
//        /**************************************/
//        System.out.print("AST NODE Program\n");
//
//        /*************************************/
//        /* RECURSIVELY PRINT HEAD + TAIL ... */
//        /*************************************/
//        if (this.l != null) this.l.PrintMe();
//
//        /**********************************/
//        /* PRINT to AST GRAPHVIZ DOT file */
//        /**********************************/
//        AST_GRAPHVIZ.getInstance().logNode(
//                SerialNumber,
//                "Program\n");
//
//        /****************************************/
//        /* PRINT Edges to AST GRAPHVIZ DOT file */
//        /****************************************/
//        if (this.l != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,this.l.SerialNumber);
//    }
    public void PrintMe()
    {
        System.out.print("AST NODE Program\n");
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "Program\n");
        this.l.PrintMe(SerialNumber);
    }

}
