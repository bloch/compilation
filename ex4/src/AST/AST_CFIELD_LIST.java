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
    public AST_CFIELD_LIST(AST_CFIELD head, AST_CFIELD_LIST tail , int lineNumber) {

        this.lineNumber = lineNumber;
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
    public void PrintMe(int SerialNumber) {
        /**************************************/
        /* AST NODE TYPE = AST STATEMENT LIST */
        /**************************************/

        ArrayList<AST_CFIELD> lst = new ArrayList<AST_CFIELD>();
        lst.add(this.head);
        AST_CFIELD_LIST tmp = this.tail;
        while (tmp != null) {
            lst.add(tmp.head);
            tmp = tmp.tail;
        }

        for (int counter = 0; counter < lst.size(); counter++) {
            lst.get(counter).PrintMe();
        }

        for (int counter = 0; counter < lst.size(); counter++) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, lst.get(counter).SerialNumber);
        }
    }

    public void CheckSTMTForConstantInit(AST_CFIELD cfield) {
        if (cfield instanceof AST_CFIELD_VAR_DEC) {
            AST_CFIELD_VAR_DEC vardec = (AST_CFIELD_VAR_DEC) cfield;
            // if vardec.vd instance of AST_VAR_DEC_1 -> OK
            if (vardec.vd instanceof AST_VAR_DEC_2) {
                AST_VAR_DEC_2 vd2 = (AST_VAR_DEC_2) vardec.vd;
                if (!((vd2.exp instanceof AST_EXP_INT) || (vd2.exp instanceof AST_EXP_STRING) || (vd2.exp instanceof AST_EXP_NIL))) {
                    AST_Node.file_writer.print(String.format("ERROR(%d)", vd2.lineNumber));
                    AST_Node.file_writer.close();
                    System.out.format("ERROR IN CLASS DEC: tried to init var with not constant");
                    System.exit(0);
                }
            }
            if (vardec.vd instanceof AST_VAR_DEC_3) {
                AST_VAR_DEC_3 vd3 = (AST_VAR_DEC_3) vardec.vd;
                AST_Node.file_writer.print(String.format("ERROR(%d)", vd3.lineNumber));
                AST_Node.file_writer.close();
                System.out.format("ERROR IN CLASS DEC: tried to init var with not constant(NEW exp)");
                System.exit(0);
            }
        }
    }

    public TYPE SemantMe(TYPE_CLASS type_class) {
        if(type_class.father != null) {
            type_class.num_fields = type_class.father.num_fields;
            type_class.num_methods = type_class.father.num_methods;
        }
        else {
            type_class.num_fields = 0;
            type_class.num_methods = -1;
        }
        this.CheckSTMTForConstantInit(this.head);          // exits on error
        TYPE_ID head_signature = GetClassSignature(this.head);
        CheckIfOverrideLegal(type_class.father, head_signature, this.head, type_class);
        this.head.SemantMe();
        type_class.data_members = new TYPE_LIST(head_signature, null);

        AST_CFIELD_LIST tmp = this.tail;
        while (tmp != null) {
            this.CheckSTMTForConstantInit(tmp.head);       // exits on error
            TYPE_ID tmp_head_signature = GetClassSignature(tmp.head);
            CheckIfOverrideLegal(type_class.father, tmp_head_signature, tmp.head, type_class);
            tmp.head.SemantMe();

            type_class.data_members.AddToTypeList(tmp_head_signature);
            tmp = tmp.tail;
        }
        System.out.println("CLASS: " + type_class.name);
        for(TYPE_LIST tmp_data_members = type_class.data_members; tmp_data_members != null; tmp_data_members = tmp_data_members.tail) {
            System.out.println("data member " + tmp_data_members.head.name + ", offset " + tmp_data_members.head.class_offset);
        }
        return null;
    }

    public static TYPE_ID GetClassSignature(AST_CFIELD cfield) {
        if (cfield instanceof AST_CFIELD_FUNC_DEC) {
            AST_CFIELD_FUNC_DEC cf_func_dec = (AST_CFIELD_FUNC_DEC) cfield;
            if (cf_func_dec.fd instanceof AST_FUNC_DEC_1) {
                AST_FUNC_DEC_1 fd_1 = (AST_FUNC_DEC_1) cf_func_dec.fd;
                return new TYPE_ID(fd_1.GetSignature(), fd_1.type_with_id1.id_name);
            } else if (cf_func_dec.fd instanceof AST_FUNC_DEC_2) {
                AST_FUNC_DEC_2 fd_2 = (AST_FUNC_DEC_2) cf_func_dec.fd;
                return new TYPE_ID(fd_2.GetSignature(), fd_2.type_with_id1.id_name);
            } else if (cf_func_dec.fd instanceof AST_FUNC_DEC_3) {
                AST_FUNC_DEC_3 fd_3 = (AST_FUNC_DEC_3) cf_func_dec.fd;
                return new TYPE_ID(fd_3.GetSignature(), fd_3.type_with_id1.id_name);
            } else {
                System.out.format("unreachable code 1 in GetSignatures() of CFL_LIST");
                System.exit(0);
                return null;
            }
        } else if (cfield instanceof AST_CFIELD_VAR_DEC) {
            AST_CFIELD_VAR_DEC cf_var_dec = (AST_CFIELD_VAR_DEC) cfield;
            if (cf_var_dec.vd instanceof AST_VAR_DEC_1) {
                AST_VAR_DEC_1 vd_1 = (AST_VAR_DEC_1) cf_var_dec.vd;
                return new TYPE_ID(vd_1.GetSignature(), vd_1.type_with_id1.id_name);
            } else if (cf_var_dec.vd instanceof AST_VAR_DEC_2) {
                AST_VAR_DEC_2 vd_2 = (AST_VAR_DEC_2) cf_var_dec.vd;
                //add value data for IRme ----------------
                if(vd_2.exp instanceof AST_EXP_INT) {
                    int int_value = ((AST_EXP_INT) (vd_2.exp)).value;
                    return new TYPE_ID(vd_2.GetSignature(), vd_2.type_with_id1.id_name , int_value , "null");
                }
                if(vd_2.exp instanceof AST_EXP_STRING) {
                    String string_value = ((AST_EXP_STRING) (vd_2.exp)).value;
                    return new TYPE_ID(vd_2.GetSignature(), vd_2.type_with_id1.id_name , 0.5f ,string_value);
                }
                // ---------else , keep it "simple" like ex3--------------
                return new TYPE_ID(vd_2.GetSignature(), vd_2.type_with_id1.id_name);
            } else if (cf_var_dec.vd instanceof AST_VAR_DEC_3) {
                AST_VAR_DEC_3 vd_3 = (AST_VAR_DEC_3) cf_var_dec.vd;
                return new TYPE_ID(vd_3.GetSignature(), vd_3.type_with_id1.id_name);
            } else {
                System.out.format("unreachable code 1 in GetSignatures() of CFL_LIST");
                System.exit(0);
                return null;
            }
        } else {
            System.out.format("unreachable code 3 in GetSignatures() of CFL_LIST");
            System.exit(0);
            return null;
        }
    }

    public static void CheckIfOverrideLegal(TYPE_CLASS father_class, TYPE_ID current_field, AST_CFIELD cfield, TYPE_CLASS cur_class) {
        for (TYPE_CLASS tmp_superclass = father_class ; tmp_superclass != null ; tmp_superclass = tmp_superclass.father) {
            for (TYPE_LIST superclass_signatures = tmp_superclass.data_members ; superclass_signatures != null ; superclass_signatures=superclass_signatures.tail){
                if ((current_field.name).equals(superclass_signatures.head.name)){
                    if( !isOverriden(current_field , (TYPE_ID) superclass_signatures.head) ){
                        AST_Node.file_writer.print(String.format("ERROR(%d)", cfield.lineNumber));
                        AST_Node.file_writer.close();
                        System.out.format(">> ERROR: field name already exists in superclass and it's not valid override\n");
                        System.exit(0);
                    }
                    // legal over-ride
                    current_field.class_offset = superclass_signatures.head.class_offset;
                    return;
                }
            }
        }

        if(current_field.type instanceof TYPE_FUNCTION) {
            current_field.class_offset = (cur_class.num_methods+1)*4;
            cur_class.num_methods += 1;
        }
        else {
            current_field.class_offset = (cur_class.num_fields+1)*4;
            cur_class.num_fields += 1;
        }
    }

    // this function check if subClassType is override  superClassType
    public static boolean isOverriden(TYPE_ID subClassType , TYPE_ID superClassType){

        if ((subClassType.type.name.equals("int")) && (superClassType.type.name.equals("int"))){
            return true;
        }
        if ((subClassType.type.name.equals("string")) && (superClassType.type.name.equals("string"))){
            return true;
        }
        if ((subClassType.type.name.equals("void")) && (superClassType.type.name.equals("void"))){
            return true;
        }

        if (subClassType.type.isClass()){
            if (!superClassType.type.isClass()){
                return false;
            }
            if (subClassType.type.name.equals(superClassType.type.name)){
                return true;
            }
        }

        if (subClassType.type.isArray()){
            if (!superClassType.type.isArray()){
                return false;
            }
            if (subClassType.type.name.equals(superClassType.type.name)){
                return true;
            }
        }
        if (subClassType.type.isFunction()){
            if (!superClassType.type.isFunction()){
                return false;
            }
            TYPE_FUNCTION subClassFuncType = (TYPE_FUNCTION) subClassType.type;
            TYPE_FUNCTION superClassFuncType = (TYPE_FUNCTION) superClassType.type;
            return funcSignaturesComp(subClassFuncType , superClassFuncType);
        }
        System.out.format(">> ERROR : in isOverriden func in AST_CLASS_DEC_2  \n");
        return false;
    }

    public static boolean funcSignaturesComp(TYPE_FUNCTION func1 , TYPE_FUNCTION func2){
        if (!(func1.returnType.name.equals(func2.returnType.name))){
            return false;
        }
        if (func1.params == null){
            if (func2.params == null){
                return true;
            }
            return false;
        }
        //here func1.params != null
        if (func2.params == null){
            return false;
        }
        if (func1.params.numOfParams() != func2.params.numOfParams()){
            return false;
        }
//        if (func1.params == null){
//            return true;
//        }
        if (!(func1.params.head.name.equals(func2.params.head.name))){
            return false;
        }
        TYPE_LIST params1 = func1.params.tail;
        TYPE_LIST params2 = func2.params.tail;
        while (params1 != null){
            if (!(params1.head.name.equals(params2.head.name))){
                return false;
            }
            params1 = params1.tail;
            params2 = params2.tail;
        }
        return true;
    }

}
