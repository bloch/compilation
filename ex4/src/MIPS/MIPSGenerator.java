/***********/
/* PACKAGE */
/***********/
package MIPS;
import java.util.*;
import TYPES.*;
import AST.*;
import java.util.*;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

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
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
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
		int i1 =oprnd1.getSerialNumber();
				
		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);				
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
