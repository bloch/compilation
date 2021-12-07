package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_STMT_RETURN_EXP extends AST_STMT{
    public AST_EXP exp;

    public AST_STMT_RETURN_EXP(AST_EXP exp)
    {
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
            System.out.println(">> ERROR STMT_RETURN_EXP: illegal return exp");
            System.exit(0);
            return null;
        }
        return null;
    }

}
