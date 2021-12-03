package AST;

import java.util.ArrayList;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_CFIELD_LIST extends AST_Node {
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_CFIELD head;
    public AST_CFIELD_LIST tail;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_CFIELD_LIST(AST_CFIELD head, AST_CFIELD_LIST tail)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (tail != null) System.out.print("====================== cFieldList -> cField cFieldList\n");
        if (tail == null) System.out.print("====================== cFieldList -> cField      \n");

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
        /**************************************/
        /* AST NODE TYPE = AST STATEMENT LIST */
        /**************************************/

        ArrayList<AST_CFIELD> lst = new ArrayList<AST_CFIELD>();
        lst.add(this.head);
        AST_CFIELD_LIST tmp = this.tail;
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

    public TYPE SemantMe() {
        this.head.SemantMe();

        AST_CFIELD_LIST tmp = this.tail;
        while(tmp != null) {
            tmp.head.SemantMe();
            tmp = tmp.tail;
        }
        return null;
    }
//    public TYPE SemantMe()
//    {
//        if (head != null) head.SemantMe();
//        if (tail != null) tail.SemantMe();
//
//        return null;
//    }

    public TYPE_LIST GetSignatures() {
        if (this.tail == null) {
            return new TYPE_LIST(this.head.GetSignature(), null);
        }
        else {
            return new TYPE_LIST(this.head.GetSignature(), this.tail.GetSignatures());
        }
    }

}
