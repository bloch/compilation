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

public class IRcommand_Return extends IRcommand
{
	public TEMP t;

	public IRcommand_Return(TEMP t)
	{
		this.t = t;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if(t != null) {
			MIPSGenerator.getInstance().return_command(t);
		}

	}
}
