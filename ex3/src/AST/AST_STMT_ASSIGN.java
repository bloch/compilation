package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

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
	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp)
	{
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
			System.out.format(">> ERROR [%d:%d] illegal access of var\n",6,6);
			System.exit(0);
		}

		if (exp != null) t2 = exp.SemantMe();
		System.out.format("%s , %s", t1.name, t2.name);
		if (!isT1SubInstanceT2(t2, t1)) {
			System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n",6,6);
			System.exit(0);
		}
		return null;
	}
	public TYPE SemantMeClass(TYPE_CLASS type_class) {
		TYPE t1 = null;
		TYPE t2 = null;

		if (var != null) t1 = var.SemantMe(); //TODO : check if in cuerrent scope or (global scope ?)
		if (t1 == null) { // TODO : need to be change --> var could be field of current class super class that assgined again inside function
			//TODO : nathan implement recursiveGetNameOfVar .....
			TYPE var_type = isVarFieldOfClass(name_of_var!!!!!!!!! , type_class);
			if (var_type == null){
				System.out.format(">> ERROR : var isn't apper inside global/class/superClasses scopes!!!!!\n");
				System.exit(0);
			}
			t1 = var_type;
//			System.out.format(">> ERROR [%d:%d] illegal access of var\n",6,6);
//			System.exit(0);
		}

		if (exp != null) t2 = exp.SemantMe();
		if (t2 == null){ // t2 isn't apper inside symbol table !!!!!!!!!!HERE!!!!!!!!!!!
			TYPE exp_type = isVarFieldOfClass(name_of_exp!!!!!!!!!! , type_class);
			if (exp_type == null){
				System.out.format(">> ERROR : exp isn't apper inside global/class/superClasses scopes!!!!!\n");
				System.exit(0);
			}
			t2 = exp_type;
		}
		System.out.format("%s , %s", t1.name, t2.name);
		if (!isT1SubInstanceT2(t2, t1)) {
			System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n",6,6);
			System.exit(0);
		}
		return null;
	}

}
