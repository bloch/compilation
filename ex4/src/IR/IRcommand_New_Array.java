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

public class IRcommand_New_Array extends IRcommand
{
    TEMP dst;
    TEMP src;

    public IRcommand_New_Array(TEMP dst , TEMP src)
    {
        this.dst = dst;
        this.src = src;
    }

    public void MIPSme() {
        MIPSGenerator.getInstance().new_array(dst, src);
    }
}
