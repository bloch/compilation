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

public class IRcommand_Field_Set extends IRcommand
{
    public TEMP o;
    public String f;
    public TEMP e;

    public IRcommand_Field_Set(TEMP o , String f , TEMP e)
    {
        this.o = o;
        this.f = f;
        this.e = e;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {

    }
}
