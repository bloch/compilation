package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_STMT_MODIFY_5 extends AST_STMT{
    public AST_VAR var;
    public String id_name1;
    public AST_EXP exp;

    public AST_STMT_MODIFY_5(AST_VAR var, String id_name1, AST_EXP exp, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== stmt -> var . ID ( exp );\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
        this.id_name1 = id_name1;
        this.exp = exp;
    }

    /***********************************************/
    /* The default message for an exp var AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = EXP VAR AST NODE */
        /************************************/
        System.out.print("AST NODE STMT_MODIFY_5\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (var != null) var.PrintMe();
        if (exp != null) exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STMT_MODIFY_5\nvar.ID(%s)\n( exp );\n", id_name1));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
    }

    public TYPE SemantMe() {
        TYPE v_type = this.var.SemantMe();
        if (!(v_type instanceof TYPE_CLASS)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println("error in STMT_MODIFY_5: var isn't TYPE_CLASS");
            System.exit(0);
            return null;
        }
        TYPE_CLASS var_class = (TYPE_CLASS) v_type;

        /************************************/
        /* [3] Look for id_name1 inside class&super_classes fields names */
        /************************************/
        for (TYPE_CLASS father_class = var_class; father_class != null; father_class = father_class.father) {
            for (TYPE_LIST it = father_class.data_members; it != null; it = it.tail) {
                if (it.head.name.equals(id_name1)) {
                    TYPE_ID class_member = (TYPE_ID) it.head;
                    if (class_member.type.isFunction()) {
                        TYPE_FUNCTION t_func = (TYPE_FUNCTION) class_member.type;
                        if (t_func.params == null) {
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println("ERROR STMT_MODIFY_5: function called with 1 parameters but should have 0 parameters");
                            System.exit(0);
                            return null;
                        }
                        if (t_func.params.tail != null) {
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println("ERROR STMT_MODIFY_5: function called with 1 parameter but should have more");
                            System.exit(0);
                            return null;
                        }
                        TYPE t_head = t_func.params.head;
                        TYPE exp_type = exp.SemantMe();
                        if (exp_type == null) {
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println(">> ERROR STMT_MODIFY_5: single parameter for function doesn't exist");
                            System.exit(0);
                            return null;
                        }
                        if (!isT1SubInstanceT2(exp_type, t_head)) {
                        //if (exp_type != t_head) {  //first parameter type checking
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println("ERROR STMT_MODIFY_5: (only) parameter doesn't match");
                            System.exit(0);
                            return null;
                        }
                        // Good flow: all OK
                        return null;
                    } else {
                        AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                        AST_Node.file_writer.close();
                        System.out.println("error in STMT_MODIFY_5: ID isn't a class ***method***");
                        System.exit(0);
                        return null;
                    }
                }
            }
        }
        AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
        AST_Node.file_writer.close();
        System.out.println("error in STMT_MODIFY_5: ID isn't a class(or super-class) member");
        System.exit(0);
        return null;
    }
}
