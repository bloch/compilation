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

public class IRcommand_New_Class extends IRcommand
{
    String class_name;
    TEMP dst;

    public IRcommand_New_Class(TEMP dst , String class_name)
    {
        this.class_name = class_name;
        this.dst = dst;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {};
}
