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
	public String j_label;

	public IRcommand_Return(TEMP t, String j_label)
	{
		this.t = t;
		this.j_label = j_label;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().return_command(t, j_label);
	}
}
