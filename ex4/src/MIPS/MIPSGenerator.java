/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.ArrayList;

import java.util.*;
import TYPES.*;
import AST.*;
import java.util.*;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

public class MIPSGenerator
{
	private int WORD_SIZE=4;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		//fileWriter.print("\tli $v0,10\n");
		//fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}
	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}
	//public TEMP addressLocalVar(int serialLocalVarNum)
	//{
	//	TEMP t  = TEMP_FACTORY.getInstance().getFreshTEMP();
	//	int idx = t.getSerialNumber();
	//
	//	fileWriter.format("\taddi Temp_%d,$fp,%d\n",idx,-serialLocalVarNum*WORD_SIZE);
	//	
	//	return t;
	//}
	public void allocate(String var_name)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\t%s: .word DEFAULTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT\n",var_name);
	}
	public void allocate_int(String var_name, int value)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\t%s: .word %s\n",var_name, value);
	}
	public void allocate_string(String var_name, String value, boolean global)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\t%s_str: .asciiz \"%s\"\n",var_name, value);
		if(global) {
			fileWriter.format("\t%s: .word %s_str\n", var_name, var_name);
		}
	}

	public void jal_to_str_eq(TEMP t1 , TEMP T2 , TEMP t3)
	{
		int t1_idx = t1.getSerialNumber();
		int t2_idx = t2.getSerialNumber();
		int t3_idx = t3.getSerialNumber();
		fileWriter.format("\tsubu $sp, $sp, 4\n");
		fileWriter.format("\tsw Temp_%d, 0($sp)\n",t2_idx);
		fileWriter.format("\tsubu $sp, $sp, 4\n");
		fileWriter.format("\tsw Temp_%d, 0($sp)\n",t1_idx);
		fileWriter.format("\tjal str_eq\n");
		fileWriter.format("\taddu $sp, $sp, 8\n");
		fileWriter.format("\tmove Temp_%d, $v0\n",t3_idx);
	}

	//should be join to nathan main
	public void str_eq(){
		function_prologue("str_eq" , 0);

		fileWriter.format("\t$v0, 1\n");

		fileWriter.format("\tlw $s0, 8($fp)\n");
		fileWriter.format("\tlw $s1, 12($fp)\n");
		fileWriter.format("\tstr_eq_loop:\n");
		fileWriter.format("\tlb $s2, 0($s0)\n");//load single byte
		fileWriter.format("\tlb $s3, 0($s1)\n");//load single byte
		fileWriter.format("\tbne $s2, $s3, neq_label\n");
		fileWriter.format("\tbeq $s2, 0, str_eq_end\n");
		fileWriter.format("\taddu $s0, $s0 , 1\n");
		fileWriter.format("\taddu $s1, $s0 , 1\n");
		fileWriter.format("\tj str_eq_loop\n");
		fileWriter.format("\tneq_label:\n");

		fileWriter.format("\t$v0, 0\n");
		fileWriter.format("\tstr_eq_end:\n");

		//TODO: add epilogue

	}

	public void load(TEMP dst,String var_name)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,%s\n",idxdst,var_name);
	}
	public void store(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d,%s\n",idxsrc,var_name);
	}
	public void li(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tli Temp_%d,%d\n",idx,value);
	}
	public void la(String offset, String value)
	{
		fileWriter.format(".text\n");
		fileWriter.format("\tla %s,%s\n",offset,value);
	}
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void label(String inlabel)
	{
		if (inlabel.equals("main"))
		{
			fileWriter.format(".text\n");
			fileWriter.format("%s:\n",inlabel);
		}
		else
		{
			fileWriter.format("%s:\n",inlabel);
		}
	}	
	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}	
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 = oprnd1.getSerialNumber();
				
		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);				
	}
	public void new_array(TEMP dst, TEMP src)
	{
		int t0 = dst.getSerialNumber();
		int t1 = src.getSerialNumber();

		fileWriter.format("\tli $v0, 9\n");
		fileWriter.format("\tmove $a0, Temp_%d\n", t1);
		fileWriter.format("\tadd $a0, $a0, 1\n");
		fileWriter.format("\tmul $a0, $a0, 4\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tmove Temp_%d, $v0\n", t0);
		fileWriter.format("\tsw Temp_%d, 0(Temp_%d)\n", t1, t0);
	}
	public void class_dec(String class_name, ArrayList<String> function_labels)
	{
		fileWriter.format(".data\n");
		fileWriter.format("vt_%s:\n", class_name);
		for(int i = 0; i < function_labels.size(); i++) {
			fileWriter.format("\t.word %s\n", function_labels.get(i));
		}
	}
	public void field_access(TEMP dst, int offset, TEMP src) {
		int t0 = dst.getSerialNumber();
		int t1 = src.getSerialNumber();
		fileWriter.format("\tbeq Temp_%d, 0, abort\n", t1);
		fileWriter.format("\tlw Temp_%d, %d(Temp_%d)\n", t0, offset, t1);
	}
	public void field_set(TEMP o, int offset, TEMP e) {
		int t0 = o.getSerialNumber();
		int t1 = e.getSerialNumber();
		fileWriter.format("\tbeq Temp_%d, 0, abort\n", t0);
		fileWriter.format("\tsw Temp_%d, %d(Temp_%d)\n", t1, offset, t0);
	}
	public void virtual_call(TEMP object, int offset, TEMP_LIST params, TEMP dst) {
		int t0 = object.getSerialNumber();
		int t2 = dst.getSerialNumber();
		ArrayList<TEMP> temp_list = new ArrayList<TEMP>();
		while(params != null) {
			temp_list.add(params.head);
			params = params.tail;
		}

		//push arguments in reverse order
		for(int i = temp_list.size() - 1; i >= 0; i--) {
			TEMP cur = temp_list.get(i);
			int cur_t = cur.getSerialNumber();
			fileWriter.format("\tsubu $sp, $sp, 4\n");
			fileWriter.format("\tsw Temp_%d, 0($sp)\n", cur_t);
		}

		//push 'this' argument as last argument
		fileWriter.format("\tsubu $sp, $sp, 4\n");

		fileWriter.format("\tsw Temp_%d, 0($sp)\n", t0);

		// load vtable of object
		fileWriter.format("\tlw $s0, 0(Temp_%d)\n", t0);

		// load method from vtable
		fileWriter.format("\tlw $s1, %d($s0)\n", offset);

		// jump and link register
		fileWriter.format("\tjalr $s1\n");

		// move sp back by (#arguments+1)*4
		fileWriter.format("\taddu $sp, $sp, %d\n", (temp_list.size()+1)*4);

		// read return value

		fileWriter.format("\tmove Temp_%d, $v0\n", t2);

	}

	public void arrray_access(TEMP t0 , TEMP t1 , TEMP t2){
		int t0_idx = t0.getSerialNumber();
		int t1_idx = t1.getSerialNumber();
		int t2_idx = t2.getSerialNumber();

		//error handilng
		fileWriter.format("\tbltz Temp_%d, abort_nathannnnn\n",t2_idx);
		fileWriter.format("\tlw $s0, 0(Temp_%d)\n",t1_idx);
		fileWriter.format("\tbge Temp_%d, $s0, abort_nathannnnn\n",t2_idx);

		fileWriter.format("\tmove $s0, Temp_%d\n",t2_idx);
		fileWriter.format("\tadd $s0, $s0, 1\n");
		fileWriter.format("\tmul $s0, $s0, 4\n");
		fileWriter.format("\taddu $s0, Temp_%d, $s0\n",t1_idx);
		fileWriter.format("\tlw Temp_%d, 0($s0)\n",t0_idx);

		fileWriter.format("\tabort_nathannnnn:\n");
		fileWriter.format("\tli $v0,10\n");
		fileWriter.format("\tsyscall\n");

	}

	public void new_class(TEMP t0 , TYPE_ID[] fields_array , int size_of_class , String vt_name){
		//malloc
		int t0_idx = t0.getSerialNumber();
		fileWriter.format("\tli $v0, 9\n");
		fileWriter.format("\tli $a0, %d\n" , size_of_class*4);
		fileWriter.format("\tsyscall\n");

		//set vt at index 0 of the object after malloc
		fileWriter.format("\tmove Temp_%d, $v0\n",t0_idx);
		fileWriter.format("\tla $s0, %s\n" , vt_name);
		fileWriter.format("\tsw $s0, 0(Temp_%d)\n",t0_idx);

		//start to set the fields
		//for now store in $s0 some defoult value
		String default_val = "DEFAULT";
		for (int i = 1; i < size_of_class; i++) {
			int offest = 4*i;
			fileWriter.format("\tli $s0, %s\n",default_val);
			//add here check if exist non-default value
			fileWriter.format("\tsw $s0, %d(Temp_%d)\n",offest,t0_idx);
		}
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./output/";
				String filename=String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname+filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
		}
		return instance;
	}
}
