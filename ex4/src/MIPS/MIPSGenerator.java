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

	private int MAX_NUM = 32767;
	private int MIN_NUM = -32768;
	private int const_str_cnt = 0;
	private int str_concat_cnt = 0;

	private ArrayList<String> code_commands;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.print(".text\n");
		for(int i = 0; i < code_commands.size(); i++) {
			fileWriter.print(code_commands.get(i));
		}
		fileWriter.print("abort:\n");
		fileWriter.print("\tli $v0, 10\n");
		fileWriter.print("\tsyscall\n");

		fileWriter.print("abort_access_violation:\n");
		fileWriter.print("\tla $a0, string_access_violation\n");
		fileWriter.print("\tli $v0, 4\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.print("\tli $v0, 10\n");
		fileWriter.print("\tsyscall\n");

		fileWriter.print("abort_invalid_pointer_dereference:\n");
		fileWriter.print("\tla $a0, string_invalid_ptr_dref\n");
		fileWriter.print("\tli $v0, 4\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.print("\tli $v0, 10\n");
		fileWriter.print("\tsyscall\n");

		fileWriter.print("abort_division_by_zero:\n");
		fileWriter.print("\tla $a0, string_illegal_div_by_0\n");
		fileWriter.print("\tli $v0, 4\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.print("\tli $v0, 10\n");
		fileWriter.print("\tsyscall\n");

		fileWriter.print("main:\n");
		fileWriter.print("\tjal user_main\n");
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}
	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
//		fileWriter.format("\tmove $a0, Temp_%d\n", idx);
//		fileWriter.format("\tli $v0,1\n");
//		fileWriter.format("\tsyscall\n");
		code_commands.add(String.format("\tmove $a0, Temp_%d\n", idx));
		code_commands.add(String.format("\tli $v0, 1\n"));
		code_commands.add(String.format("\tsyscall\n"));

		code_commands.add(String.format("\tla $a0, space_for_printInt_space\n"));
		code_commands.add(String.format("\tli $v0, 4\n"));
		code_commands.add(String.format("\tsyscall\n"));
	}

	public void concat_strings(TEMP dst,TEMP oprnd1,TEMP oprnd2, TEMP tmp, TEMP tmp2,String label_string1, String label_string2, String label_end, String label_looplen1,String label_strlen_exit1,String label_looplen2,String label_strlen_exit2){
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();
		int tmpid = tmp.getSerialNumber();
		int tmpid2 = tmp2.getSerialNumber();
		str_concat_cnt++;

//		code_commands.add(String.format("\tli $v0, 9\n"));
//		code_commands.add(String.format("\tmove $a0, Temp_%d\n",i1));
//		code_commands.add(String.format("\tadd $a0, $a0, Temp_%d\n",i2));;
//		code_commands.add(String.format("\tsub $a0, $a0, 1\n"));
//		code_commands.add(String.format("\tsyscall\n"));
//		code_commands.add(String.format("\tmove Temp_%d, $v0\n",dstidx)); Temp_



		code_commands.add(String.format("\tmove $s0, Temp_%d\n",i1));
		code_commands.add(String.format("\tli $s1, 0\n")); //counter
		code_commands.add(String.format("%s:\n",label_looplen1));
		code_commands.add(String.format("\tlb $s2, 0($s0)\n"));
		code_commands.add(String.format("\tbeq $s2, $zero, %s\n",label_strlen_exit1));
		code_commands.add(String.format("\taddi $s1, $s1, 1\n"));
		code_commands.add(String.format("\taddi $s0, $s0, 1\n"));
		code_commands.add(String.format("\tj %s\n",label_looplen1));
		code_commands.add(String.format("%s:\n",label_strlen_exit1));

		code_commands.add(String.format("\tmove $a0, $s1\n"));

		code_commands.add(String.format("\tmove $s0, Temp_%d\n",i2));
		code_commands.add(String.format("\tli $s1, 0\n")); //counter
		code_commands.add(String.format("%s:\n",label_looplen2));
		code_commands.add(String.format("\tlb $s2, 0($s0)\n"));
		code_commands.add(String.format("\tbeq $s2, $zero, %s\n",label_strlen_exit2));
		code_commands.add(String.format("\taddi $s1, $s1, 1\n"));
		code_commands.add(String.format("\taddi $s0, $s0, 1\n"));
		code_commands.add(String.format("\tj %s\n",label_looplen2));
		code_commands.add(String.format("%s:\n",label_strlen_exit2));

		code_commands.add(String.format("\tadd $a0, $a0, $s1\n"));
		code_commands.add(String.format("\taddi $a0, $a0, 1\n"));

		code_commands.add(String.format("\tli $v0, 9\n"));
		code_commands.add(String.format("\tsyscall\n"));

		code_commands.add(String.format("\tmove $s0, $v0\n"));
		code_commands.add(String.format("\tmove $s3, $s0\n"));

		code_commands.add(String.format("\tmove $s1 Temp_%d\n",i1));

		code_commands.add(String.format("%s:\n",label_string1));
		code_commands.add(String.format("\tlb $s2, 0($s1)\n"));
		code_commands.add(String.format("\tbeq $s2, $zero, %s\n",label_string2));
		code_commands.add(String.format("\tsb $s2, 0($s3)\n"));
		code_commands.add(String.format("\taddi $s3, $s3, 1\n"));
		code_commands.add(String.format("\taddi $s1, $s1, 1\n"));
		code_commands.add(String.format("\tj %s\n",label_string1));


		code_commands.add(String.format("\tmove $s1 Temp_%d\n",i2));

		code_commands.add(String.format("%s:\n",label_string2));
		code_commands.add(String.format("\tlb $s2, 0($s1)\n"));
		code_commands.add(String.format("\tbeq $s2, $zero, %s\n",label_end));
		code_commands.add(String.format("\tsb $s2, 0($s3)\n"));
		code_commands.add(String.format("\taddi $s3, $s3, 1\n"));
		code_commands.add(String.format("\taddi $s1, $s1, 1\n"));
		code_commands.add(String.format("\tj %s\n",label_string2));

		code_commands.add(String.format("%s:\n",label_end));

		code_commands.add(String.format("\tsb $zero, 0($s3)\n"));
		code_commands.add(String.format("\tmove Temp_%d $s0\n",dstidx));



//		code_commands.add(String.format("\tmove $s0, Temp_%d\n",i1));
//		code_commands.add(String.format("\tli Temp_%d, 0\n",tmpid)); //counter
//		code_commands.add(String.format("%s:\n",label_looplen1));
//		code_commands.add(String.format("\tlb Temp_%d, 0($s0)\n",tmpid2));
//		code_commands.add(String.format("\tbeq $s2, $zero, %s\n",label_strlen_exit1));
//		code_commands.add(String.format("\taddi $s0, $s0, 1\n"));
//		code_commands.add(String.format("\taddi Temp_%d, Temp_%d, 1\n",tmpid,tmpid));
//		code_commands.add(String.format("\tj %s\n",label_looplen1));
//		code_commands.add(String.format("%s:\n",label_strlen_exit1));
//
//
//		//code_commands.add(String.format("\tsub $s0, $s0, Temp_%d\n",tmpid));
//		code_commands.add(String.format("\tmove $s1, Temp_%d\n",i2));
//		code_commands.add(String.format("\tmove $a0, Temp_%d\n", tmpid));
//		code_commands.add(String.format("\tli $v0, 4\n"));
//		code_commands.add(String.format("\tsyscall\n"));
//
//
//		code_commands.add(String.format("%s:\n",label_looplen2));
//		code_commands.add(String.format("\tlb Temp_%d, 0($s1)\n",tmpid2));
//		code_commands.add(String.format("\tbeq Temp_%d, $zero, %s\n",tmpid2,label_strlen_exit2));
//		code_commands.add(String.format("\taddi $s1, $s1, 1\n"));
//		code_commands.add(String.format("\taddi Temp_%d, Temp_%d, 1\n",tmpid,tmpid));
//		code_commands.add(String.format("\tj %s\n",label_looplen2));
//		code_commands.add(String.format("%s:\n",label_strlen_exit2));
//		code_commands.add(String.format("\taddi Temp_%d, Temp_%d, 1\n",tmpid,tmpid));
//		//code_commands.add(String.format("\tsub Temp_%d, Temp_%d, Temp_%d\n",i2,i2,tmpid));









//		fileWriter.format("\tconcat_str%d: .space 255\n",str_concat_cnt);
//		code_commands.add(String.format("\tla Temp_%d, concat_str%d\n",dstidx, str_concat_cnt));
//		code_commands.add(String.format("\tmove $s0, Temp_%d\n",i1));
//		code_commands.add(String.format("\tmove $s1, Temp_%d\n",i2));
//		code_commands.add(String.format("\tmove $s2, Temp_%d\n",dstidx));
//
//		code_commands.add(String.format("%s:\n",label_string1));
//		code_commands.add(String.format("\tlb Temp_%d, 0($s0)\n",tmpid));
//		code_commands.add(String.format("\tbeq Temp_%d, $zero, %s\n",tmpid,label_string2));
//		code_commands.add(String.format("\tsb Temp_%d, 0($s2)\n",tmpid));
//		code_commands.add(String.format("\taddi $s0, $s0, 1\n"));
//		code_commands.add(String.format("\taddi $s2, $s2, 1\n"));
//		code_commands.add(String.format("\tj %s\n",label_string1));
//
//		code_commands.add(String.format("%s:\n",label_string2));
//		code_commands.add(String.format("\tlb Temp_%d, 0($s1)\n",tmpid));
//		code_commands.add(String.format("\tbeq Temp_%d, $zero, %s\n",tmpid,label_end));
//		code_commands.add(String.format("\tsb Temp_%d, 0($s2)\n",tmpid));
//		code_commands.add(String.format("\taddi $s1, $s1, 1\n"));
//		code_commands.add(String.format("\taddi $s2, $s2, 1\n"));
//		code_commands.add(String.format("\tj %s\n",label_string2));
//
//		code_commands.add(String.format("%s:\n",label_end));
//		code_commands.add(String.format("\tsb $zero, 0($s2)\n"));
//		code_commands.add(String.format("\tla Temp_%d, concat_str%d\n",dstidx, str_concat_cnt));
	}

	public void print_string(TEMP t)
	{
		int idx=t.getSerialNumber();


		//code_commands.add(String.format("\tmove $s0, Temp_%d\n",i1));
		//code_commands.add(String.format("\tmove $s1, Temp_%d\n",i2));

//		code_commands.add(String.format("\tmove $s2, Temp_%d\n", idx));
//		code_commands.add(String.format("\tli $s0, 0\n")); //counter
//		code_commands.add(String.format("label_looplen1:\n"));
//		code_commands.add(String.format("\tlb $s1, 0($s2)\n",idx));
//		code_commands.add(String.format("\tbeq $s1, $zero, label_strlen_exit1\n"));
//		code_commands.add(String.format("\taddi $s0, $s0, 1\n"));
//		code_commands.add(String.format("\taddi $s2, $s2, 1\n"));
//		code_commands.add(String.format("\tj label_looplen1\n"));
//		code_commands.add(String.format("label_strlen_exit1:\n"));


		code_commands.add(String.format("\tmove $a0, Temp_%d\n", idx));
		code_commands.add(String.format("\tli $v0, 4\n"));
		code_commands.add(String.format("\tsyscall\n"));

//		code_commands.add(String.format("\tmove $a0, $s0\n"));
//		code_commands.add(String.format("\tli $v0, 1\n"));
//		code_commands.add(String.format("\tsyscall\n"));
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
//		fileWriter.format(".data\n");
		fileWriter.format("\t%s: .word DEFAULTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT\n",var_name);
	}
	public void allocate_int(String var_name, int value)
	{
//		fileWriter.format(".data\n");
		fileWriter.format("%s: .word %s\n",var_name, value);
	}
	public void allocate_string(String var_name, String value, boolean global)
	{
//		fileWriter.format(".data\n");
		fileWriter.format("\t%s_str: .asciiz \"%s\"\n",var_name, value);
		if(global) {
			fileWriter.format("\t%s: .word %s_str\n", var_name, var_name);
		}
	}
	public void load(TEMP dst,String var_name)
	{
		int idxdst=dst.getSerialNumber();
//		fileWriter.format("\tlw Temp_%d,%s\n",idxdst,var_name);
		code_commands.add(String.format("\tlw Temp_%d, %s\n",idxdst,var_name));
	}
	public void load_from_saved_register(String var_name)
	{
		//for now we need only $s0 , if needed more saved registers -->ovveride this func with addistional arg - index for saved reg
		code_commands.add(String.format("\tlw $s0, %s\n",var_name));
	}
	public void store(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
//		fileWriter.format("\tsw Temp_%d, %s\n",idxsrc,var_name);
		code_commands.add(String.format("\tsw Temp_%d, %s\n",idxsrc,var_name));
	}
	public void li(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
//		fileWriter.format("\tli Temp_%d,%d\n",idx,value);
		code_commands.add(String.format("\tli Temp_%d, %d\n",idx,value));
	}
	public void la(String offset, String value)
	{
//		fileWriter.format(".text\n");
//		fileWriter.format("\tla %s,%s\n",offset,value);
		code_commands.add(String.format("\tla %s, %s\n",offset,value));
	}

	public void allocate_and_store_const_string(TEMP t, String value)
	{
		const_str_cnt++;
		int idx=t.getSerialNumber();
		fileWriter.format("\tstr_aconst%d: .asciiz \"%s\"\n", const_str_cnt, value);
		code_commands.add(String.format("\tla Temp_%d, %s%d\n",idx,"str_aconst",const_str_cnt));
	}
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2, String label_end_max, String label_end_min)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

