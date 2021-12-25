package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_EXP_MODIFY_1 extends AST_EXP {
    public String id_name;

    public AST_EXP_MODIFY_1(String id_name , int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== exp -> ID ( ); \n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
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
        System.out.print("AST NODE EXP_MODIFY_1\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("EXP_MODIFY_1\nID(%s);\n", id_name));

    }

    public TYPE SemantMe() {
        TYPE t = SYMBOL_TABLE.getInstance().findNotInGlobalScope(this.id_name);
        if(t == null) {
            System.out.format(">> EXP_MODIFY_1: %s not in local scopes, then looking in father..\n", this.id_name);
            t = isFuncInClassFields(this.id_name);
            if (t == null) {
                System.out.format(">> EXP_MODIFY_1: %s not in local scopes & fathers, then looking in global..\n", this.id_name);
                t = SYMBOL_TABLE.getInstance().find(this.id_name);
                if (t == null) {
                    AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                    AST_Node.file_writer.close();
                    System.out.format(">> ERROR EXP_MODIFY_1: illegal ID name(not in global and not in fathers and locals)\n");
                    System.exit(0);
                }
            }
        }

        if(!(t.isFunction())) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR EXP_MODIFY_1: ID name is not function(AST_STMT_MODIFY_1)\n",6,6);
            System.exit(0);
        }
        TYPE_FUNCTION t_func = (TYPE_FUNCTION) t;
        if (t_func.params != null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR EXP_MODIFY_1: should have parameters");
            System.exit(0);
            return null;
        }
        // Good flow
        return t_func.returnType;
    }

    public TEMP IRme() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_Call_Func(dst, id_name, null, null));
        return dst;
    }
}
