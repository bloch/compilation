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

public class IRcommand_Function_Prologue extends IRcommand
{
    public String func_name;
    public int sp_offset;

    public IRcommand_Function_Prologue(String func_name, int sp_offset) {
        this.func_name = func_name;
        this.sp_offset = sp_offset;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().function_prologue(this.func_name, this.sp_offset);
    }

}
