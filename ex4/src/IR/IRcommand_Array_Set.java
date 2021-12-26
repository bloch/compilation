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

public class IRcommand_Array_Set extends IRcommand
{
    public TEMP t1;
    public TEMP t2;
    public TEMP t3;

    public IRcommand_Array_Set(TEMP t1 , TEMP t2 , TEMP t3)
    {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {

    }
}
