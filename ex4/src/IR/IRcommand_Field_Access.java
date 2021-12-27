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

public class IRcommand_Field_Access extends IRcommand
{
    public TEMP dst;
    public TEMP t;
    String fieldName;
    public int offset;

    public IRcommand_Field_Access(TEMP dst, TEMP t, String fieldName, int offset)
    {
        this.dst = dst;
        this.t = t;
        this.fieldName = fieldName;
        this.offset = offset;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().field_access(this.dst, this.offset, this.t);
    }
}
