package AST;

public class AST_DEC extends AST_Node {
    public AST_VAR_DEC vd;
    public AST_FUNC_DEC fd;
    public AST_CLASS_DEC cd;
    public AST_ARRAY_TYPE_DEF atd;

    public AST_DEC(AST_VAR_DEC vd, AST_FUNC_DEC fd, AST_CLASS_DEC cd, AST_ARRAY_TYPE_DEF atd)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (vd  != null) System.out.print("====================== dec -> varDec\n");
        if (fd  != null) System.out.print("====================== dec -> funcDec\n");
        if (cd  != null) System.out.print("====================== dec -> classDec\n");
        if (atd != null) System.out.print("====================== dec -> arrayTypedef \n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.vd = vd;
        this.fd = fd;
        this.cd = cd;
        this.atd = atd;
    }
}
