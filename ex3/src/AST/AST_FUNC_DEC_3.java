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

        // TODO: append params to current new scope(to table) + check that types exist

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

        TYPE return_type = GetSignature(type_with_id1.t);

        this.stmtList.SemantMe(return_type);

//        TYPE return_type = GetSignature(type_with_id1.t);
//
//        TYPE t = this.stmtList.SemantMe(return_type);
//        if (t == null && !(return_type instanceof TYPE_VOID))
//        {
//            System.out.println(">> ERROR STMT_LIST: function doesn't have return statement but return type isn't void");
//            System.exit(0);
//            return null;
//        }

        symbol_table.endScope();

        return null;
    }

    public TYPE GetSignature() {

        TYPE return_type = GetSignature(type_with_id1.t);

        TYPE arg1_type = GetSignature(type_with_id2.t);

        TYPE_LIST type_list = BuildTypeList(this.ptil);

        return new TYPE_FUNCTION(return_type, this.type_with_id1.id_name, new TYPE_LIST(arg1_type, type_list));
    }

    public TYPE_LIST BuildTypeList(AST_PSIK_TYPE_ID_LIST ptil) {
        if(ptil.tail == null) {
            return new TYPE_LIST(GetSignature(ptil.type_with_id.t), null);
        }
        else {
            return new TYPE_LIST(GetSignature(ptil.type_with_id.t), BuildTypeList(ptil.tail));
        }
    }
}