//		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		code_commands.add(String.format("\tadd Temp_%d, Temp_%d, Temp_%d\n",dstidx,i1,i2));

		code_commands.add(String.format("\tli $s0, %d\n",MAX_NUM));
		code_commands.add(String.format("\tble Temp_%d, $s0, %s\n",dstidx,label_end_max)); // chceck if dst <= max, if yes so jump to next checking
		code_commands.add(String.format("\tli Temp_%d, %d\n",dstidx,MAX_NUM)); // dst = max, so we jump to end_label
		code_commands.add(String.format("\tj %s\n",label_end_min)); // j end
		code_commands.add(String.format("%s:\n",label_end_max)); 	// in_label:
		code_commands.add(String.format("\tli $s0, %d\n",MIN_NUM));
		code_commands.add(String.format("\tbgt Temp_%d, $s0, %s\n",dstidx,label_end_min)); // check if dst > min
		code_commands.add(String.format("\tli Temp_%d, %d\n",dstidx,MIN_NUM));
		code_commands.add(String.format("%s:\n",label_end_min)); // end_label:

	}
	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2, String label_end_max, String label_end_min)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

//		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		code_commands.add(String.format("\tsub Temp_%d, Temp_%d, Temp_%d\n",dstidx,i1,i2));

		code_commands.add(String.format("\tli $s0, %d\n",MAX_NUM));
		code_commands.add(String.format("\tble Temp_%d, $s0, %s\n",dstidx,label_end_max)); // chceck if dst <= max, if yes so jump to next checking
		code_commands.add(String.format("\tli Temp_%d, %d\n",dstidx,MAX_NUM)); // dst = max, so we jump to end_label
		code_commands.add(String.format("\tj %s\n",label_end_min)); // j end
		code_commands.add(String.format("%s:\n",label_end_max)); 	// in_label:
		code_commands.add(String.format("\tli $s0, %d\n",MIN_NUM));
		code_commands.add(String.format("\tbgt Temp_%d, $s0, %s\n",dstidx,label_end_min)); // check if dst > min
		code_commands.add(String.format("\tli Temp_%d, %d\n",dstidx,MIN_NUM));
		code_commands.add(String.format("%s:\n",label_end_min)); // end_label:

	}
	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2, String label_end_max, String label_end_min)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

