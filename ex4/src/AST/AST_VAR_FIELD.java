package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_FIELD(AST_VAR var,String fieldName, int lineNumber)
	{
		this.lineNumber = lineNumber;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();
		System.out.format("FIELD NAME( %s )\n",fieldName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n...->%s",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	public TYPE SemantMe()
	{
		TYPE t = null;
		TYPE_CLASS tc = null;

		/******************************/
		/* [1] Recursively semant var */
		/******************************/
		if (var != null) t = var.SemantMe();

		//type of var isn't declered in the symbol table
		if (t == null){
			AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
			AST_Node.file_writer.close();
			System.out.format(">> ERROR var doesn't exist (AST_VAR_FIELD)\n");
			System.exit(0);
		}
		/*********************************/
		/* [2] Make sure type is a class */
		/*********************************/
		if (t.isClass() == false) {
			AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
			AST_Node.file_writer.close();
			System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",6,6,fieldName);
			System.exit(0);
		}
		else {
			tc = (TYPE_CLASS) t;
		}

		/************************************/
		/* [3] Look for fiedlName inside class&super_classes fields names */
		/************************************/
		for (TYPE_CLASS tmp_class = tc ; tmp_class !=null ; tmp_class = tmp_class.father) {
			for (TYPE_LIST it=tmp_class.data_members ; it != null ; it=it.tail)
			{
				if (it.head.name.equals(fieldName)) {
					TYPE_ID class_member = (TYPE_ID) it.head;
					if (class_member.type.isFunction()){
						AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
						AST_Node.file_writer.close();
						System.out.format(">> ERROR : expected var and recieved function in AST_VAR_FIELD)");
						System.exit(0);
					}
					else{
						return class_member.type;
					}

				}
			}
		}

		/*********************************************/
		/* [4] fieldName does not exist in class var */
		/*********************************************/
		AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
		AST_Node.file_writer.close();
		System.out.format(">> ERROR [%d:%d] field %s does not exist in class\n",6,6,fieldName);
		System.exit(0);
		return null;
	}

	public TEMP IRme()
	{
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP object = this.var.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Field_Access(dst, object, this.fieldName));
		return dst;
	}

}
