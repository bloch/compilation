package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import java.util.ArrayList;

public class AST_STMT_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_STMT head;
	public AST_STMT_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_LIST(AST_STMT head,AST_STMT_LIST tail, int lineNumber)
	{
		this.lineNumber = lineNumber;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null) System.out.print("====================== stmts -> stmt stmts\n");
		if (tail == null) System.out.print("====================== stmts -> stmt      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe(int SerialNumber)
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/

		ArrayList<AST_STMT> lst = new ArrayList<AST_STMT>();
		lst.add(this.head);
		AST_STMT_LIST tmp = this.tail;
		while(tmp != null) {
			lst.add(tmp.head);
			tmp = tmp.tail;
		}

		for (int counter = 0; counter < lst.size(); counter++) {
			lst.get(counter).PrintMe();
		}

		for (int counter = 0; counter < lst.size(); counter++) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,lst.get(counter).SerialNumber);
		}
	}

	public TYPE SemantMe() {
		this.head.SemantMe();

		AST_STMT_LIST tmp = this.tail;
		while(tmp != null) {
			tmp.head.SemantMe();
			tmp = tmp.tail;
		}
		return null;
	}

	public void AddToStmtList(AST_STMT stmt) {
		AST_STMT_LIST tmp = this;
		if (tmp.head == null) {
			tmp.head = stmt;
			return;
		}
		while(tmp.tail != null) {
			//System.out.format("%s\t", tmp.head.name);
			tmp = tmp.tail;
		}

		tmp.tail = new AST_STMT_LIST(stmt, null, stmt.lineNumber);
	}

}
