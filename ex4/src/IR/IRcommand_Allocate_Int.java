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

public class IRcommand_Allocate_Int extends IRcommand_Allocate
{
    int value;

    public IRcommand_Allocate_Int(String var_name, int value)
    {
        this.var_name = var_name;
        this.value = value;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().allocate_int(var_name, this.value);
    }
}
