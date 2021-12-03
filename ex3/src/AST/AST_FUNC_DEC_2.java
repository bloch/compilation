package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_FUNC_DEC_2 extends AST_FUNC_DEC{
    public AST_TYPE_WITH_ID type_with_id1;
    public AST_TYPE_WITH_ID type_with_id2;
    public AST_STMT_LIST stmtList;

    public AST_FUNC_DEC_2(AST_TYPE type1, String id_name1, AST_TYPE type2, String id_name2, AST_STMT_LIST stmtList) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== funcDec -> type  ID (type ID) { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type_with_id1 = new AST_TYPE_WITH_ID(type1, id_name1);
        this.type_with_id2 = new AST_TYPE_WITH_ID(type2, id_name2);
        this.stmtList = stmtList;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE FUNC_DEC_2\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (type_with_id1 != null) type_with_id1.PrintMe();
        if (type_with_id2 != null) type_with_id2.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("funcDec_2\n"));


        if (stmtList != null) stmtList.PrintMe(SerialNumber);

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type_with_id1.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type_with_id2.SerialNumber);
    }

    public TYPE SemantMe() {
        SYMBOL_TABLE symbol_table = SYMBOL_TABLE.getInstance();

        TYPE_FUNCTION function_signature = (TYPE_FUNCTION) this.GetSignature();

        symbol_table.enter(this.type_with_id1.id_name, function_signature);

        symbol_table.beginScope();

        /** Difference between AST_FUNC_DEC_1 and AST_FUNC_DEC_2:
         *  AST_FUNC_DEC_2 has one parameter to function **/

        symbol_table.enter(type_with_id2.id_name,   function_signature.params.head);

        /** **/

        this.stmtList.head.SemantMe();

        AST_STMT_LIST tmp = this.stmtList.tail;
        while(tmp != null) {
            tmp.head.SemantMe();
            tmp = tmp.tail;
        }

        symbol_table.endScope();

        return null;
    }

    public TYPE GetSignature() {

        TYPE return_type = null;                                // null to be removed in future
        if (this.type_with_id1.t instanceof AST_TYPE_INT) {
            return_type = TYPE_INT.getInstance();
        }
        if (this.type_with_id1.t instanceof AST_TYPE_STRING) {
            return_type = TYPE_STRING.getInstance();
        }
        if (this.type_with_id1.t instanceof AST_TYPE_VOID) {
            return_type = TYPE_VOID.getInstance();
        }

        // TODO: add conversion of AST_TYPE_ID also..

        /** Difference between AST_FUNC_DEC_1 and AST_FUNC_DEC_2:
         *  AST_FUNC_DEC_2 has one parameter to function **/

        TYPE arg1_type = null;                                // null to be removed in future
        if (this.type_with_id2.t instanceof AST_TYPE_INT) {
            arg1_type = TYPE_INT.getInstance();
        }
        if (this.type_with_id2.t instanceof AST_TYPE_STRING) {
            arg1_type = TYPE_STRING.getInstance();
        }
        if (this.type_with_id2.t instanceof AST_TYPE_VOID) {
            arg1_type = TYPE_VOID.getInstance();
        }

        // TODO: add conversion of AST_TYPE_ID also..

        return new TYPE_FUNCTION(return_type, this.type_with_id1.id_name, new TYPE_LIST(arg1_type, null));
    }

}
