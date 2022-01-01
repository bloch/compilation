package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

import java.util.ArrayList;

public class AST_CLASS_DEC_2 extends AST_CLASS_DEC {
    public String id_name1;
    public String id_name2;
    public AST_CFIELD_LIST cfl;

    public AST_CLASS_DEC_2(String id_name1, String id_name2, AST_CFIELD_LIST cfl, int lineNumber) {

        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== classDec -> CLASS ID EXTENDS ID { cFieldList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name1 = id_name1;
        this.id_name2 = id_name2;
        this.cfl = cfl;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE CLASS_DEC_2\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("classDec_2\nID1(%s) ID2(%s)\n", id_name1, id_name2));

        if (cfl != null) cfl.PrintMe(SerialNumber);

    }

    public TYPE SemantMe()
    {
        // check if class name already exist, if yes --> exit(0)
        if (SYMBOL_TABLE.getInstance().find(id_name1) != null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_CLASS_DEC_2: class name %s already exists in scope\n", id_name1);
            System.exit(0);
        }

        SYMBOL_TABLE.getInstance().enter(id_name1, new TYPE_CLASS(null, id_name1, null));

        // check if super class exist , if not --> exit(0)
        TYPE_CLASS father_class = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(id_name2);
        if (father_class == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_CLASS_DEC_2: super class doesn't exist\n");
            System.exit(0);
            return null;
        }

        TYPE_CLASS t = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(id_name1);
        t.father = father_class;

        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        SYMBOL_TABLE.getInstance().beginScope("CLASS");
        AST_Node.cur_class = t;
        /***************************/
        /* [2] Semant Data Members */
        /***************************/

        this.cfl.SemantMe(t);

        /*****************/
        /* [3] End Scope */
        /*****************/
        AST_Node.cur_class = null;
        SYMBOL_TABLE.getInstance().endScope();

        /*********************************************************/
        /* [5] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;
    }


    public TEMP IRme()
    {
        TYPE_CLASS type_class = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(id_name1);
        ArrayList<String> function_labels = new ArrayList<String>();
//        System.out.println(type_class.num_methods);
//        boolean overriden = false;
//        String parent_dec;

        for(int i = 0; i < type_class.num_methods + 1; i++) {
            int current_offset = 4*i;
            boolean found_here = false;
            for (TYPE_LIST signatures = type_class.data_members; signatures != null; signatures=signatures.tail){
                    TYPE_ID member_type = (TYPE_ID) signatures.head;
                    if(member_type.type instanceof TYPE_FUNCTION && member_type.class_offset == current_offset) {
                        function_labels.add(type_class.name + "_" + member_type.name);
                        found_here = true;
                    }
            }
            if(found_here == true) {
                continue;
            }
            boolean finish = false;
            for (TYPE_CLASS tmp_superclass = type_class.father; tmp_superclass != null; tmp_superclass = tmp_superclass.father) {
                for (TYPE_LIST superclass_signatures = tmp_superclass.data_members ; superclass_signatures != null ; superclass_signatures=superclass_signatures.tail) {
                    TYPE_ID member_type = (TYPE_ID) superclass_signatures.head;
                    if(member_type.type instanceof TYPE_FUNCTION && member_type.class_offset == current_offset) {
                        function_labels.add(tmp_superclass.name + "_" + member_type.name);
                        finish = true;
                        break;
                    }
                }
                if(finish) {
                    break;
                }
            }
        }
        IR.getInstance().Add_IRcommand(new IRcommand_Class_Dec(type_class.name, function_labels));

        AST_Node.cur_class = type_class;
        this.cfl.IRme();
        AST_Node.cur_class = null;

        return null;
    }
}
