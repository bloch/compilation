package AST;
import SYMBOL_TABLE.*;
import TYPES.*;
import java.util.*;

public class AST_CLASS_DEC extends AST_DEC {
    public AST_CLASS_DEC cd;

    public AST_CLASS_DEC() {

    }

    public AST_CLASS_DEC(AST_CLASS_DEC cd) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.cd = cd;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE CLASS_DEC\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (cd != null) cd.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("classDec"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cd.SerialNumber);
    }

    public TYPE SemantMe() {
        return this.cd.SemantMe();
    }
    public TYPE GetSignature() {
        return this.cd.GetSignature();
    }


    //this function check if there is shadowing between vars, funcs , classes inside a class
    public boolean isShadowing(TYPE_LIST class_signatures){
        HashSet<String> fieldsNamesSet = new HashSet();
        for (TYPE_LIST tmp_class_signatures = class_signatures ; tmp_class_signatures != null; tmp_class_signatures = tmp_class_signatures.tail) {
            TYPE typeField = tmp_class_signatures.head;
            //retrieve field name
            String fieldName = typeField.name;
            if (fieldsNamesSet.contains(fieldName)){
                //field name already exist --> shadowing --> exit(0)
                System.out.format(">> ERROR [%d:%d] shadowing in field %s -\n",6,6,fieldName);
                return true;
            }
            else{
                fieldsNamesSet.add(fieldName);
            }
        }
        return false;
    }

    public boolean isSignaturesValid(TYPE_LIST class_signatures){
        for (TYPE_LIST tmp_class_signatures = class_signatures ; tmp_class_signatures != null; tmp_class_signatures = tmp_class_signatures.tail) {
            TYPE typeField = tmp_class_signatures.head;
            if (typeField == null){  //type doesn't exist in symbol table
                return false;
            }
        }
        return true;
    }
}
