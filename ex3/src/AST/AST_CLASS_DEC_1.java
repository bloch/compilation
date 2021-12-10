package AST;
import SYMBOL_TABLE.*;
import TYPES.*;


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
        // check if class name already exist, if yes --> exit(0)
        if (SYMBOL_TABLE.getInstance().find(id_name1) != null) {
            System.out.format(">> ERROR [%d:%d] class name %s already exists in scope\n",2,2,id_name1);
            System.exit(0);
        }

        //TYPE_CLASS t = new TYPE_CLASS(null, id_name1, class_signatures);
        SYMBOL_TABLE.getInstance().enter(id_name1, new TYPE_CLASS(null, id_name1, null));
        /******************************** OLD CODE ********************************************************/
        //TYPE_LIST class_signatures = cfl.GetSignatures();

        //check if all class signatures appears inside symbol table
//        if (isSignaturesValid(class_signatures) == false){
//            System.out.format(">> ERROR : some cfield signatures types doesn't appear inside symbol table\n");
//            System.exit(0);
//        }
//
//        //check if there is a shadowing between var/funcs/class/arrays in the class
//        if (isShadowing(class_signatures)){
//            System.out.format(">> ERROR : shadowing inside class scope\n");
//            System.exit(0);
//        }

//        // check if class name already exist, if yes --> exit(0)
//        if (SYMBOL_TABLE.getInstance().find(id_name1) != null) {
//            System.out.format(">> ERROR [%d:%d] class name %s already exists in scope\n",2,2,id_name1);
//            System.exit(0);
//        }
        /******************************** OLD CODE ********************************************************/
        TYPE_CLASS t = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(id_name1);
        /******************************** OLD CODE ********************************************************/
        //t.data_members = class_signatures;

////       FOR DEBUG ONLY
//        TYPE_CLASS t2 = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(id_name1);
//        TYPE_LIST tmp = t2.data_members;
//        tmp.PrintTypeList();


//        TYPE_CLASS t = new TYPE_CLASS(null, id_name1, class_signatures);
//        SYMBOL_TABLE.getInstance().enter(id_name1,t);
        /******************************** OLD CODE ********************************************************/
        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        SYMBOL_TABLE.getInstance().beginScope();

        /***************************/
        /* [2] Semant Data Members */
        /***************************/

        this.cfl.SemantMe(t);
        if (isSignaturesValid(t.data_members) == false){
            System.out.format(">> ERROR : some cfield signatures types doesn't appear inside symbol table\n");
            System.exit(0);
        }

        //check if there is a shadowing between var/funcs/class/arrays in the class
        if (isShadowing(t.data_members)){
            System.out.format(">> ERROR : shadowing inside class scope\n");
            System.exit(0);
        }

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
