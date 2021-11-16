package AST;

public class AST_CFIELD_VAR_DEC extends AST_CFIELD{
    public AST_VAR_DEC vd;

    public AST_CFIELD_VAR_DEC(AST_VAR_DEC vd) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== cField -> varDec\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.vd = vd;
    }
}
