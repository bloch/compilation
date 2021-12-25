package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_NEW_EXP_2 extends AST_NEW_EXP {
    public AST_TYPE t;
    public AST_EXP e;

    public AST_NEW_EXP_2(AST_TYPE t, AST_EXP e, int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== newExp -> NEW type [ exp ]\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.t = t;
        this.e = e;
    }
    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE NEW_EXP_2\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (t != null) t.PrintMe();
        if (e != null) e.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("newExp_2"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,t.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);

    }

    public TYPE SemantMe() {
        /****************************/
        /* [0] Semant the expression */
        /****************************/
        if (this.e.SemantMe() != TYPE_INT.getInstance()) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR [%d:%d] expression inside BRACKETS is not integral\n",2,2);
            System.exit(0);
        }
        //Now we know e is INTEGRAL
        if (this.e instanceof AST_EXP_INT) {
            int value = ((AST_EXP_INT) this.e).value;
            if (value <= 0) {
                AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
                AST_Node.file_writer.close();
                System.out.format(">> ERROR AST_NEW_EXP_2: expression inside BRACKETS constant integral but <= 0\n");
                System.exit(0);
            }
        }

        return GetSignature(this.t);
    }

    public TEMP IRme(){
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

        (AST_TYPE_ID) ta = (AST_TYPE_ID) t; //must be type array , otherwise it will fail at semantic level
        String name = ta.name;

        IR.getInstance().AddIRcommand(new IRcommand_New_Array(dst,name,e.IRme()));
        return dst;

    }

}
