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
import java.util.ArrayList;

public class IRcommand_Class_Dec extends IRcommand
{
    String class_name;
    ArrayList<String> function_labels;

    public IRcommand_Class_Dec(String class_name, ArrayList<String> function_labels)
    {
        this.class_name = class_name;
        this.function_labels = function_labels;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().class_dec(this.class_name, this.function_labels);
    }
}
