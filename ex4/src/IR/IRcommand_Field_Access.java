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

public class IRcommand_Field_Access extends IRcommand
{
    public TEMP dst;
    public TEMP t;
    String fieldName;

    public IRcommand_Field_Access(TEMP dst, TEMP t, String fieldName)
    {
        this.dst = dst;
        this.t = t;
        this.fieldName = fieldName;
    }
    /***************/
    /* MIPS me !!! */
    /***************/
//	public void MIPSme()
//	{
//		MIPSGenerator.getInstance().add(dst,t1,t2);
//	}
}
