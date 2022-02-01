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
		if (this.offset > 30000000) {
			offset = offset - 30000000;
			String offset_string1 = "8($fp)";
			MIPSGenerator.getInstance().load_from_saved_register(offset_string1);

			int idxdst = src.getSerialNumber();
			String offset_string2 = offset + "($s0)";
			MIPSGenerator.getInstance().store(offset_string2, src);
		}

		else if(offset != -300000000) {
			String offset_string = this.offset + "($fp)";
			MIPSGenerator.getInstance().store(offset_string, src);
		}
		else {
			MIPSGenerator.getInstance().store(var_name + "_global_var", src);
		}
	}
}
