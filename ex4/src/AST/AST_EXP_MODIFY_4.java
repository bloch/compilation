package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_EXP_MODIFY_4 extends AST_EXP {
    AST_VAR var;
    public String id_name;

    public int class_offset;

    public AST_EXP_MODIFY_4(AST_VAR var, String id_name, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== exp -> var . ID ( ); \n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
        this.id_name = id_name;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE EXP_MODIFY_4\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (var != null) var.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("EXP_MODIFY_4\nID(%s);\n", id_name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);

    }

    public TYPE SemantMe() {
        TYPE v_type = this.var.SemantMe();
        if (!(v_type instanceof TYPE_CLASS)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR EXP_MODIFY_4: var isn't TYPE_CLASS");
            System.exit(0);
            return null;
        }
        TYPE_CLASS var_class = (TYPE_CLASS) v_type;

        /************************************/
        /* [3] Look for id_name1 inside class&super_classes fields names */
        /************************************/
        for (TYPE_CLASS father_class = var_class; father_class !=null; father_class = father_class.father) {
            for (TYPE_LIST it=father_class.data_members; it != null; it=it.tail) {
                if (it.head.name.equals(id_name)) {
                    TYPE_ID class_member = (TYPE_ID) it.head;
                    if (class_member.type.isFunction()) {
                        TYPE_FUNCTION t_func = (TYPE_FUNCTION) class_member.type;
                        if (t_func.params != null) {
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println(">> ERROR EXP_MODIFY_4: should have parameters");
                            System.exit(0);
                        }
                        // Good flow: all OK
                        this.class_offset = class_member.class_offset;
                        return t_func.returnType;
                    }
                    else {
                        AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                        AST_Node.file_writer.close();
                        System.out.println(">> ERROR EXP_MODIFY_4: ID isn't a class ***method***");
                        System.exit(0);
                        return null;
                    }
                }
            }
        }
        AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
        AST_Node.file_writer.close();
        System.out.println(">> ERROR EXP_MODIFY_4: ID isn't a class(or super-class) member");
        System.exit(0);
        return null;
    }

    public TEMP IRme()
    {
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP object = this.var.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Virtual_Call(object, t, id_name, null, this.class_offset));
        return t;
    }
}
