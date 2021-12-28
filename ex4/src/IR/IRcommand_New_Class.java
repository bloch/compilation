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
import TYPES.*;
import AST.*;
import java.util.*;

public class IRcommand_New_Class extends IRcommand
{
    TYPE_ID[] fields_lst;
    TEMP dst;
    int size_of_class;
    String class_name;

    public IRcommand_New_Class(TEMP dst , TYPE_ID[] fields_lst , int size_of_class , String class_name)
    {
        this.fields_lst = fields_lst;
        this.dst = dst;
        this.size_of_class = size_of_class;
        this.class_name = class_name;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {MIPSGenerator.getInstance().new_class(dst , fields_lst , size_of_class , this.class_name);}
}