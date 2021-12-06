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
        TYPE head_type_id = null;
        if (this.head instanceof AST_CFIELD_FUNC_DEC) {
            AST_CFIELD_FUNC_DEC cf_func_dec = (AST_CFIELD_FUNC_DEC) this.head;
            if(cf_func_dec.fd instanceof AST_FUNC_DEC_1) {
                AST_FUNC_DEC_1 fd_1 = (AST_FUNC_DEC_1) cf_func_dec.fd;
                head_type_id = new TYPE_ID(fd_1.GetSignature(), fd_1.type_with_id1.id_name);
            }
            else if(cf_func_dec.fd instanceof AST_FUNC_DEC_2) {
                AST_FUNC_DEC_2 fd_2 = (AST_FUNC_DEC_2) cf_func_dec.fd;
                head_type_id = new TYPE_ID(fd_2.GetSignature(), fd_2.type_with_id1.id_name);
            }
            else if(cf_func_dec.fd instanceof AST_FUNC_DEC_3) {
                AST_FUNC_DEC_3 fd_3 = (AST_FUNC_DEC_3) cf_func_dec.fd;
                head_type_id = new TYPE_ID(fd_3.GetSignature(), fd_3.type_with_id1.id_name);
            }
            else {
                System.out.format("unreachable code 1 in GetSignatures() of CFL_LIST");
                System.exit(0);
            }
        }
        else if (this.head instanceof AST_CFIELD_VAR_DEC) {
            AST_CFIELD_VAR_DEC cf_var_dec = (AST_CFIELD_VAR_DEC) this.head;
            if(cf_var_dec.vd instanceof AST_VAR_DEC_1) {
                AST_VAR_DEC_1 vd_1 = (AST_VAR_DEC_1) cf_var_dec.vd;
                head_type_id = new TYPE_ID(vd_1.GetSignature(), vd_1.type_with_id1.id_name);
            }
            else if(cf_var_dec.vd instanceof AST_VAR_DEC_2) {
                AST_VAR_DEC_2 vd_2 = (AST_VAR_DEC_2) cf_var_dec.vd;
                head_type_id = new TYPE_ID(vd_2.GetSignature(), vd_2.type_with_id1.id_name);
            }
            else if(cf_var_dec.vd instanceof AST_VAR_DEC_3) {
                AST_VAR_DEC_3 vd_3 = (AST_VAR_DEC_3) cf_var_dec.vd;
                head_type_id = new TYPE_ID(vd_3.GetSignature(), vd_3.type_with_id1.id_name);
            }
            else {
                System.out.format("unreachable code 1 in GetSignatures() of CFL_LIST");
                System.exit(0);
            }
        }
        else {
            System.out.format("unreachable code 3 in GetSignatures() of CFL_LIST");
            System.exit(0);
        }

        if (this.tail == null) {
            return new TYPE_LIST(head_type_id, null);
        }
        else {
            return new TYPE_LIST(head_type_id, this.tail.GetSignatures());
        }
    }
//    public boolean isSignaturesValid(TYPE_LIST class_signatures){
//        for (TYPE_LIST tmp_class_signatures = class_signatures ; tmp_class_signatures != null; tmp_class_signatures = tmp_class_signatures.tail) {
//            TYPE typeField = tmp_class_signatures.head;
//            if (typeField == null){  //type doesn't exist in symbol table
//                return false;
//            }
//        }
//        return true;
//    }

}
