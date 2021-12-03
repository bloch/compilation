package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_ARRAY_TYPE_DEF_1 extends AST_ARRAY_TYPE_DEF {
    String id_name;
    public AST_TYPE type;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_ARRAY_TYPE_DEF_1(String id_name, AST_TYPE type)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== arrayTypedef -> ARRAY ID = type [];\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name = id_name;
        this.type = type;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE ARRAY_TYPE_DEF_1\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (type != null) type.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("ARRAY_TYPE_DEF_1\nID (%s);\n", id_name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
    }

    public TYPE SemantMe() {
        // want: add to symbol table + return type.

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        TYPE type_of_var = GetSignature(type);
        if (type_of_var == null) {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type_of_var.name);
            System.exit(0);
        }

        /** type_of_var is the type of array **/

        /**************************************/
        /* [2] Check That Name does NOT exist */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().find(id_name) != null) {
            System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,id_name);
        }

        /***************************************************/
        /* [3] Enter the Function Type to the Symbol Table */
        /***************************************************/
        TYPE_ARRAY type_array = new TYPE_ARRAY(type_of_var, id_name);

        SYMBOL_TABLE.getInstance().enter(id_name,type_array);

        /*********************************************************/
        /* [4] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;

    }

}
