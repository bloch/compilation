package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_STMT_MODIFY_4 extends AST_STMT{
    public AST_VAR var;
    public String id_name1;

    public AST_STMT_MODIFY_4(AST_VAR var, String id_name1) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== stmt -> var . ID ( ); \n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
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
        System.out.print("AST NODE STMT_MODIFY_4\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (var != null) var.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STMT_MODIFY_4\nvar . ID(%s);\n", id_name1));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);

    }

    public TYPE SemantMe()
    {
        TYPE v_type = this.var.SemantMe();
        if (!(v_type instanceof TYPE_CLASS))
        {
            System.out.println("error in STMT_MODIFY_4: var isn't TYPE_CLASS");
            System.exit(0);
            return null;
        }
        TYPE_CLASS var_class = (TYPE_CLASS) v_type;
        TYPE_LIST tmp = var_class.data_members;
        //TODO: check for method in fathers
        if (tmp.head.name.equals(this.id_name1))
        {
            if (tmp.head instanceof TYPE_FUNCTION)
            {
                TYPE_FUNCTION f = (TYPE_FUNCTION) tmp.head;
                return null;
            }
            else
            {
                System.out.println("error in STMT_MODIFY_4: ID isn't a class method");
                System.exit(0);
                return null;
            }
        }
        tmp = tmp.tail;
        while (tmp != null)
        {
            if (tmp.head.name.equals(this.id_name1))
            {
                if (tmp.head instanceof TYPE_FUNCTION)
                {
                    TYPE_FUNCTION f = (TYPE_FUNCTION) tmp.head;
                    return null;
                }
                else
                {
                    System.out.println("error in STMT_MODIFY_4: ID isn't a class method");
                    System.exit(0);
                    return null;
                }
            }
            tmp = tmp.tail;
        }
        System.out.println("error in STMT_MODIFY_4: ID isn't a class member");
        System.exit(0);
        return null;
    }

}
