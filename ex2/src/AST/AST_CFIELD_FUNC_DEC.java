package AST;

public class AST_CFIELD_FUNC_DEC extends AST_CFIELD {
    public AST_FUNC_DEC fd;

    public AST_CFIELD_FUNC_DEC(AST_FUNC_DEC fd) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== cField -> funcDec\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.fd = fd;
    }
}
