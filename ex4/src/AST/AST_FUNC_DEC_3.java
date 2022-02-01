package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_FUNC_DEC_3 extends AST_FUNC_DEC {
    public AST_TYPE_WITH_ID type_with_id1;
    public AST_TYPE_WITH_ID type_with_id2;
    public AST_PSIK_TYPE_ID_LIST ptil;
    public AST_STMT_LIST stmtList;

    public AST_FUNC_DEC_3(AST_TYPE type1, String id_name1, AST_TYPE type2, String id_name2, AST_PSIK_TYPE_ID_LIST ptil, AST_STMT_LIST stmtList , int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.print("====================== funcDec -> type  ID (type ID psikTypeIdList) { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type_with_id1 = new AST_TYPE_WITH_ID(type1, id_name1, this.lineNumber);
        this.type_with_id2 = new AST_TYPE_WITH_ID(type2, id_name2, this.lineNumber);
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

        /**************************************/
        /* [0] Check That Function Name does NOT exist */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().findInLastScope(this.type_with_id1.id_name) != null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_3: function name %s already exists in scope\n",type_with_id1.id_name);
            System.exit(0);
        }

        TYPE_FUNCTION function_signature = (TYPE_FUNCTION) this.GetSignature();

        symbol_table.enter(this.type_with_id1.id_name, function_signature);

        symbol_table.beginScope();

        AST_Node.local_offset = -44;
        AST_Node.param_offset = 8;

        if(AST_Node.cur_class != null) {
            // if cur_class != null, this is method, thus recieving 'this' at param offset 8
            AST_Node.param_offset += 4;
        }

        TYPE_LIST type_list = BuildTypeList(this.ptil);

        // here we know that scope only opned, thus type_with_id2.id_name doesn't exist and type is valid(checked in GetSignature() line 79)
        //symbol_table.enter(type_with_id2.id_name,   function_signature.params.head);
        symbol_table.enter(type_with_id2.id_name,   function_signature.params.head, AST_Node.param_offset);
        AST_Node.param_offset += 4;

        AST_PSIK_TYPE_ID_LIST tmp_ptil = this.ptil;
        TYPE_LIST tmp_type_list = type_list;
        while(tmp_ptil != null) {
            if (SYMBOL_TABLE.getInstance().findInLastScope(tmp_ptil.type_with_id.id_name) != null) {
                AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                AST_Node.file_writer.close();
                System.out.format(">> ERROR AST_FUNC_DEC_3: some parameters have the same name");
                System.exit(0);
            }
            //symbol_table.enter(tmp_ptil.type_with_id.id_name, tmp_type_list.head);
            symbol_table.enter(tmp_ptil.type_with_id.id_name, tmp_type_list.head, AST_Node.param_offset);
            AST_Node.param_offset += 4;

            tmp_ptil = tmp_ptil.tail;
            tmp_type_list = tmp_type_list.tail;
        }

        AST_Node.retTypesList = new TYPE_LIST(null, null);
        AST_Node.retStmtList = new AST_STMT_LIST(null, null, -1);

        this.stmtList.SemantMe();

        if(!CheckReturnTypes(function_signature.returnType)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_3 : Return Type exception");
            System.exit(0);
        }

        AST_Node.retTypesList = null;
        AST_Node.retStmtList = null;

        symbol_table.endScope();
        this.calc_var_decs(this.stmtList);
        return null;
    }

    public TYPE GetSignature() {

        TYPE return_type = GetSignature(type_with_id1.t);
        if(return_type == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_3 : Return Type doesn't exist");
            System.exit(0);
        }

        TYPE arg1_type = GetSignature(type_with_id2.t);
        if(arg1_type == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_3 : first parameter type doesn't exist");
            System.exit(0);
        }

        if (arg1_type instanceof TYPE_VOID) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_3: first parameter type can't be void");
            System.exit(0);
        }

        TYPE_LIST type_list = BuildTypeList(this.ptil);

        return new TYPE_FUNCTION(return_type, this.type_with_id1.id_name, new TYPE_LIST(arg1_type, type_list));
    }

    public TYPE_LIST BuildTypeList(AST_PSIK_TYPE_ID_LIST ptil) {
        TYPE head_type = GetSignature(ptil.type_with_id.t);
        if(head_type == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_3 : some parameter(second or higher) type doesn't exist");
            System.exit(0);
        }
        if (head_type instanceof TYPE_VOID) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR AST_FUNC_DEC_3: some parameter(second or higher) type can't be void");
            System.exit(0);
        }
        if(ptil.tail == null) {
            return new TYPE_LIST(head_type, null);
        }
        else {
            return new TYPE_LIST(head_type, BuildTypeList(ptil.tail));
        }
    }

    public TEMP IRme()
    {
        String function_label;
        if(AST_Node.cur_class == null) {
            if (type_with_id1.id_name.equals("main")) {
                function_label = "user_main";
            } else {
                function_label = type_with_id1.id_name + "_function";
            }
        }
        else {
            if (type_with_id1.id_name.equals("main")) {
                function_label = AST_Node.cur_class.name + "_" + "user_main";
            } else {
                function_label = AST_Node.cur_class.name + "_" + type_with_id1.id_name;
            }
        }

        IR.getInstance().Add_IRcommand(new IRcommand_Label(function_label));
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Prologue(function_label, Math.max(16, 4*this.num_locals)));

        AST_Node.cur_function_label = function_label;
        if (stmtList != null) stmtList.IRme();
        AST_Node.cur_function_label = null;

        IR.getInstance().Add_IRcommand(new IRcommand_Function_Epilogue(function_label));

        return null;
    }

}
