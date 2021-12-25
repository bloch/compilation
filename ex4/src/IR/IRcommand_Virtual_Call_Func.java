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

public class IRcommand_Virtual_Call_Func extends IRcommand
{
    TEMP dst;
    TEMP t1;
    string id_name;
    TEMP t2;
    TEMP_LIST lst;

    public IRcommand_Virtual_Call_Func(TEMP dst , TEMP t1, string id_name, TEMP t2, TEMP_LIST lst)
    {
        this.dst = dst;
        this.t1 = t1;
        this.id_name = id_name;
        this.t2 = t2;
        this.lst = lst;  //TODO: decide how to handle this
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().virtual_call(dst, t1, id_name, t2, lst); //TODO: add implementation of virtual_call at mipsgenerator
    }
}
