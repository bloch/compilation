package AST;

import java.util.ArrayList;

public class AST_PSIK_TYPE_ID_LIST extends AST_Node{
    /****************/
    /* DATA MEMBERS */
    /****************/
//    public AST_TYPE type;
//    public String id_name;

    public AST_TYPE_WITH_ID type_with_id;
    public AST_PSIK_TYPE_ID_LIST tail;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_PSIK_TYPE_ID_LIST(AST_TYPE type, String id_name, AST_PSIK_TYPE_ID_LIST tail, int lineNumber)
    {
        this.lineNumber = lineNumber;
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
//        this.type = type;
//        this.id_name = id_name;
        this.type_with_id = new AST_TYPE_WITH_ID(type, id_name);
        this.tail = tail;
    }

    /******************************************************/
    /* The printing message for a statement list AST node */
    /******************************************************/
    public void PrintMe(int SerialNumber)
    {
        ArrayList<AST_TYPE_WITH_ID> lst = new ArrayList<AST_TYPE_WITH_ID>();

        lst.add(this.type_with_id);

        AST_PSIK_TYPE_ID_LIST tmp = this.tail;
        while(tmp != null) {
            lst.add(tmp.type_with_id);
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