//		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		code_commands.add(String.format("\tmul Temp_%d, Temp_%d, Temp_%d\n",dstidx,i1,i2));

		code_commands.add(String.format("\tli $s0, %d\n",MAX_NUM));
		code_commands.add(String.format("\tble Temp_%d, $s0, %s\n",dstidx,label_end_max)); // chceck if dst <= max, if yes so jump to next checking
		code_commands.add(String.format("\tli Temp_%d, %d\n",dstidx,MAX_NUM)); // dst = max, so we jump to end_label
		code_commands.add(String.format("\tj %s\n",label_end_min)); // j end
		code_commands.add(String.format("%s:\n",label_end_max)); 	// in_label:
		code_commands.add(String.format("\tli $s0, %d\n",MIN_NUM));
		code_commands.add(String.format("\tbgt Temp_%d, $s0, %s\n",dstidx,label_end_min)); // check if dst > min
		code_commands.add(String.format("\tli Temp_%d, %d\n",dstidx,MIN_NUM));
		code_commands.add(String.format("%s:\n",label_end_min)); // end_label:

	}
	public void label(String inlabel)
	{
//		if (inlabel.equals("main"))
//		{
//			fileWriter.format(".text\n");
//			fileWriter.format("%s:\n",inlabel);
//		}
//		else
//		{
//			fileWriter.format("%s:\n",inlabel);
//		}
		code_commands.add(String.format("%s:\n",inlabel));
	}	
	public void jump(String inlabel)
	{
//		fileWriter.format("\tj %s\n",inlabel);
		code_commands.add(String.format("\tj %s\n",inlabel));
	}	
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
//		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);
		code_commands.add(String.format("\tblt Temp_%d, Temp_%d, %s\n",i1,i2,label));
	}
	public void bgt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

