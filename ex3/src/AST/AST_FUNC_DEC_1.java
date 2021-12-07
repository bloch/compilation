package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_FUNC_DEC_1 extends AST_FUNC_DEC {
    public AST_TYPE_WITH_ID type_with_id1;
    public AST_STMT_LIST stmtList;

    public AST_FUNC_DEC_1(AST_TYPE type1, String id_name1, AST_STMT_LIST stmtList) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== funcDec -> type  ID () { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/

        this.type_with_id1 = new AST_TYPE_WITH_ID(type1, id_name1);
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
        System.out.print("AST NODE FUNC_DEC_1\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (type_with_id1 != null) type_with_id1.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("funcDec_1\n"));

        if (stmtList != null) stmtList.PrintMe(SerialNumber);


        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type_with_id1.SerialNumber);

    }

    public TYPE SemantMe() {

        SYMBOL_TABLE symbol_table = SYMBOL_TABLE.getInstance();

        TYPE_FUNCTION function_signature = (TYPE_FUNCTION) this.GetSignature();

        symbol_table.enter(this.type_with_id1.id_name, function_signature);

        symbol_table.beginScope();


        this.stmtList.SemantMe();

        //this.checkReturnTypes();
        symbol_table.endScope();

        return null;
    }
    public TYPE GetSignature() {
        TYPE return_type = GetSignature(type_with_id1.t);
        if(return_type == null) {
            System.out.format(">> ERROR AST_FUNC_DEC_1 : Return Type doesn't exist");
            System.exit(0);
        }
        return new TYPE_FUNCTION(return_type, this.type_with_id1.id_name, null);
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
//                /** expected return type **/
//                TYPE expected_return_type = GetSignature(type_with_id1.t);
//                if(expected_return_type instanceof TYPE_VOID) {
//                    System.out.println(">> ERROR FUNC_DEC_1: tried to return exp in a void function");
//                    System.exit(0);
//                }
//
//                AST_STMT_RETURN_EXP return_stmt = (AST_STMT_RETURN_EXP) stmt_list.head;
//
//                /**  actual return type **/
//                TYPE actual_return_type = return_stmt.exp.SemantMe();
//                if (actual_return_type == null) {
//                    System.out.println(">> ERROR FUNC_DEC_1: illegal return exp");
//                    System.exit(0);
//                }
//
//
//                /** check that actual_return_type can be interrepted as expected_return_type **/
//                if (!isT1SubInstanceT2(actual_return_type, expected_return_type)) {
//                    System.out.format(">> ERROR FUNC_DEC_1: return type mismatch\n",6,6);
//                    System.exit(0);
//                }
//
//            }
//            else if(stmt_list.head instanceof AST_STMT_IF) {
//                AST_STMT_IF if_stmt = (AST_STMT_IF) stmt_list.head;
//                for()
//            }
//            else if(stmt_list.head instanceof AST_STMT_WHILE) {
//                AST_STMT_WHILE while_stmt = (AST_STMT_WHILE) stmt_list.head;
//            }
//            else {
//                continue;
//            }
//        }
//
//        return true;
//    }

}
