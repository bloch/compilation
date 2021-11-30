package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_CLASS_DEC_2 extends AST_CLASS_DEC {
    public String id_name1;
    public String id_name2;
    public AST_CFIELD_LIST cfl;

    public AST_CLASS_DEC_2(String id_name1, String id_name2, AST_CFIELD_LIST cfl) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== classDec -> CLASS ID EXTENDS ID { cFieldList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id_name1 = id_name1;
        this.id_name2 = id_name2;
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
        System.out.print("AST NODE CLASS_DEC_2\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("classDec_2\nID1(%s) ID2(%s)\n", id_name1, id_name2));

        if (cfl != null) cfl.PrintMe(SerialNumber);

    }

    public TYPE SemantMe()
    {
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

        /************************************************/
        /* [4] Enter the Class Type to the Symbol Table */
        /************************************************/
        TYPE_LIST class_signatures = cfl.GetSignatures();

        TYPE_CLASS father_class = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(id_name2);
        if (father_class == null) {
            /**************************/
            /* ERROR: undeclared class */
            /**************************/
            System.exit(0);
            return null;
        }

        TYPE_CLASS t = new TYPE_CLASS(father_class, id_name1, class_signatures);

        SYMBOL_TABLE.getInstance().enter(id_name1,t);

        /*********************************************************/
        /* [5] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;
    }

}
