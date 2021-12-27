package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_STMT_MODIFY_6 extends AST_STMT{
    public AST_VAR var;
    public String id_name1;
    public AST_EXP exp;
    public AST_PSIK_EXP_LIST l;

    public AST_STMT_MODIFY_6(AST_VAR var, String id_name1, AST_EXP exp, AST_PSIK_EXP_LIST l, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== stmt -> var . ID ( exp psikExpList );\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
        this.id_name1 = id_name1;
        this.exp = exp;
        this.l = l;
    }

    /***********************************************/
    /* The default message for an exp var AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = EXP VAR AST NODE */
        /************************************/
        System.out.print("AST NODE STMT_MODIFY_6\n");

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
                String.format("STMT_MODIFY_6\nvar.ID(%s)\n(exp psikExpList);\n", id_name1));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);


        this.l.PrintMe(SerialNumber);

    }

    public TYPE SemantMe() {
        TYPE v_type = this.var.SemantMe();
        if (!(v_type instanceof TYPE_CLASS)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println("error in STMT_MODIFY_6: var isn't TYPE_CLASS");
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
                            System.out.println("ERROR STMT_MODIFY_6: function called with 2+ parameters but should 0 parameters");
                            System.exit(0);
                            return null;
                        }
                        if (t_func.params.tail == null) {
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println("ERROR STMT_MODIFY_6: function called with 2+ parameters but should have 1 parameters");
                            System.exit(0);
                            return null;
                        }
                        TYPE t_head = t_func.params.head;
                        TYPE exp_type = exp.SemantMe();
                        if (exp_type == null) {
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println(">> ERROR STMT_MODIFY_6: first parameter for function doesn't exist");
                            System.exit(0);
                            return null;
                        }
                        if (!isT1SubInstanceT2(exp_type, t_head)) { //first parameter type checking
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println("error in STMT_MODIFY_6: first parameter doesn't match");
                            System.exit(0);
                            return null;
                        }

                        TYPE_LIST l_type_list = l.GetSignatures();
                        TYPE_LIST tmp_l = l_type_list;
                        TYPE_LIST tmp_p = t_func.params.tail;

                        while(tmp_l != null && tmp_p != null) {
                            if (tmp_l.head == null) {
                                AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                                AST_Node.file_writer.close();
                                System.out.println(">> ERROR STMT_MODIFY_6: some parameter(second or higher) for function doesn't exist");
                                System.exit(0);
                                return null;
                            }
                            if (!isT1SubInstanceT2(tmp_l.head, tmp_p.head)) {
                                AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                                AST_Node.file_writer.close();
                                System.out.println("error in EXP_MODIFY_6: some parameter(second or higher) doesn't match");
                                System.exit(0);
                                return null;
                            }
                            tmp_l = tmp_l.tail;
                            tmp_p = tmp_p.tail;
                        }
                        if(tmp_l != null && tmp_p == null)
                        {
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println("error in EXP_MODIFY_6: too many parameters given for function");
                            System.exit(0);
                            return null;
                        }
                        else if (tmp_l == null && tmp_p != null)
                        {
                            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.println("error in EXP_MODIFY_6: not enough parameters given for function");
                            System.exit(0);
                            return null;
                        }
                        // Good flow: all OK
                        return null;
                    } else {
                        AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                        AST_Node.file_writer.close();
                        System.out.println("error in STMT_MODIFY_6: ID isn't a class ***method***");
                        System.exit(0);
                        return null;
                    }
                }
            }
        }
        AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
        AST_Node.file_writer.close();
        System.out.println("error in STMT_MODIFY_6: ID isn't a class(or super-class) member");
        System.exit(0);
        return null;
    }

    public TEMP IRme()
    {
        TEMP object = this.var.IRme();
        TEMP param1 = exp.IRme();
        TEMP_LIST params_list = new TEMP_LIST(param1, null);
        AST_PSIK_EXP_LIST tmp = l;
        while(tmp != null) {
            TEMP next_param = tmp.head.IRme();
            params_list.AddToTEMPList(next_param);
            tmp = tmp.tail;
        }
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Virtual_Call(object, null, id_name1, params_list));
        return null;
    }

}
