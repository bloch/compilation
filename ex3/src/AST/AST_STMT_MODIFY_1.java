package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_STMT_MODIFY_1 extends AST_STMT{
    public String id_name1;

    public AST_STMT_MODIFY_1(String id_name1, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== stmt -> ID ( ); \n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name1 = id_name1;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE STMT_MODIFY_1\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STMT_MODIFY_1\nID(%s);\n", id_name1));

    }

    public TYPE SemantMe() {
        TYPE t = SYMBOL_TABLE.getInstance().find(this.id_name1);
        if(t == null) {
            System.out.format(">> STMT_MODIFY_1: %s not in global, then looking in father..\n", this.id_name1);
            t = isFuncInClassFields(this.id_name1);
            if(t == null) {
                AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                AST_Node.file_writer.close();
                System.out.format(">> ERROR STMT_MODIFY_1: illegal ID name(not in global and not in father's)\n");
                System.exit(0);
            }
        }
        if(!(t.isFunction())) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR STMT_MODIFY_1: ID name is not function(AST_STMT_MODIFY_1)\n",6,6);
            System.exit(0);
        }
        TYPE_FUNCTION t_func = (TYPE_FUNCTION) t;
        if (t_func.params != null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_MODIFY_1: should have parameters");
            System.exit(0);
            return null;
        }
        return null;
    }
}
