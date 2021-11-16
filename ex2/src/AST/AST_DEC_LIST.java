package AST;

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
    public AST_DEC_LIST(AST_DEC head,AST_DEC_LIST tail)
    {
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
}
