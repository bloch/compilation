package AST;

public class AST_STMT_VAR_DEC  extends AST_STMT{
    public AST_VAR_DEC vd;

    public AST_STMT_VAR_DEC(AST_VAR_DEC vd)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== stmt -> varDec\n");

        /*******************************/
        /* COPY INPUT DATA EXP ... */
        /*******************************/
        this.vd = vd;
    }
}
