package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_EXP_MODIFY_3 extends AST_EXP {
    public String id_name;
    public AST_EXP e;
    public AST_PSIK_EXP_LIST l;

    public AST_EXP_MODIFY_3(String id_name, AST_EXP e, AST_PSIK_EXP_LIST l, int lineNumber) {

        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== exp -> ID ( exp psikExpList );\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name = id_name;
        this.e = e;
        this.l = l;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE EXP_MODIFY_3\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (e != null) e.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("EXP_MODIFY_3\nID(%s);\n", id_name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);

        this.l.PrintMe(SerialNumber);

    }


    public TYPE SemantMe() {
        TYPE t = SYMBOL_TABLE.getInstance().findNotInGlobalScope(this.id_name);
        if(t == null) {
            System.out.format(">> EXP_MODIFY_3: %s not in local scopes, then looking in father..\n", this.id_name);
            t = isFuncInClassFields(this.id_name);
            if (t == null) {
                System.out.format(">> EXP_MODIFY_3: %s not in local scopes & fathers, then looking in global..\n", this.id_name);
                t = SYMBOL_TABLE.getInstance().find(this.id_name);
                if (t == null) {
                    AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                    AST_Node.file_writer.close();
                    System.out.format(">> ERROR EXP_MODIFY_3: illegal ID name(not in global and not in fathers and locals)\n");
                    System.exit(0);
                }
            }
        }

        if (!(t instanceof TYPE_FUNCTION)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR EXP_MODIFY_3: not a function");
            System.exit(0);
            return null;
        }
        TYPE_FUNCTION t_func = (TYPE_FUNCTION) t;
        if (t_func.params == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR EXP_MODIFY_3: shouldn't have parameters");
            System.exit(0);
            return null;
        }
        if (t_func.params.tail == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR EXP_MODIFY_3: function called with 2+ parameters but should have 1 parameters");
            System.exit(0);
            return null;
        }
        //first parameter type checking
        TYPE t_head = t_func.params.head;
        TYPE exp_type = e.SemantMe();
        if (exp_type == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR EXP_MODIFY_3: first parameter for function doesn't exist");
            System.exit(0);
            return null;
        }
        if (!isT1SubInstanceT2(exp_type, t_head)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR EXP_MODIFY_3: first parameter doesn't match");
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
                System.out.println(">> ERROR EXP_MODIFY_3: some parameter(second or higher) for function doesn't exist");
                System.exit(0);
                return null;
            }
            if (!isT1SubInstanceT2(tmp_l.head, tmp_p.head)) {
                AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                AST_Node.file_writer.close();
                System.out.println(">> ERROR EXP_MODIFY_3: some parameters don't match");
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
            System.out.println(">> ERROR EXP_MODIFY_3: too many parameters given for function");
            System.exit(0);
            return null;
        }
        else if (tmp_l == null && tmp_p != null)
        {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR EXP_MODIFY_3: not enough parameters given for function");
            System.exit(0);
            return null;
        }
        // tmp_l and tmp_p now should be null both
        return t_func.returnType;
    }

    public TEMP IRme()
    {
        TEMP t0 = TEMP_FACTORY.getInstance().getFreshTEMP();
        System.out.println(t0.getSerialNumber());
        TEMP param1 = e.IRme();
        System.out.println(param1.getSerialNumber());
        TEMP_LIST params_list = new TEMP_LIST(param1, null);
        AST_PSIK_EXP_LIST tmp = l;
        while(tmp != null) {
            TEMP next_param = tmp.head.IRme();
            System.out.println(next_param.getSerialNumber());
            params_list.AddToTEMPList(next_param);
            tmp = tmp.tail;
        }

//        if(t0 == null) {
//            System.out.println("WHAT THE FUCK");
//        }
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Call(t0, id_name, params_list));
        return t0;
    }
}
