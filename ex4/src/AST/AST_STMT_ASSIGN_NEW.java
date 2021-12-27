package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_STMT_ASSIGN_NEW extends AST_STMT
{
    public AST_VAR var;
    public AST_NEW_EXP ne;

    /*******************/
    /*  CONSTRUCTOR(S) */
    /*******************/
    public AST_STMT_ASSIGN_NEW(AST_VAR var,AST_NEW_EXP ne, int lineNumber)
    {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> var ASSIGN newExp SEMICOLON\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
        this.ne = ne;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE NEW ASSIGN STMT\n");

        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (var != null) var.PrintMe();
        if (ne != null) ne.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "NEW ASSIGN\nleft := right\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,ne.SerialNumber);
    }

    public TYPE SemantMe() {
        TYPE t1 = null;
        TYPE t2 = null;

        if (var != null) t1 = var.SemantMe();
        if (t1 == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR [%d:%d] illegal access of var\n",6,6);
            System.exit(0);
        }

        if (ne != null) t2 = ne.SemantMe();

        if (ne instanceof AST_NEW_EXP_2) { // type should be array
            if (!t1.isArray()) {
                AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                AST_Node.file_writer.close();
                System.out.println(">> ERROR AST_STMT_ASSIGN_NEW: t1 should be array");
                System.exit(0);
                return null;
            }
            TYPE_ARRAY type_array = (TYPE_ARRAY) t1;
            t1 = type_array.type;
        }

        if (!isT1SubInstanceT2(t2, t1)) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR [%d:%d] type mismatch for var := newExp\n",6,6);
            System.exit(0);
        }
        return null;
    }
    public TEMP IRme(){
        TEMP src = ne.IRme();
        if (var instanceof AST_VAR_SIMPLE){
            AST_VAR_SIMPLE var_simple = (AST_VAR_SIMPLE) var;
            IR.getInstance().Add_IRcommand(new IRcommand_Store(var_simple.name , src, var_simple.offset));
        }
        else if (var instanceof AST_VAR_SUBSCRIPT){
            AST_VAR_SUBSCRIPT var_sub = (AST_VAR_SUBSCRIPT) var;
            TEMP arrReg = var_sub.var.IRme();
            TEMP entryReg = var_sub.subscript.IRme();
            IR.getInstance().Add_IRcommand(new IRcommand_Array_Set(arrReg , entryReg , src));

        }
        else if (var instanceof AST_VAR_FIELD){
            AST_VAR_FIELD var_field = (AST_VAR_FIELD) var;
            TEMP object = var_field.var.IRme();
            String field_name = var_field.fieldName;
            int class_offset = var_field.class_offset;
            IR.getInstance().Add_IRcommand(new IRcommand_Field_Set(object,field_name,src, class_offset));

        }
        return null;
    }

}
