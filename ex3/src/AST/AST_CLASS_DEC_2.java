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
        /************************************************/
        /* [4] Enter the Class Type to the Symbol Table */
        /************************************************/
        TYPE_LIST class_signatures = cfl.GetSignatures();

        //check if there is a shadowing between var/funcs/class/arrays in the class
        if (isShadowing(class_signatures)){
            System.exit(0);
        }

        // check if super class exist , if not --> exit(0)
        TYPE_CLASS father_class = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(id_name2);
        if (father_class == null) {
            System.out.format(">> ERROR : super class doesn't exist\n");
            System.exit(0);
            return null;
        }

        // check if class name already exist, if yes --> exit(0)
        if (SYMBOL_TABLE.getInstance().find(id_name1) != null) {
            System.out.format(">> ERROR [%d:%d] class name %s already exists in scope\n",2,2,id_name1);
            System.exit(0);
        }

        //check if there is overloading/shadowing inside the class or in super class!!
        // NOTE : if there is an overriding methods in dervied class it's not an error

        TYPE_CLASS t = new TYPE_CLASS(father_class, id_name1, class_signatures);

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
