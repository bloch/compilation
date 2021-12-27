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

public class IRcommand_Allocate_String extends IRcommand_Allocate
{
    String value;
    boolean global;

    public IRcommand_Allocate_String(String var_name, String value, boolean global)
    {
        this.var_name = var_name;
        this.value = value;
        this.global = global;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().allocate_string(var_name, this.value, this.global);
    }
}
