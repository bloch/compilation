package AST;

import java.util.ArrayList;

public class AST_PSIK_EXP_LIST extends AST_Node{
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_EXP head;
    public AST_PSIK_EXP_LIST tail;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_PSIK_EXP_LIST(AST_EXP head, AST_PSIK_EXP_LIST tail)
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
        this.head = head;
        this.tail = tail;
    }

    /******************************************************/
    /* The printing message for a statement list AST node */
    /******************************************************/
    public void PrintMe(int SerialNumber)
    {
        ArrayList<AST_EXP> lst = new ArrayList<AST_EXP>();
        lst.add(this.head);
        AST_PSIK_EXP_LIST tmp = this.tail;
        while(tmp != null) {
            lst.add(tmp.head);
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
