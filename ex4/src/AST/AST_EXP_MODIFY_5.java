package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_EXP_MODIFY_5 extends AST_EXP {
    public AST_VAR var;
    public String id_name;
    public AST_EXP e;

    public AST_EXP_MODIFY_5(AST_VAR var, String id_name, AST_EXP e, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== exp -> var . ID ( exp );\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
        this.id_name = id_name;
        this.e = e;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE EXP_MODIFY_5\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (var != null) var.PrintMe();
        if (e != null) e.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("EXP_MODIFY_5\nID(%s);\n", id_name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);

    }

    public TYPE SemantMe() {
        TYPE v_type = this.var.SemantMe();
        if (!(v_type instanceof TYPE_CLASS)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println("error in EXP_MODIFY_5: var isn't TYPE_CLASS");
            System.exit(0);
            return null;
        }
        TYPE_CLASS var_class = (TYPE_CLASS) v_type;

        /************************************/
        /* [3] Look for id_name1 inside class&super_classes fields names */
        /************************************/
        for (TYPE_CLASS father_class = var_class; father_class != null; father_class = father_class.father) {
            for (TYPE_LIST it = father_class.data_members; it != null; it = it.tail) {
                if (it.head.name.equals(id_name)) {
                    TYPE_ID class_member = (TYPE_ID) it.head;
                    if (class_member.type.isFunction()) {
                        TYPE_FUNCTION t_func = (TYPE_FUNCTION) class_member.type;
                        if (t_func.params == null) {
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println("ERROR EXP_MODIFY_5: function called with 1 parameters but should have 0 parameters");
                            System.exit(0);
                            return null;
                        }
                        if (t_func.params.tail != null) {
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println("ERROR EXP_MODIFY_5: function called with 1 parameter but should have more");
                            System.exit(0);
                            return null;
                        }
                        TYPE t_head = t_func.params.head;
                        TYPE exp_type = e.SemantMe();
                        if (exp_type == null) {
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println(">> ERROR EXP_MODIFY_5: single parameter for function doesn't exist");
                            System.exit(0);
                            return null;
                        }
                        if (!isT1SubInstanceT2(exp_type, t_head)) { //first parameter type checking
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println("ERROR EXP_MODIFY_5: (only) parameter doesn't match");
                            System.exit(0);
                            return null;
                        }
                        // Good flow: all OK
                        return t_func.returnType;
                    } else {
                        AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                        AST_Node.file_writer.close();
                        System.out.println("error in EXP_MODIFY_5: ID isn't a class ***method***");
                        System.exit(0);
                        return null;
                    }
                }
            }
        }
        AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
        AST_Node.file_writer.close();
        System.out.println("error in EXP_MODIFY_5: ID isn't a class(or super-class) member");
        System.exit(0);
        return null;
    }

    public TEMP IRme()
    {
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP object = this.var.IRme();
        TEMP param1 = e.IRme();
        TEMP_LIST params_list = new TEMP_LIST(param1, null);
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Virtual_Call(object, t, id_name, params_list));
        return t;
    }

}
