package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript, int lineNumber)
	{
		this.lineNumber = lineNumber;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}

	public TYPE SemantMe()
	{
		TYPE t = null;
		TYPE_ARRAY ta = null;

		/******************************/
		/* [1] Recursively semant var */
		/******************************/
		if (var != null) t = var.SemantMe();

		/*********************************/
		/* [2] Make sure type is a array */
		/*********************************/
		if (t == null) {
			AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
			AST_Node.file_writer.close();
			System.out.format(">> ERROR AST_VAR_SUBSCRIPT: var doesn't exist\n",t.name);
			System.exit(0);
		}
		else if (t.isArray() == false) {
			AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
			AST_Node.file_writer.close();
			System.out.format(">> ERROR AST_VAR_SUBSCRIPT: access %s field of a non-array variable\n",t.name);
			System.exit(0);
		}
		else {
			ta = (TYPE_ARRAY) t;
		}

		if (this.subscript.SemantMe() != TYPE_INT.getInstance()) {
			AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
			AST_Node.file_writer.close();
			System.out.format(">> ERROR AST_VAR_SUBSCRIPT: expression inside BRACKETS is not integral\n");
			System.exit(0);
		}

		return ta.type;

	}

	public TEMP IRme(){
		TEMP t1 = var.IRme();
		TEMP t2 = subscript.IRme();
		TEMP t3 = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Array_Access(t1,t2,t3));
		return t3;
	}

}
