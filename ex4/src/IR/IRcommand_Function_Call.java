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

public class IRcommand_Function_Call extends IRcommand
{
    public TEMP dst;
    public String function_name;
    public TEMP_LIST params;

    public IRcommand_Function_Call(TEMP dst, String function_name, TEMP_LIST params) {
        this.dst = dst;
        this.function_name = function_name;
        this.params = params;
    }

    public void MIPSme()
    {
        if (this.function_name.equals("user_main")){
            MIPSGenerator.getInstance().call(this.function_name, this.params, this.dst);
        }
        else{
            MIPSGenerator.getInstance().call(this.function_name + "_function", this.params, this.dst);
        }

    }

}
