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

public class IRcommand_Load extends IRcommand {
	TEMP dst;
	String var_name;
	int offset;

	public IRcommand_Load(TEMP dst, String var_name, int offset) {
		this.dst = dst;
		this.var_name = var_name;
		this.offset = offset;
	}

	/***************/
	/* MIPS me !!! */

	/***************/
	public void MIPSme() {

		if (this.offset > 30000000) {
			//here we use $s0 saved register in oreder to load "this" object from the stack
			offset = offset - 30000000;
			String offset_string1 = "8($fp)";
			MIPSGenerator.getInstance().load_from_saved_register(offset_string1);

			int idxdst = dst.getSerialNumber();
			String offset_string2 = offset + "($s0)";
			MIPSGenerator.getInstance().load(dst, offset_string2);

		} else if (offset != -300000000) {
			String offset_string = this.offset + "($fp)";
			MIPSGenerator.getInstance().load(dst, offset_string);
		} else {
			MIPSGenerator.getInstance().load(dst, var_name + "_global_var");
		}
	}
}
//		if(offset != -300000000) {
//			String offset_string = this.offset + "($fp)";
//			MIPSGenerator.getInstance().load(dst, offset_string);
//		}
//		else {
//			MIPSGenerator.getInstance().load(dst, var_name);
//		}
