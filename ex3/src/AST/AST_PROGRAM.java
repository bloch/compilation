package AST;

import java.util.ArrayList;
import TYPES.*;
import SYMBOL_TABLE.*;

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

    public void PrintMe()
    {
        System.out.print("AST NODE Program\n");
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "Program\n");
        this.l.PrintMe(SerialNumber);
    }

    public TYPE SemantMe()
    {
        this.l.head.SemantMe();

        AST_DEC_LIST tmp = this.l.tail;
        while(tmp != null) {
            tmp.head.SemantMe();
            tmp = tmp.tail;
        }

        return null;
    }
}