//		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);
		code_commands.add(String.format("\tbgt Temp_%d, Temp_%d, %s\n",i1,i2,label));
	}

	public void ble(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

//		fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);
		code_commands.add(String.format("\tble Temp_%d, Temp_%d, %s\n",i1,i2,label));
	}

	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
//		fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);
		code_commands.add(String.format("\tbge Temp_%d, Temp_%d, %s\n",i1,i2,label));
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
//		fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n",i1,i2,label);
		code_commands.add(String.format("\tbne Temp_%d, Temp_%d, %s\n",i1,i2,label));
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
//		fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n",i1,i2,label);
		code_commands.add(String.format("\tbeq Temp_%d, Temp_%d, %s\n",i1,i2,label));
	}
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 = oprnd1.getSerialNumber();
				
//		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);
		code_commands.add(String.format("\tbeq Temp_%d, $zero, %s\n",i1,label));
	}
	public void new_array(TEMP dst, TEMP src)
	{
		int t0 = dst.getSerialNumber();
		int t1 = src.getSerialNumber();

//		fileWriter.format("\tli $v0, 9\n");
//		fileWriter.format("\tmove $a0, Temp_%d\n", t1);
//		fileWriter.format("\tadd $a0, $a0, 1\n");
//		fileWriter.format("\tmul $a0, $a0, 4\n");
//		fileWriter.format("\tsyscall\n");
//		fileWriter.format("\tmove Temp_%d, $v0\n", t0);
//		fileWriter.format("\tsw Temp_%d, 0(Temp_%d)\n", t1, t0);
		code_commands.add(String.format("\tli $v0, 9\n"));
		code_commands.add(String.format("\tmove $a0, Temp_%d\n", t1));
		code_commands.add(String.format("\tadd $a0, $a0, 1\n"));
		code_commands.add(String.format("\tmul $a0, $a0, 4\n"));
		code_commands.add(String.format("\tsyscall\n"));
		code_commands.add(String.format("\tmove Temp_%d, $v0\n", t0));
		code_commands.add(String.format("\tsw Temp_%d, 0(Temp_%d)\n", t1, t0));
	}
	public void class_dec(String class_name, ArrayList<String> function_labels)
	{
//		fileWriter.format(".data\n");
		fileWriter.format("vt_%s:\n", class_name);
		for(int i = 0; i < function_labels.size(); i++) {
			fileWriter.format("\t.word %s\n", function_labels.get(i));
		}
	}
	public void field_access(TEMP dst, int offset, TEMP src) {
		int t0 = dst.getSerialNumber();
		int t1 = src.getSerialNumber();
		code_commands.add(String.format("\tbeq Temp_%d, 0, abort_invalid_pointer_dereference\n", t1));
		code_commands.add(String.format("\tlw Temp_%d, %d(Temp_%d)\n", t0, offset, t1));
	}
	public void field_set(TEMP o, int offset, TEMP e) {
		int t0 = o.getSerialNumber();
		int t1 = e.getSerialNumber();
		code_commands.add(String.format("\tbeq Temp_%d, 0, abort_invalid_pointer_dereference\n", t0));
		code_commands.add(String.format("\tsw Temp_%d, %d(Temp_%d)\n", t1, offset, t0));
	}
	public void virtual_call(TEMP object, int offset, TEMP_LIST params, TEMP dst) {
		int t0 = object.getSerialNumber();
		ArrayList<TEMP> temp_list = new ArrayList<TEMP>();
		while(params != null) {
			temp_list.add(params.head);
			params = params.tail;
		}
		//push arguments in reverse order
		for(int i = temp_list.size() - 1; i >= 0; i--) {
			TEMP cur = temp_list.get(i);
			int cur_t = cur.getSerialNumber();
//			fileWriter.format("\tsubu $sp, $sp, 4\n");
//			fileWriter.format("\tsw Temp_%d, 0($sp)\n", cur_t);
			code_commands.add(String.format("\tsubu $sp, $sp, 4\n"));
			code_commands.add(String.format("\tsw Temp_%d, 0($sp)\n", cur_t));
		}

		//push 'this' argument as last argument
		//fileWriter.format("\tsubu $sp, $sp, 4\n");
		code_commands.add(String.format("\tsubu $sp, $sp, 4\n"));

		//fileWriter.format("\tsw Temp_%d, 0($sp)\n", t0);
		code_commands.add(String.format("\tsw Temp_%d, 0($sp)\n", t0));

		// load vtable of object
		//fileWriter.format("\tlw $s0, 0(Temp_%d)\n", t0);
		code_commands.add(String.format("\tlw $s0, 0(Temp_%d)\n", t0));

		// load method from vtable
		//fileWriter.format("\tlw $s1, %d($s0)\n", offset);
		code_commands.add(String.format("\tlw $s1, %d($s0)\n", offset));

		// jump and link register
		//fileWriter.format("\tjalr $s1\n");
		code_commands.add(String.format("\tjalr $s1\n"));

		// move sp back by (#arguments+1)*4
		//fileWriter.format("\taddu $sp, $sp, %d\n", (temp_list.size()+1)*4);
		code_commands.add(String.format("\taddu $sp, $sp, %d\n", (temp_list.size()+1)*4));

		// read return value

		//fileWriter.format("\tmove Temp_%d, $v0\n", t2);
		if (dst!=null){
			int t2 = dst.getSerialNumber();
			code_commands.add(String.format("\tmove Temp_%d, $v0\n", t2));
		}

	}

	public void arrray_access(TEMP t0 , TEMP t1 , TEMP t2){
		int t0_idx = t0.getSerialNumber();
		int t1_idx = t1.getSerialNumber();
		int t2_idx = t2.getSerialNumber();

		code_commands.add(String.format("\tbltz Temp_%d, abort_access_violation\n",t2_idx));
		code_commands.add(String.format("\tlw $s0, 0(Temp_%d)\n",t1_idx));
		code_commands.add(String.format("\tbge Temp_%d, $s0, abort_access_violation\n",t2_idx));
		code_commands.add(String.format("\tmove $s0, Temp_%d\n",t2_idx));
		code_commands.add(String.format("\tadd $s0, $s0, 1\n"));
		code_commands.add(String.format("\tmul $s0, $s0, 4\n"));
		code_commands.add(String.format("\taddu $s0, Temp_%d, $s0\n",t1_idx));
		code_commands.add(String.format("\tlw Temp_%d, 0($s0)\n",t0_idx));
	}

	public void new_class(TEMP t0 , TYPE_ID[] fields_array , int size_of_class , String class_name){
		//malloc
		int t0_idx = t0.getSerialNumber();
//		fileWriter.format("\tli $v0, 9\n");
		code_commands.add(String.format("\tli $v0, 9\n"));
//		fileWriter.format("\tli $a0, %d\n" , size_of_class*4);
		code_commands.add(String.format("\tli $a0, %d\n" , size_of_class*4));
//		fileWriter.format("\tsyscall\n");
		code_commands.add(String.format("\tsyscall\n"));

		//set vt at index 0 of the object after malloc
//		fileWriter.format("\tmove Temp_%d, $v0\n",t0_idx);
		code_commands.add(String.format("\tmove Temp_%d, $v0\n",t0_idx));
//		fileWriter.format("\tla $s0, %s\n" , vt_name);
		code_commands.add(String.format("\tla $s0, %s\n" , "vt_" + class_name));
//		fileWriter.format("\tsw $s0, 0(Temp_%d)\n",t0_idx);
		code_commands.add(String.format("\tsw $s0, 0(Temp_%d)\n",t0_idx));

		//start to set the fields
		//for now store in $s0 some defoult value
		String default_val = "0";
		for (int i = 1; i < size_of_class; i++) {
			int offest = 4*i;
//			System.out.println(fields_array[i]);

			if (fields_array[i].int_value != 0.5){
				String real_value = "" + ((int) fields_array[i].int_value);
				//fileWriter.format("\tli $s0, %s\n",real_value);
				code_commands.add(String.format("\tli $s0, %s\n",real_value));
			}
			else if (fields_array[i].string_value != "null"){
				String real_value = fields_array[i].string_value;
				String label_name = fields_array[i].name + "_" + class_name;
				//fileWriter.format("\t%s_str: .asciiz \"%s\"\n",label_name, real_value);
				fileWriter.format("%s_str: .asciiz \"%s\"\n",label_name, real_value);
				//fileWriter.format("\tla $s0, %s\n",label_name + "_str");
				code_commands.add(String.format("\tla $s0, %s\n",label_name + "_str"));
			}
			else{
				//fileWriter.format("\tli $s0, %s\n",default_val);
				code_commands.add(String.format("\tli $s0, %s\n",default_val));
			}
			//add here check if exist non-default value
			//fileWriter.format("\tsw $s0, %d(Temp_%d)\n",offest,t0_idx);
			code_commands.add(String.format("\tsw $s0, %d(Temp_%d)\n",offest,t0_idx));
		}
	}

	public void function_prologue(String func_name, int sp_offset) {
//		fileWriter.format("%s_prologue:\n", func_name);
		code_commands.add(String.format("%s_prologue:\n", func_name));
//		fileWriter.format("\tsubu $sp, $sp, 4\n");
		code_commands.add(String.format("\tsubu $sp, $sp, 4\n"));
//		fileWriter.format("\tsw $ra, 0($sp)\n");
		code_commands.add(String.format("\tsw $ra, 0($sp)\n"));
//		fileWriter.format("\tsubu $sp, $sp, 4\n");
		code_commands.add(String.format("\tsubu $sp, $sp, 4\n"));
//		fileWriter.format("\tsw $fp, 0($sp)\n");
		code_commands.add(String.format("\tsw $fp, 0($sp)\n"));
//		fileWriter.format("\tmove $fp, $sp\n");
		code_commands.add(String.format("\tmove $fp, $sp\n"));
		// callee save t0-t9
		for(int i = 0; i < 10; i++) {
//			fileWriter.format("\tsubu $sp, $sp, 4\n");
			code_commands.add(String.format("\tsubu $sp, $sp, 4\n"));
//			fileWriter.format("\tsw $t%d, 0($sp)\n", i);
			code_commands.add(String.format("\tsw $t%d, 0($sp)\n", i));
		}
//		fileWriter.format("\tsub $sp, $sp, %d\n", sp_offset);
		code_commands.add(String.format("\tsub $sp, $sp, %d\n", sp_offset));
//		fileWriter.format("%s_body:\n", func_name);
		code_commands.add(String.format("%s_body:\n", func_name));
	}

	public void function_epilogue(String func_name) {
//		fileWriter.format("%s_epilogue:\n", func_name);
		code_commands.add(String.format("%s_epilogue:\n", func_name));
//		fileWriter.format("\tmove $sp, $fp\n");
		code_commands.add(String.format("\tmove $sp, $fp\n"));
		// calle load t0-t9
		for(int i = 0; i < 10; i++) {
//			fileWriter.format("\tlw $t%d, %d($sp)\n", i, -4 + -4*i);
			code_commands.add(String.format("\tlw $t%d, %d($sp)\n", i, -4 + -4*i));
		}
//		fileWriter.format("\tlw $fp, 0($sp)\n");
		code_commands.add(String.format("\tlw $fp, 0($sp)\n"));
//		fileWriter.format("\tlw $ra, 4($sp)\n");
		code_commands.add(String.format("\tlw $ra, 4($sp)\n"));
//		fileWriter.format("\taddu $sp, $sp, 8\n");
		code_commands.add(String.format("\taddu $sp, $sp, 8\n"));
//		fileWriter.format("\tjr $ra\n");
		code_commands.add(String.format("\tjr $ra\n"));
	}

	public void return_command(TEMP t, String j_label) {
		if(t != null) {
			int t_idx = t.getSerialNumber();
//			fileWriter.format("\tmove $v0, Temp_%d\n", t_idx);
			code_commands.add(String.format("\tmove $v0, Temp_%d\n", t_idx));
		}
		code_commands.add(String.format("\tj %s_epilogue\n", j_label));
	}

	public void call(String func_name,  TEMP_LIST params, TEMP dst) {
		ArrayList<TEMP> temp_list = new ArrayList<TEMP>();
		while(params != null) {
			temp_list.add(params.head);
			params = params.tail;
		}
		//push arguments in reverse order
		for(int i = temp_list.size() - 1; i >= 0; i--) {
			TEMP cur = temp_list.get(i);
			int cur_t = cur.getSerialNumber();
//			fileWriter.format("\tsubu $sp, $sp, 4\n");
			code_commands.add(String.format("\tsubu $sp, $sp, 4\n"));
//			fileWriter.format("\tsw Temp_%d, 0($sp)\n", cur_t);
			code_commands.add(String.format("\tsw Temp_%d, 0($sp)\n", cur_t));
		}

		// jal to func_name
//		fileWriter.format("\tjal %s\n", func_name);
		code_commands.add(String.format("\tjal %s\n", func_name));

		// return sp
//		fileWriter.format("\taddu $sp, $sp, %d\n", temp_list.size() * 4);
		code_commands.add(String.format("\taddu $sp, $sp, %d\n", temp_list.size() * 4));
		if(dst != null) {
			int t0 = dst.getSerialNumber();
//			fileWriter.format("\tmove Temp_%d, $v0\n", t0);
			code_commands.add(String.format("\tmove Temp_%d, $v0\n", t0));
		}
	}

	public void array_set(TEMP t1 , TEMP t2 , TEMP t3) {
		int t2_idx = t2.getSerialNumber();
		int t1_idx = t1.getSerialNumber();
		int t3_idx = t3.getSerialNumber();
		code_commands.add(String.format("\tbltz Temp_%d, abort_access_violation\n",t2_idx));
		code_commands.add(String.format("\tlw $s0, 0(Temp_%d)\n",t1_idx));
		code_commands.add(String.format("\tbge Temp_%d, $s0, abort_access_violation\n",t2_idx));

		code_commands.add(String.format("\tmove $s0, Temp_%d\n",t2_idx));
		code_commands.add(String.format("\tadd $s0, $s0, 1\n"));
		code_commands.add(String.format("\tmul $s0, $s0, 4\n"));
		code_commands.add(String.format("\taddu $s0, Temp_%d, $s0\n",t1_idx));

		code_commands.add(String.format("\tsw Temp_%d, 0($s0)\n", t3_idx));
	}

	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2, String label_end_max, String label_end_min)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

