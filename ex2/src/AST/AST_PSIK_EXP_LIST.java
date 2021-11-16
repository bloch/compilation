package AST;

public class AST_PSIK_EXP_LIST extends AST_Node{
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_EXP e;
    public AST_PSIK_EXP_LIST tail;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_PSIK_EXP_LIST(AST_EXP e, AST_PSIK_EXP_LIST tail)
    {
        // This class hold data from of kind: COMMA TYPE ID(name)
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (tail != null) System.out.print("====================== psikExpList -> COMMA exp psikExpList\n");
        if (tail == null) System.out.print("====================== psikExpList -> COMMA exp      \n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.e = e;
        this.tail = tail;
    }
}
