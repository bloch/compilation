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

public class IRcommand_Function_Epilogue extends IRcommand
{
    public String func_name;

    public IRcommand_Function_Epilogue(String func_name) {
        this.func_name = func_name;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().function_epilogue(this.func_name);
    }

}
