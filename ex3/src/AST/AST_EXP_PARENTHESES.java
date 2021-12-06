package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_EXP_PARENTHESES extends AST_EXP{
    /***************/
    /*  exp -> (exp) */
    /***************/
    public AST_EXP exp;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_PARENTHESES(AST_EXP exp)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== exp -> (exp)\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.exp = exp;
    }

    /***********************************************/
    /* The default message for an exp parentheses AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = EXP PARENTHESES AST NODE */
        /************************************/
        System.out.print("AST NODE EXP PARENTHESES\n");

        /*****************************/
        /* RECURSIVELY PRINT exp ... */
        /*****************************/
        if (exp != null) exp.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "EXP\nPARENTHESES");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);

    }

    public TYPE SemnatMe(){return this.exp.SemantMe();}
}

