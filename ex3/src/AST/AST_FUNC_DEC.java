package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_FUNC_DEC extends AST_DEC {
    public AST_FUNC_DEC fd;

    public AST_FUNC_DEC() {

    }

    public AST_FUNC_DEC(AST_FUNC_DEC fd , int lineNumber) {
        this.lineNumber = lineNumber;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.fd = fd;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE FUNC_DEC\n");

        /***********************************/
        /* RECURSIVELY PRINT ... */
        /***********************************/
        if (fd != null) fd.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("funcDec"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,fd.SerialNumber);

    }

    public TYPE SemantMe() {
        return this.fd.SemantMe();
    }
    public TYPE GetSignature() {
        return this.fd.GetSignature();
    }

    public static boolean CheckReturnTypes(TYPE retType) {
        if(AST_Node.retTypesList.head == null) {
            return true;
        }
        while(AST_Node.retTypesList != null) {
            if(!isT1SubInstanceT2(AST_Node.retTypesList.head, retType)) {
                AST_Node.file_writer.print(String.format("ERROR(%d)", AST_Node.retStmtList.head.lineNumber));
                AST_Node.file_writer.close();
                return false;
            }
            AST_Node.retTypesList = AST_Node.retTypesList.tail;
            AST_Node.retStmtList = AST_Node.retStmtList.tail;
        }
        return true;
    }

}
