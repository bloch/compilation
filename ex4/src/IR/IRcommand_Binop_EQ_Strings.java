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

public class IRcommand_Binop_EQ_Strings extends IRcommand
{
	public TEMP t1;
	public TEMP t2;
	public TEMP dst;

	public IRcommand_Binop_EQ_Strings(TEMP dst,TEMP t1,TEMP t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
	    String neq_label  = getFreshLabel("neq_label");
        String str_eq_end = getFreshLabel("str_eq_end");
        String str_eq_loop = getFreshLabel("str_eq_loop");
        MIPSGenerator.getInstance().str_eq(dst, t1, t2, neq_label, str_eq_end, str_eq_loop);
	}
}
