package AST;

public class AST_STMT_ASSIGN_NEW extends AST_STMT
{
    public AST_VAR var;
    public AST_NEW_EXP ne;

    /*******************/
    /*  CONSTRUCTOR(S) */
    /*******************/
    public AST_STMT_ASSIGN_NEW(AST_VAR var,AST_NEW_EXP ne)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> var ASSIGN newExp SEMICOLON\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
        this.ne = ne;
    }
}
