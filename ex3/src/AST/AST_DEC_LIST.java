package AST;

import java.util.ArrayList;

public class AST_DEC_LIST extends AST_Node
{
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_DEC head;
    public AST_DEC_LIST tail;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_DEC_LIST(AST_DEC head,AST_DEC_LIST tail , int lineNumber)
    {

        this.lineNUmber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (tail != null) System.out.print("====================== decList -> dec decList\n");
        if (tail == null) System.out.print("====================== decList -> dec      \n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.head = head;
        this.tail = tail;
    }

    /******************************************************/
    /* The printing message for a statement list AST node */
    /******************************************************/
    public void PrintMe(int SerialNumber)
    {
        /**************************************/
        /* AST NODE TYPE = AST STATEMENT LIST */
        /**************************************/

        ArrayList<AST_DEC> lst = new ArrayList<AST_DEC>();
        lst.add(this.head);
        AST_DEC_LIST tmp = this.tail;
        while(tmp != null) {
            lst.add(tmp.head);
            tmp = tmp.tail;
        }

        for (int counter = 0; counter < lst.size(); counter++) {
            lst.get(counter).PrintMe();
        }

        for (int counter = 0; counter < lst.size(); counter++) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,lst.get(counter).SerialNumber);
        }

    }

}
