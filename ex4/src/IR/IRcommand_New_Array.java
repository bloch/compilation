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
    String array_name;
    TEMP dst;
    TEMP src;

    public IRcommand_New_Array(TEMP dst , String array_name , TEMP src)
    {
        this.dst = dst;
        this.array_name = array_name;
        this.src = src;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {};
}
