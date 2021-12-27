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

public class IRcommand_Load extends IRcommand
{
	TEMP dst;
	String var_name;
	int offset;
	
	public IRcommand_Load(TEMP dst,String var_name, int offset)
	{
		this.dst      = dst;
		this.var_name = var_name;
		this.offset = offset;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if(offset != -300000000) {
			String offset_string = this.offset + "($fp)";
			MIPSGenerator.getInstance().load(dst, offset_string);
		}
		else {
			MIPSGenerator.getInstance().load(dst, var_name);
		}
	}
}
