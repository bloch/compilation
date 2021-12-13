package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_CLASS_DEC_2 extends AST_CLASS_DEC {
    public String id_name1;
    public String id_name2;
    public AST_CFIELD_LIST cfl;

    public AST_CLASS_DEC_2(String id_name1, String id_name2, AST_CFIELD_LIST cfl, int lineNumber) {

        this.lineNumber = lineNumber;
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
        // check if class name already exist, if yes --> exit(0)
        if (SYMBOL_TABLE.getInstance().find(id_name1) != null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR [%d:%d] class name %s already exists in scope\n",2,2,id_name1);
            System.exit(0);
        }

        SYMBOL_TABLE.getInstance().enter(id_name1, new TYPE_CLASS(null, id_name1, null));

        // check if super class exist , if not --> exit(0)
        TYPE_CLASS father_class = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(id_name2);
        if (father_class == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR : super class doesn't exist\n");
            System.exit(0);
            return null;
        }

        TYPE_CLASS t = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(id_name1);
        t.father = father_class;

        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        SYMBOL_TABLE.getInstance().beginScope();
        AST_Node.cur_class = t;
        /***************************/
        /* [2] Semant Data Members */
        /***************************/

        this.cfl.SemantMe(t);
        //print to output file is handeled in isSignaturesValid func
        if (isSignaturesValid(t.data_members , this.cfl) == false){
            System.out.format(">> ERROR : some cfield signatures types doesn't appear inside symbol table\n");
            System.exit(0);
        }

        //check if there is a shadowing between var/funcs/class/arrays in the class
        //print to output file is handeled in isShadowing func
        if (isShadowing(t.data_members , this.cfl)){
            System.out.format(">> ERROR : shadowing inside class scope\n");
            System.exit(0);
        }

        //check if there is overloading/shadowing of fields inside this class or in super classes of this class
        // NOTE1 : if there is an overriding methods in dervied class it's not an error
        // NOTE2 : in this section of code father_class != null
        AST_CFIELD_LIST cfield_list = this.cfl;
        for (TYPE_LIST tmp_class_signatures = t.data_members ; tmp_class_signatures != null; tmp_class_signatures = tmp_class_signatures.tail , cfield_list = cfield_list.tail) {
            for (TYPE_CLASS tmp_superclass = father_class ; tmp_superclass != null ; tmp_superclass = tmp_superclass.father) {
                for (TYPE_LIST superclass_signatures = tmp_superclass.data_members ; superclass_signatures != null ; superclass_signatures=superclass_signatures.tail){
                    if ((tmp_class_signatures.head.name).equals(superclass_signatures.head.name)){
                        if( !isOverriden((TYPE_ID) tmp_class_signatures.head , (TYPE_ID) superclass_signatures.head) ){
                            AST_Node.file_writer.print(String.format("ERROR(%d)", cfield_list.head.lineNumber));
                            AST_Node.file_writer.close();
                            System.out.format(">> ERROR: field name already exists in superclass and it's not valid override\n");
                            System.exit(0);
                        }
                    }
                }
            }
        }


        /*****************/
        /* [3] End Scope */
        /*****************/
        AST_Node.cur_class = null;
        SYMBOL_TABLE.getInstance().endScope();

        /*********************************************************/
        /* [5] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;
    }

    // this function check if subClassType is override  superClassType
    public boolean isOverriden(TYPE_ID subClassType , TYPE_ID superClassType){

        if ((subClassType.type.name.equals("int")) && (superClassType.type.name.equals("int"))){
            return true;
        }
        if ((subClassType.type.name.equals("string")) && (superClassType.type.name.equals("string"))){
            return true;
        }
        if ((subClassType.type.name.equals("void")) && (superClassType.type.name.equals("void"))){
            return true;
        }

        if (subClassType.type.isClass()){
            if (!superClassType.type.isClass()){
                return false;
            }
            if (subClassType.type.name.equals(superClassType.type.name)){
                return true;
            }
        }

        if (subClassType.type.isArray()){
            if (!superClassType.type.isArray()){
                return false;
            }
            if (subClassType.type.name.equals(superClassType.type.name)){
                return true;
            }
        }
        if (subClassType.type.isFunction()){
            if (!superClassType.type.isFunction()){
                return false;
            }
            TYPE_FUNCTION subClassFuncType = (TYPE_FUNCTION) subClassType.type;
            TYPE_FUNCTION superClassFuncType = (TYPE_FUNCTION) superClassType.type;
            return funcSignaturesComp(subClassFuncType , superClassFuncType);
        }
        System.out.format(">> ERROR : in isOverriden func in AST_CLASS_DEC_2  \n");
        return false;
    }

    public boolean funcSignaturesComp(TYPE_FUNCTION func1 , TYPE_FUNCTION func2){
        if (!(func1.returnType.name.equals(func2.returnType.name))){
            return false;
        }
        if (func1.params == null){
            if (func2.params == null){
                return true;
            }
            return false;
        }
        //here func1.params != null
        if (func2.params == null){
            return false;
        }
        if (func1.params.numOfParams() != func2.params.numOfParams()){
            return false;
        }
//        if (func1.params == null){
//            return true;
//        }
        if (!(func1.params.head.name.equals(func2.params.head.name))){
            return false;
        }
        TYPE_LIST params1 = func1.params.tail;
        TYPE_LIST params2 = func2.params.tail;
        while (params1 != null){
            if (!(params1.head.name.equals(params2.head.name))){
                return false;
            }
            params1 = params1.tail;
            params2 = params2.tail;
        }
        return true;
    }

}
