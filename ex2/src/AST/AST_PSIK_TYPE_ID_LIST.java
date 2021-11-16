package AST;

public class AST_PSIK_TYPE_ID_LIST extends AST_Node{
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_TYPE type;
    public String id_name;
    public AST_PSIK_TYPE_ID_LIST tail;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_PSIK_TYPE_ID_LIST(AST_TYPE type, String id_name, AST_PSIK_TYPE_ID_LIST tail)
    {
        // This class hold data from of kind: COMMA TYPE ID(name)
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (tail != null) System.out.print("====================== psikTypeIdList -> COMMA type ID psikTypeIdList\n");
        if (tail == null) System.out.print("====================== psikTypeIdList -> COMMA type ID      \n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type = type;
        this.id_name = id_name;
        this.tail = tail;
    }
}
