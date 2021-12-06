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


	// parameters : t1 , t2 are both TYPE_CLASS (check before using this func)
	// t1 is the function parameter , and t2 is the class field we search for
	// return true iff t1 is subclass of t2
	// tmp objects for grasp?
	public boolean isT1SubInstanceT2_helper(TYPE_CLASS t1 , TYPE_CLASS t2){
		if (t1.name.equals(t2.name)){//type ar equal
			return true;
		}
		if (t2.father == null){
			return false;
		}
		return isT1SubInstanceT2(t1 , t2.father);
	}

	public boolean isT1SubInstanceT2(TYPE t1 , TYPE t2){
		//primitive type check , is nill primtive type also by grammer?
		if (t1 instanceof TYPE_INT){
			if (t2 instanceof TYPE_INT){
				return true;
			}
			return false;
		}
		if (t1 instanceof TYPE_STRING){
			if (t2 instanceof TYPE_STRING){
				return true;
			}
			return false;
		}
		if (t1 instanceof TYPE_VOID){ // TODO : check if needed
			if (t2 instanceof TYPE_VOID){
				return true;
			}
			return false;
		}
		if (t1 instanceof TYPE_NIL){
			if (t2 instanceof TYPE_ID){
				TYPE_ID tmp_t2 = (TYPE_ID) t2;
				if (tmp_t2.type.isClass() || tmp2_t2.type.isArray()){ //TODO : what about isFunction?
					return true;
				}
				return false;
			}
		}
		if (t1 instanceof TYPE_ARRAY && t2 instanceof TYPE_ARRAY){
			if (t1.name.equals(t2.name)){ // noninterchangable (page 4-5 in pdf)
				return true;
			}
			return false;
		}
		if (t1 instanceof TYPE_CLASS){
			if (t2 instanceof TYPE_CLASS){
				return isT1SubInstanceT2_helper((TYPE_CLASS) t1, (TYPE_CLASS) t2);
			}
			return false;
		}
		System.out.println("reach non reachable code in isT1SubInstanceT2 in AST_NODE");
		return false;
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