//		fileWriter.format("\tdiv Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		// TODO: check if (i2 != zero)
		code_commands.add(String.format("\tbeq Temp_%d, $zero, abort_division_by_zero\n",i2));

		code_commands.add(String.format("\tdiv Temp_%d, Temp_%d, Temp_%d\n",dstidx,i1,i2));
		code_commands.add(String.format("\tli $s0, %d\n",MAX_NUM));
		code_commands.add(String.format("\tble Temp_%d, $s0, %s\n",dstidx,label_end_max)); // chceck if dst <= max, if yes so jump to next checking
		code_commands.add(String.format("\tli Temp_%d, %d\n",dstidx,MAX_NUM)); // dst = max, so we jump to end_label
		code_commands.add(String.format("\tj %s\n",label_end_min)); // j end
		code_commands.add(String.format("%s:\n",label_end_max)); 	// in_label:
		code_commands.add(String.format("\tli $s0, %d\n",MIN_NUM));
		code_commands.add(String.format("\tbgt Temp_%d, $s0, %s\n",dstidx,label_end_min)); // check if dst > min
		code_commands.add(String.format("\tli Temp_%d, %d\n",dstidx,MIN_NUM));
		code_commands.add(String.format("%s:\n",label_end_min)); // end_label:
	}

	public void str_eq(TEMP t3,TEMP t1,TEMP t2, String neq_label, String str_eq_end, String str_eq_loop){
		int t3_idx = t3.getSerialNumber();
		int t1_idx = t1.getSerialNumber();
		int t2_idx = t2.getSerialNumber();

		code_commands.add(String.format("\tli Temp_%d, %d\n",t3_idx,1));
		code_commands.add(String.format("\tmove $s0, Temp_%d\n",t1_idx));
		code_commands.add(String.format("\tmove $s1, Temp_%d\n",t2_idx));
		code_commands.add(String.format("%s:\n",str_eq_loop));
		code_commands.add(String.format("\tlb $s2, 0($s0)\n"));
		code_commands.add(String.format("\tlb $s3, 0($s1)\n"));
		code_commands.add(String.format("\tbne $s2, $s3, %s\n",neq_label));
		code_commands.add(String.format("\tbeq $s2, $zero, %s\n",str_eq_end));
		code_commands.add(String.format("\taddu $s0, $s0, 1\n"));
		code_commands.add(String.format("\taddu $s1, $s1, 1\n"));
		code_commands.add(String.format("\tj %s\n",str_eq_loop));
		code_commands.add(String.format("%s:\n",neq_label));
		code_commands.add(String.format("\tli Temp_%d, %d\n",t3_idx,0));
		code_commands.add(String.format("%s:\n",str_eq_end));
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
				instance.code_commands = new ArrayList<String>();
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
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
			instance.fileWriter.print("space_for_printInt_space: .asciiz \" \"\n");
//			instance.fileWriter.print(".text\n");
		}
		return instance;
	}
}
