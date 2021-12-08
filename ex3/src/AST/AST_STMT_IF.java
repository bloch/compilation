package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_STMT_IF extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(AST_EXP cond,AST_STMT_LIST body)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== stmt -> if (exp) { stmtList }\n");

		/*******************************/
		/* COPY INPUT DATA EXP ... */
		/*******************************/
		this.cond = cond;
		this.body = body;
	}


	/**************************************************/
	/* The printing message for a STMT RETURN AST node */
	/**************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE STMT_IF\n");

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("IF STMT\n"));

		/***********************************/
		/* RECURSIVELY PRINT ... */
		/***********************************/
		if (cond != null) cond.PrintMe();

		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);

		if (body != null) body.PrintMe(SerialNumber);

	}

	public TYPE SemantMe()
	{
		/****************************/
		/* [0] Semant the Condition */
		/****************************/
		if (cond.SemantMe() != TYPE_INT.getInstance()) {
			System.out.format(">> ERROR [%d:%d] condition inside IF is not integral\n",2,2);
		}

		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope();

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		body.SemantMe();

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}

	public TYPE SemantRet(TYPE ret_type){
		body.SemantRet(ret_type);
		return null;
	}

}