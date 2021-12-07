package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name)
	{
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
		return SYMBOL_TABLE.getInstance().find(name);
	}

	public TYPE SemantMeClass(TYPE_CLASS type_class)
	{
		for (TYPE_CLASS tmp_class = type_class ; tmp_class !=null ; tmp_class = tmp_class.father) {
			for (TYPE_LIST it=tmp_class.data_members ; it != null ; it=it.tail)
			{
				if (it.head.name.equals(var_name)) { //var is not function ( grammer : func_dec_1 --> stmtlist --> smst --> smts_assign )
					TYPE_ID class_member = (TYPE_ID) it.head;
					if (class_member.type.isFunction()){
						System.out.format(">> ERROR : expected var and recieved function in AST_STMT)");
						return false;
					}
					else{
						return true;
					}
				}
			}
		}
		return false; //var_name does not exist inside class/superClasses simple fields.
	}

	public String getNameOfVar(){
		return this.name;
	}
}
