package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

import java.util.ArrayList;

public class AST_CLASS_DEC_1 extends AST_CLASS_DEC {
    public String id_name1;
    public AST_CFIELD_LIST cfl;

    public AST_CLASS_DEC_1(String id_name1, AST_CFIELD_LIST cfl , int lineNumber) {

        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== classDec -> CLASS ID { cFieldList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name1 = id_name1;
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
        System.out.print("AST NODE CLASS_DEC_1\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("classDec_1\nID(%s)\n", id_name1));

        if (cfl != null) cfl.PrintMe(SerialNumber);

    }

    public TYPE SemantMe()
    {
        // check if class name already exist, if yes --> exit(0)
        if (SYMBOL_TABLE.getInstance().find(id_name1) != null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_CLASS_DEC_1: class name %s already exists in scope\n",id_name1);
            System.exit(0);
        }

        SYMBOL_TABLE.getInstance().enter(id_name1, new TYPE_CLASS(null, id_name1, null));

        TYPE_CLASS t = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(id_name1);

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
        for(TYPE_LIST tmp_data_members = type_class.data_members; tmp_data_members != null; tmp_data_members = tmp_data_members.tail) {
            TYPE_ID member_type = (TYPE_ID) tmp_data_members.head;
            if(member_type.type instanceof TYPE_FUNCTION) {
                function_labels.add(type_class.name + "_" + member_type.name);
            }
        }
        IR.getInstance().Add_IRcommand(new IRcommand_Class_Dec(type_class.name, function_labels));

        AST_Node.cur_class = type_class;
        this.cfl.IRme();
        AST_Node.cur_class = null;

        return null;
    }

}
