package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import MIPS.*;
import IR.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	int offset;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name, int lineNumber)
	{
		this.lineNumber = lineNumber;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.offset = -300000000;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}

	public TYPE SemantMe() {
		TYPE t = SYMBOL_TABLE.getInstance().findNotInGlobalScope(name);//need to be change to functionscope
		if (t != null) {
			this.offset = SYMBOL_TABLE.getInstance().find_offset(name);
			System.out.println("Name " + name + ", offset: " + this.offset);
			return t;
		}
		t = isVarInClassFields(name);
		if (t!=null){
			System.out.println("print here 1 ############");
			TYPE_ID t_id = (TYPE_ID) t;
			if (t_id.type != null) {
				System.out.println("print here 2 ############");
				// TODO: fix offset calculation for that fucking case(we must considerate the object before it created)
				// DEBUG - class field inside derived method together
//				this.offset = t_id.class_offset; // that's not correct
				//first need to set : lw $t, 8($fp)  ...... "this" is located in 8($fp)
				//after we need to set: lw $t, t_id.class_offset($t)
				this.offset = 30000000 + t_id.class_offset; // new flage for this
				System.out.println("Name " + name + ", offset: " + this.offset);
				return t_id.type;
			}
		}

		t = SYMBOL_TABLE.getInstance().find(name);
		if (t != null) {
			this.offset = SYMBOL_TABLE.getInstance().find_offset(name);
			System.out.println("Name " + name + ", offset: " + this.offset);
		}
		return t;		//might return null... and will fall outside
	}

	public TEMP IRme()
	{
		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Load(t,name, this.offset));
		return t;
	}
}
