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

public class IRcommand_Function_Virtual_Call extends IRcommand
{
    public TEMP object;
    public TEMP dst;
    public String function_name;
    public TEMP_LIST params;
    public int offset;

    public IRcommand_Function_Virtual_Call(TEMP object, TEMP dst, String function_name, TEMP_LIST params, int offset) {
        this.object = object;
        this.dst = dst;
        this.function_name = function_name;
        this.params = params;
        this.offset = offset;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().virtual_call(this.object, this.offset, this.params, this.dst);
    }

}
