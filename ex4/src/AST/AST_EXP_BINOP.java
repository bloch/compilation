package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	public AST_EXP left;
	public AST_EXP right;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left,AST_EXP right,int OP , int lineNumber)
	{
		this.lineNumber = lineNumber;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.OP = OP;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		String sOP="";
		
		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		if (OP == 0) {sOP = "+";}
		if (OP == 1) {sOP = "-";}
		if (OP == 2) {sOP = "*";}
		if (OP == 3) {sOP = "/";}
		if (OP == 4) {sOP = "<";}
		if (OP == 5) {sOP = ">";}
		if (OP == 6) {sOP = "=";}
		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP\n(%s)",sOP));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}

	public TYPE SemantMe() {
		TYPE t1 = null;
		TYPE t2 = null;

		if (left != null) t1 = left.SemantMe();
		if (right != null) t2 = right.SemantMe();
		//System.out.format(t1.name);
		if(t1 == null) {
			AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
			AST_Node.file_writer.close();
			System.out.format("\n>> ERROR IN AST_EXP_BINOP: LEFT EXP DOESN'T EXIST(OP=%d)", OP);
			System.exit(0);
		}
		if(t2 == null) {
			AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
			AST_Node.file_writer.close();
			System.out.format("\n>> ERROR IN AST_EXP_BINOP: RIGHT EXP DOESN'T EXIST(OP=%d)", OP);
			System.exit(0);
		}

		if(OP == 6) {			// Equality logic : page 7
			System.out.println("enter equality in binop");
			if (isT1SubInstanceT2(t1, t2) || isT1SubInstanceT2(t2, t1)) {
				return TYPE_INT.getInstance();
			}
			else {
				AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
				AST_Node.file_writer.close();
				System.out.println("\n>> ERROR IN AST_EXP_BINOP: ILLEGAL EQUALITY(=) CHECKING");
				System.exit(0);
				return null;
			}
		}
		else {	// Binary Operations : page 8
			if ((t1 == TYPE_INT.getInstance()) && (t2 == TYPE_INT.getInstance())) {
				if (this.OP == 3 && right instanceof AST_EXP_INT) {
					AST_EXP_INT tmp_r = (AST_EXP_INT) right;
					if (tmp_r.value == 0) {
						AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
						AST_Node.file_writer.close();
						System.out.println("\n>> ERROR IN AST_EXP_BINOP: DIVISION BY ZERO");
						System.exit(0);
						return null;
					}
				}
				return TYPE_INT.getInstance();
			}
			else if ((t1 == TYPE_STRING.getInstance()) && (t2 == TYPE_STRING.getInstance()) && (this.OP==0)) {
				return TYPE_STRING.getInstance();
			}
			else {
				AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
				AST_Node.file_writer.close();
				System.out.format(">> ERROR IN AST_EXP_BINOP: ILLEGAL BINARY OPERATION %d", OP);
				System.exit(0);
				return null;
			}
		}
	}

	public TEMP IRme()
	{
		TEMP t1 = null;
		TEMP t2 = null;
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

		if (left  != null) t1 = left.IRme();
		if (right != null) t2 = right.IRme();

		if (OP == 0) {
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(dst,t1,t2));
		}
		if (OP == 1) {
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_Subtract_Integers(dst,t1,t2));
		}
		if (OP == 2) {
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst,t1,t2));
		}
		if (OP == 3) {
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_Divide_Integers(dst,t1,t2));
		}
		if (OP == 4) {
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t1,t2));
		}
		if (OP == 5) {
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_GT_Integers(dst,t1,t2));
		}
		if (OP == 6) {
			if (left instanceof AST_EXP_STRING && left instanceof AST_EXP_STRING){
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Strings(dst,t1,t2));
			}
			else {
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst, t1, t2));
			}
		}
		return dst;
	}

}
