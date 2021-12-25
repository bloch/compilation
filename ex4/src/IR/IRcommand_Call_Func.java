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

public class IRcommand_Call_Func extends IRcommand
{
    TEMP dst;
    string id_name;
    TEMP t;
    TEMP_LIST lst;

    public IRcommand_Call_Func(TEMP dst, string id_name, TEMP t, TEMP_LIST lst)
    {
        this.dst = dst;
        this.id_name = id_name;
        this.t = t;
        this.lst = lst;  //TODO: decide how to handle this
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().call(dst, id_name, t, lst); //TODO: add implementation of call at mipsgenerator
    }
}
