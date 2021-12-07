package AST;

public abstract class AST_STMT extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}

	public TYPE isVarFieldOfClass(String var_name , TYPE_CLASS type_class){
		for (TYPE_CLASS tmp_class = type_class ; tmp_class !=null ; tmp_class = tmp_class.father) {
			for (TYPE_LIST it=tmp_class.data_members ; it != null ; it=it.tail)
			{
				if (it.head.name.equals(var_name)) { //var is not function ( grammer : func_dec_1 --> stmtlist --> smst --> stmt_assign )
					TYPE_ID class_member = (TYPE_ID) it.head;
					if (class_member.type.isFunction()){
						System.out.format(">> ERROR : expected var and recieved function in AST_STMT)");
						return null;
					}
					else{
						return class_member.type;
					}
				}
			}
		}
		return false; //var_name does not exist inside class/superClasses simple fields.
	}
}
