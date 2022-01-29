
import java.util.*;

public class CFGNode
{
    String command;
    LinkedList <CFGNode> successors;

    HashSet <String> IN;
    HashSet <String> OUT;

    public CFGNode(String cmd)
    {
        this.command = cmd;
        this.successors = new LinkedList<CFGNode>();
        this.IN = new HashSet<String>();
        this.OUT = new HashSet<String>();
    }

    public boolean run_liveness_step() {
        //1. UNION
        for(CFGNode successor: this.successors) {
            this.OUT.addAll(successor.IN);
        }
        //2. tipul per pkuda
        String[] cmd_tokens = this.command.trim().split(" ");

        if(cmd_tokens[0].equals("add") || cmd_tokens[0].equals("addu") || cmd_tokens[0].equals("sub")
                || cmd_tokens[0].equals("subu") || cmd_tokens[0].equals("mul") || cmd_tokens[0].equals("div")) {
            String dest = cmd_tokens[1].substring(0, cmd_tokens[1].length() - 1);
            System.out.println(this.command);
            System.out.println(cmd_tokens[1]);
            System.out.println(dest);

        }

        //3. azava be IN
        return false;
    }

}
