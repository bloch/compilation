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
	public AST_STMT_LIST(AST_STMT head,AST_STMT_LIST tail)
	{
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

	public static TYPE retExpCheck(AST_STMT s, TYPE ret_type) {
		TYPE res = null;
		if (s instanceof AST_STMT_RETURN_EXP){
			AST_STMT_RETURN_EXP a1 = (AST_STMT_RETURN_EXP) s;
			res = a1.SemantRet(ret_type); //check
		}
		else if (s instanceof AST_STMT_WHILE) {
			AST_STMT_WHILE a2 = (AST_STMT_WHILE) s;
			res = a2.SemantRet(ret_type); //check
		}
		else if (s instanceof AST_STMT_IF) {
			AST_STMT_IF a3 = (AST_STMT_IF) s;
			res = a3.SemantRet(ret_type); //check
		}
		else if (s instanceof AST_STMT_RETURN) {
			System.out.println(">> ERROR STMT_RETURN: return_type of function isn't void but the function return nothing");
			System.exit(0);
			return null;
		}
		return res;
	}

	public static TYPE retVoidCheck(AST_STMT s, TYPE ret_type) {
		if (s instanceof AST_STMT_RETURN_EXP){
			System.out.println(">> ERROR STMT_RETURN: return_type of function is void but the function return something");
			System.exit(0);
			return null;
		}
		else if (s instanceof AST_STMT_WHILE) {
			AST_STMT_WHILE a2 = (AST_STMT_WHILE) s;
			a2.SemantRet(ret_type); //check
			return null;
		}
		else if (s instanceof AST_STMT_IF) {
			AST_STMT_IF a3 = (AST_STMT_IF) s;
			a3.SemantRet(ret_type); //check
			return null;
		}
		return null;
	}

	public TYPE SemantRet(TYPE ret_type) {
		TYPE res = null;
		boolean flag = false;
		if (!(ret_type instanceof TYPE_VOID)) {
			res = retExpCheck(this.head, ret_type);
			if (res != null) {flag = true;}

			AST_STMT_LIST tmp = this.tail;
			while (tmp != null) {
				retExpCheck(tmp.head, ret_type);
				if (res != null) {flag = true;}
				tmp = tmp.tail;
			}
			if (!flag){
				System.out.println(">> ERROR STMT_LIST: function doesn't have return statement but return type isn't void");
				System.exit(0);
				return null;
			}
			return res;
		} else {
			retVoidCheck(this.head, ret_type);

			AST_STMT_LIST tmp = this.tail;
			while (tmp != null) {
				retVoidCheck(tmp.head, ret_type);
				tmp = tmp.tail;
			}
			return null;
		}
	}

}
