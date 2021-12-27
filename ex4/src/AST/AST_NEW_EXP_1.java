package AST;
import SYMBOL_TABLE.*;
import TYPES.*;
import java.util.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_NEW_EXP_1 extends AST_NEW_EXP {
    public AST_TYPE t;

    public AST_NEW_EXP_1(AST_TYPE t, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== newExp -> NEW type\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.t = t;
    }
    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE NEW_EXP_1\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (t != null) t.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("newExp_1"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,t.SerialNumber);

    }

    public TYPE SemantMe() {
        return GetSignature(this.t);
    }

    public TEMP IRme(){
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        AST_TYPE_ID tc = (AST_TYPE_ID) t; //must be type class , otherwise it will fail at semantic level


        //need to get more then just class neme...
        String name = tc.name;

        TYPE_CLASS type_class = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(name);//retirve class from symbol table(it's there because it's global)

        int size_of_class = type_class.num_fields + 1; //include vt

        //retrive fields(not methods!) and there default data if exist
        TYPE_ID[] fields_array = new TYPE_ID[size_of_class];


        for (TYPE_CLASS tmp_class = type_class ; tmp_class !=null ; tmp_class = tmp_class.father) {
            for (TYPE_LIST it=tmp_class.data_members ; it != null ; it=it.tail) {
                TYPE_ID class_member = (TYPE_ID) it.head;
                if (!class_member.type.isFunction()){
                    int field_index = (int) (class_member.class_offset / 4);
                    if (fields_array[field_index] != null){
                        fields_array[field_index] = class_member;
                    }
                    //else this value has been override...

                }
            }
        }




        IR.getInstance().Add_IRcommand(new IRcommand_New_Class(dst , fields_array , size_of_class , name));
        return dst;
    }

}