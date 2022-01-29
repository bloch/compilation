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

public class IRcommand_Binop_Divide_Integers extends IRcommand
{
    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop_Divide_Integers(TEMP dst,TEMP t1,TEMP t2)
    {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }

    public void MIPSme()
    {
        String label_end_max  = getFreshLabel("endMax");
        String label_end_min  = getFreshLabel("endMin");
        MIPSGenerator.getInstance().div(dst,t1,t2, label_end_max, label_end_min);
    }
}
