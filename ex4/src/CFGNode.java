
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
        HashSet<String> original_IN = new HashSet(this.IN);
        HashSet<String> original_OUT = new HashSet(this.OUT);
        //1. UNION
        HashSet<String> new_out = new HashSet<String>();
        for(CFGNode successor: this.successors) {

            new_out.addAll(successor.IN);
            //this.OUT.addAll(successor.IN);
        }
        this.OUT = new_out;

        this.IN = new HashSet<String>(this.OUT);

        //2. tipul per pkuda
        String[] cmd_tokens = this.command.trim().split(" ");

        if(cmd_tokens[0].equals("add") || cmd_tokens[0].equals("addu") || cmd_tokens[0].equals("sub")
                || cmd_tokens[0].equals("subu") || cmd_tokens[0].equals("mul") || cmd_tokens[0].equals("div")) {

            if(cmd_tokens[1].contains("Temp_")) {
                String dest = cmd_tokens[1].substring(0, cmd_tokens[1].length() - 1);
                this.IN.remove(dest);
            }
            if(cmd_tokens[2].contains("Temp_")) {
                String arg1 = cmd_tokens[2].substring(0, cmd_tokens[2].length() - 1);
                this.IN.add(arg1);
            }
            if(cmd_tokens[3].contains("Temp_")) {
                String arg2 = cmd_tokens[3];
                this.IN.add(arg2);
            }
        }
        else if(cmd_tokens[0].equals("move") || cmd_tokens[0].equals("la") || cmd_tokens[0].equals("li")) {
            /**
             *  move register, register
             *  la   register, label
             *  li   register, immediate (a number)
             * */
            if(cmd_tokens[1].contains("Temp_")) {
                String dest = cmd_tokens[1].substring(0, cmd_tokens[1].length() - 1);
                this.IN.remove(dest);
            }

            if(cmd_tokens[2].contains("Temp_")) {
                String arg1 = cmd_tokens[2];
                this.IN.add(arg1);
            }
        }
        else if(cmd_tokens[0].equals("lw") || cmd_tokens[0].equals("lb")) {
            if(cmd_tokens[1].contains("Temp_")) {
                String dest = cmd_tokens[1].substring(0, cmd_tokens[1].length() - 1);
                this.IN.remove(dest);
            }

            if(cmd_tokens[2].contains("Temp_")) {
                int index = cmd_tokens[2].indexOf("Temp_");
                String arg1 = cmd_tokens[2].substring(index, cmd_tokens[2].length() - 1);
                this.IN.add(arg1);
            }
        }
        else if(cmd_tokens[0].equals("sw") || cmd_tokens[0].equals("sb")) {
            if(cmd_tokens[1].contains("Temp_")) {
                String dest = cmd_tokens[1].substring(0, cmd_tokens[1].length() - 1);
                this.IN.add(dest);
            }

            if(cmd_tokens[2].contains("Temp_")) {
                int index = cmd_tokens[2].indexOf("Temp_");
                String arg1 = cmd_tokens[2].substring(index, cmd_tokens[2].length() - 1);
                this.IN.add(arg1);
            }
        }
        else if(cmd_tokens[0].equals("blt") || cmd_tokens[0].equals("bge") || cmd_tokens[0].equals("bne")
                || cmd_tokens[0].equals("beq")  || cmd_tokens[0].equals("bltz")  || cmd_tokens[0].equals("ble")  || cmd_tokens[0].equals("bgt")) {

            if(cmd_tokens[1].contains("Temp_")) {
                String arg1 = cmd_tokens[1].substring(0, cmd_tokens[1].length() - 1);
                this.IN.add(arg1);
            }

            if(cmd_tokens[2].contains("Temp_")) {
                String arg2 = cmd_tokens[2].substring(0, cmd_tokens[2].length() - 1);
                this.IN.add(arg2);
            }
        }

        // blt bge bne beq bltz

        if(this.IN.equals(original_IN) && this.OUT.equals(original_OUT)) {
            return false;
        }
        return true;
    }

}
