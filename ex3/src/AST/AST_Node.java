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

	public TYPE GetSignature(AST_TYPE t) {
		TYPE return_type = null;
		if (t instanceof AST_TYPE_INT) {
			return_type = TYPE_INT.getInstance();
			return return_type;
		}
		if (t instanceof AST_TYPE_STRING) {
			return_type = TYPE_STRING.getInstance();
			return return_type;
		}
		if (t instanceof AST_TYPE_VOID) {
			return_type = TYPE_VOID.getInstance();
			return return_type;
		}
		if (t instanceof AST_TYPE_ID) {
			AST_TYPE_ID type_id = (AST_TYPE_ID) t;
			return_type = SYMBOL_TABLE.getInstance().find(type_id.name);
			return return_type;
			//here if the type isn't exist then return_type = null , therefore in each time
			// we loop over GetSignatures list we need to check null
			// see in isSignaturesValid func in AST_CFIELD_LIST that check correctness

		}
		System.out.format(">> ERROR : unkown AST_TYPE (error in GetSignature func in AST_NODE)\n");
		return return_type;
	}

}
