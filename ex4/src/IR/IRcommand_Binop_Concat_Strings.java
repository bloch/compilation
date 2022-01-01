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

public class IRcommand_Binop_Concat_Strings extends IRcommand
{
    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop_Concat_Strings(TEMP dst,TEMP t1,TEMP t2)
    {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        TEMP tmp = TEMP_FACTORY.getInstance().getFreshTEMP();
        String label_string1  = getFreshLabel("string1");
        String label_string2  = getFreshLabel("string2");
        String label_end  = getFreshLabel("end");
        MIPSGenerator.getInstance().concat_strings(dst,t1,t2,tmp, label_string1, label_string2, label_end);
    }
}
