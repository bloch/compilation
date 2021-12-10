package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_FUNC_DEC_2 extends AST_FUNC_DEC{
    public AST_TYPE_WITH_ID type_with_id1;
    public AST_TYPE_WITH_ID type_with_id2;
    public AST_STMT_LIST stmtList;

    public AST_FUNC_DEC_2(AST_TYPE type1, String id_name1, AST_TYPE type2, String id_name2, AST_STMT_LIST stmtList, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== funcDec -> type  ID (type ID) { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type_with_id1 = new AST_TYPE_WITH_ID(type1, id_name1, this.lineNumber);
        this.type_with_id2 = new AST_TYPE_WITH_ID(type2, id_name2, this.lineNumber);
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

        // TODO: append params to current new scope(to table) + check that types exist


        /** Difference between AST_FUNC_DEC_1 and AST_FUNC_DEC_2:
         *  AST_FUNC_DEC_2 has one parameter to function **/

        symbol_table.enter(type_with_id2.id_name,   function_signature.params.head);

        /** **/

        AST_Node.retTypesList = new TYPE_LIST(null, null);

        this.stmtList.SemantMe();
        //AST_Node.retTypesList.PrintTypeList();
        if(!CheckReturnTypes(function_signature.returnType)) {
            System.out.format(">> ERROR AST_FUNC_DEC_2 : Return Type exception");
            System.exit(0);
        }

        //this.checkReturnTypes();
        AST_Node.retTypesList = null;

        //this.checkReturnTypes();
        symbol_table.endScope();

        return null;
    }

    public TYPE GetSignature() {
        TYPE return_type = GetSignature(type_with_id1.t);
        if(return_type == null) {
            System.out.format(">> ERROR AST_FUNC_DEC_2 : Return Type doesn't exist");
            System.exit(0);
        }

        TYPE arg1_type = GetSignature(type_with_id2.t);
        if(arg1_type == null) {
            System.out.format(">> ERROR AST_FUNC_DEC_2 : first parameter type doesn't exist");
            System.exit(0);
        }

        return new TYPE_FUNCTION(return_type, this.type_with_id1.id_name, new TYPE_LIST(arg1_type, null));
    }

//    public boolean checkReturnTypes() {
//        System.out.print("in checkReturnTypes..");
//        for(AST_STMT_LIST stmt_list = this.stmtList; stmt_list != null; stmt_list = stmt_list.tail) {
//            if(stmt_list.head instanceof AST_STMT_RETURN) {
//                if(this.type_with_id1.t instanceof AST_TYPE_VOID) {
//                    continue;
//                }
//                else {
//                    return false;
//                }
//            }
//            else if(stmt_list.head instanceof AST_STMT_RETURN_EXP) {
//                AST_STMT_RETURN_EXP return_stmt = (AST_STMT_RETURN_EXP) stmt_list.head;
//
//                /**  actual return type **/
//                TYPE actual_return_type = return_stmt.exp.SemantMe();
//                if (actual_return_type == null) {
//                    System.out.println(">> ERROR FUNC_DEC_2: illegal return exp");
//                    System.exit(0);
//                }
//
//                /** expected return type **/
//                TYPE expected_return_type = GetSignature(type_with_id1.t);
//
//                /** check that actual_return_type can be interrepted as expected_return_type **/
//                if (!isT1SubInstanceT2(actual_return_type, expected_return_type)) {
//                    System.out.format(">> ERROR FUNC_DEC_2: return type mismatch\n",6,6);
//                    System.exit(0);
//                }
//
//            }
//            else {
//                continue;
//            }
//        }
//
//        return true;
//    }


}
