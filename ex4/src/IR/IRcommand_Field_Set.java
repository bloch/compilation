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

public class IRcommand_Field_Set extends IRcommand
{
    public TEMP o;
    public String f;
    public TEMP e;

    int offset;

    public IRcommand_Field_Set(TEMP o , String f , TEMP e, int offset)
    {
        this.o = o;
        this.f = f;
        this.e = e;
        this.offset = offset;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().field_set(this.o, this.offset, this.e);
    }
}
