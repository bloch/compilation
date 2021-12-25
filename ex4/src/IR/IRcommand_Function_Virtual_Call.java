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

    public IRcommand_Function_Virtual_Call(TEMP object, TEMP dst, String function_name, TEMP_LIST params) {
        this.object = object;
        this.dst = dst;
        this.function_name = function_name;
        this.params = params;
    }

}
