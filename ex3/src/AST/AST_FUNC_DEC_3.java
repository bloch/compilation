package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_FUNC_DEC_3 extends AST_FUNC_DEC {
    public AST_TYPE_WITH_ID type_with_id1;
    public AST_TYPE_WITH_ID type_with_id2;
    public AST_PSIK_TYPE_ID_LIST ptil;
    public AST_STMT_LIST stmtList;

    public AST_FUNC_DEC_3(AST_TYPE type1, String id_name1, AST_TYPE type2, String id_name2, AST_PSIK_TYPE_ID_LIST ptil, AST_STMT_LIST stmtList) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== funcDec -> type  ID (type ID psikTypeIdList) { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type_with_id1 = new AST_TYPE_WITH_ID(type1, id_name1);
        this.type_with_id2 = new AST_TYPE_WITH_ID(type2, id_name2);
        this.ptil = ptil;
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
        System.out.print("AST NODE FUNC_DEC_3\n");

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
                String.format("funcDec_3\n"));

        if (stmtList != null) stmtList.PrintMe(SerialNumber);
        if (ptil != null) ptil.PrintMe(SerialNumber);

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type_with_id1.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type_with_id2.SerialNumber);
    }

    public TYPE SemantMe()
    {
        SYMBOL_TABLE symbol_table = SYMBOL_TABLE.getInstance();
        TYPE_FUNCTION function_signature = (TYPE_FUNCTION) this.GetSignature();

        symbol_table.enter(this.type_with_id1.id_name, function_signature);

        symbol_table.beginScope();
        TYPE_LIST type_list = BuildTypeList(this.ptil);
        /** Difference between AST_FUNC_DEC_2 and AST_FUNC_DEC_3:
         *  AST_FUNC_DEC_3 has one parameter to function **/

        symbol_table.enter(type_with_id2.id_name,   function_signature.params.head);

        AST_PSIK_TYPE_ID_LIST tmp_ptil = this.ptil;
        TYPE_LIST tmp_type_list = type_list;
        while(tmp_ptil != null) {

            symbol_table.enter(tmp_ptil.type_with_id.id_name, tmp_type_list.head);

            tmp_ptil = tmp_ptil.tail;
            tmp_type_list = tmp_type_list.tail;
        }

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

        /** Difference between AST_FUNC_DEC_2 and AST_FUNC_DEC_3:
         *  AST_FUNC_DEC_3 has two or more parameters to function **/

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

        TYPE_LIST type_list = BuildTypeList(this.ptil);


        return new TYPE_FUNCTION(return_type, this.type_with_id1.id_name, new TYPE_LIST(arg1_type, type_list));
    }

    public TYPE_LIST BuildTypeList(AST_PSIK_TYPE_ID_LIST ptil) {
        if(ptil == null) {
            return null;
        }
        else {
            TYPE arg_type = null;                                // null to be removed in future
            if (ptil.type_with_id.t instanceof AST_TYPE_INT) {
                arg_type = TYPE_INT.getInstance();
            }
            if (ptil.type_with_id.t instanceof AST_TYPE_STRING) {
                arg_type = TYPE_STRING.getInstance();
            }
            if (ptil.type_with_id.t instanceof AST_TYPE_VOID) {
                arg_type = TYPE_VOID.getInstance();
            }
            // TODO: add conversion of AST_TYPE_ID also..

            return new TYPE_LIST(arg_type, BuildTypeList(ptil.tail));
        }
    }
}
