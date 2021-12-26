package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_STMT_RETURN_EXP extends AST_STMT{
    public AST_EXP exp;

    public AST_STMT_RETURN_EXP(AST_EXP exp, int lineNumber)
    {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== stmt -> RETURN exp;\n");

        /*******************************/
        /* COPY INPUT DATA EXP ... */
        /*******************************/
        this.exp = exp;
    }


    /**************************************************/
    /* The printing message for a STMT RETURN EXP AST node */
    /**************************************************/
    public void PrintMe()
    {
        /**********************************/
        /* AST NODE TYPE = AST STMT RETURN EXP */
        /**********************************/
        System.out.format("AST NODE STMT RETURN EXP;\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (exp != null) exp.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STMT\nRETURN\nEXP\n"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
    }

    public TYPE SemantMe() {
        TYPE exp_type = exp.SemantMe();
        if (exp_type == null) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.println(">> ERROR STMT_RETURN_EXP: illegal return exp");
            System.exit(0);
            return null;
        }
        if (exp_type instanceof TYPE_VOID) {
            AST_Node.file_writer.print(String.format("ERROR(%d)", this.lineNumber));
            AST_Node.file_writer.close();
            System.out.format(">> ERROR STMT_RETURN_EXP: ILLEGAL TO return type void implicit");
            System.exit(0);
        }

        AST_Node.retTypesList.AddToTypeList(exp_type);
        AST_Node.retStmtList.AddToStmtList(this);
        return null;
    }

    public TEMP IRme()
    {
        TEMP ret = this.exp.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Return(ret));
        return null;
    }

}
