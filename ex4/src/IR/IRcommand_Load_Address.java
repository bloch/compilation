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

public class IRcommand_Load_Address extends IRcommand
{
    String label_name;
    int offset;

    public IRcommand_Load_Address(String label_name, int offset)
    {
        this.label_name = label_name;
        this.offset = offset;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        String offset_string = this.offset + "($sp)";
        MIPSGenerator.getInstance().la(offset_string, this.label_name);
    }
}
