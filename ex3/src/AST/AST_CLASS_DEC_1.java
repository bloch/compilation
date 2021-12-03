package AST;
import SYMBOL_TABLE.*;
import TYPES.*;
import java.util.*;

public class AST_CLASS_DEC_1 extends AST_CLASS_DEC {
    public String id_name1;
    public AST_CFIELD_LIST cfl;

    public AST_CLASS_DEC_1(String id_name1, AST_CFIELD_LIST cfl) {
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
        /************************************************/
        /* [4] Enter the Class Type to the Symbol Table */
        /************************************************/
        TYPE_LIST class_signatures = cfl.GetSignatures();

        //check if there is a shadowing between var/funcs/class/arrays in the class
        if (isShadowing(class_signatures)){
            System.exit(0);
        }

        // heck if class name already exist, if yes --> exit(0)
        if (SYMBOL_TABLE.getInstance().find(id_name1) != null) {
            System.out.format(">> ERROR [%d:%d] class name %s already exists in scope\n",2,2,id_name1);
            System.exit(0);
        }

        TYPE_CLASS t = new TYPE_CLASS(null, id_name1, class_signatures);
        SYMBOL_TABLE.getInstance().enter(id_name1,t);

        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        SYMBOL_TABLE.getInstance().beginScope();

        /***************************/
        /* [2] Semant Data Members */
        /***************************/

        this.cfl.SemantMe();

        /*****************/
        /* [3] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope();

        /*********************************************************/
        /* [5] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;
    }

}
