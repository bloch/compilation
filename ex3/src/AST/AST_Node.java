package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
	
	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}

	public TYPE SemantMe() { System.out.println("NO SEMANT-ME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n"); return null;}

	public TYPE GetSignature() {
		System.out.println("NO GetSignature !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n"); return null;
	}

	public TYPE_LIST GetSignatures() {
		System.out.println("NO GetSignatures(AST_NODE) !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n"); return null;
	}
}
