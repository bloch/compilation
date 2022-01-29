package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp, int lineNumber)
	{
		this.lineNumber = lineNumber;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
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

		if (exp != null) t2 = exp.SemantMe();

		if (!isT1SubInstanceT2(t2, t1)) {
			AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
			AST_Node.file_writer.close();
			System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n",6,6);
			System.exit(0);
		}
		return null;
	}


	public TEMP IRme(){
//		TEMP src = exp.IRme();
		if (var instanceof AST_VAR_SIMPLE){
			AST_VAR_SIMPLE var_simple = (AST_VAR_SIMPLE) var;
			IR.getInstance().Add_IRcommand(new IRcommand_Store(var_simple.name , exp.IRme(), var_simple.offset));
		}
		else if (var instanceof AST_VAR_SUBSCRIPT){
			AST_VAR_SUBSCRIPT var_sub = (AST_VAR_SUBSCRIPT) var;
			TEMP arrReg = var_sub.var.IRme();
			TEMP entryReg = var_sub.subscript.IRme();
			IR.getInstance().Add_IRcommand(new IRcommand_Array_Set(arrReg , entryReg , exp.IRme()));

		}
		else if (var instanceof AST_VAR_FIELD){
			AST_VAR_FIELD var_field = (AST_VAR_FIELD) var;
			TEMP object = var_field.var.IRme();
			String field_name = var_field.fieldName;
			int class_offset = var_field.class_offset;
			IR.getInstance().Add_IRcommand(new IRcommand_Field_Set(object,field_name,exp.IRme(), class_offset));

		}
		return null;
	}

}
