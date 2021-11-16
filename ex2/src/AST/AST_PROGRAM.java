package AST;

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
}
