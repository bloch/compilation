/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;

public class IRcommand_Store extends IRcommand
{
	String var_name;
	TEMP src;
	int offset;
	
	public IRcommand_Store(String var_name,TEMP src, int offset)
	{
		this.src      = src;
		this.var_name = var_name;
		this.offset = offset;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if(offset != -300000000) {
			String offset_string = this.offset + "($sp)";
			MIPSGenerator.getInstance().store(offset_string, src);
		}
		else {
			MIPSGenerator.getInstance().store(var_name, src);
		}
	}
}
