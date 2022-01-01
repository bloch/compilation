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

public class IRcommandContString extends IRcommand
{
    TEMP t;
    String value;

    public IRcommandContString(TEMP t,String value)
    {
        this.t = t;
        this.value = value;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().allocate_and_store_const_string(t,value);
    }
}
