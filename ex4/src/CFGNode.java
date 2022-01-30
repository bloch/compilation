
import java.util.*;

public class CFGNode
{
    String command;
    LinkedList <CFGNode> successors;

    HashSet <String> IN;
    HashSet <String> OUT;
    boolean flag;

    public CFGNode(String cmd)
    {
        this.command = cmd;
        this.successors = new LinkedList<CFGNode>();
        this.IN = new HashSet<String>();
        this.OUT = new HashSet<String>();
    }

    public boolean run_liveness_step() {
        HashSet <String> clonedSet = new HashSet<String>();
        clonedSet = (HashSet) this.OUT.clone();
        this.flag = false;
        // union part
        for (CFGNode elem : this.successors){
            this.OUT.addAll(elem.IN);
        }
        if (!(this.OUT.equals(clonedSet)))
            this.flag = true;

        // in <- f(out)
        HashSet <String> cmd2 = new HashSet<String>(Arrays.asList("move", "la", "li"));
        HashSet <String> cmd3 = new HashSet<String>(Arrays.asList("add", "addi","addu", "sub", "subu", "mul", "div"));
        HashSet <String> cmd4 = new HashSet<String>(Arrays.asList("beq", "bne","bge", "bgez", "bgt", "bgtz", "blt", "bltz", "ble", "blez"));
        clonedSet = (HashSet) this.IN.clone();
        this.IN.addAll(this.OUT);
        String[] splitCmd = this.command.trim().split(" ");
        int index = -1;
        if (splitCmd[0].equals("sw") || splitCmd[0].equals("sb")){
            index = splitCmd[1].indexOf("Temp_");
            if (index != -1){
                this.IN.add(splitCmd[1].substring(0,splitCmd[1].length()-1));
            }
            index = splitCmd[2].indexOf("Temp_");
            if (index != -1){
                this.IN.add("Temp_"+splitCmd[2].substring(index+5, splitCmd[2].length()-1));
            }
        }
        else if(splitCmd[0].equals("lw") || splitCmd[0].equals("lb")){
            index = splitCmd[1].indexOf("Temp_");
            if (index != -1){
                this.IN.remove(splitCmd[1].substring(0,splitCmd[1].length()-1));
            }
            index = splitCmd[2].indexOf("Temp_");
            if (index != -1){
                this.IN.add("Temp_"+splitCmd[2].substring(index+5, splitCmd[2].length()-1));
            }
        }
        else if (cmd2.contains(splitCmd[0])){
            index = splitCmd[1].indexOf("Temp_");
            if (index != -1){
                this.IN.remove(splitCmd[1].substring(0,splitCmd[1].length()-1));
            }
            index = splitCmd[2].indexOf("Temp_");
            if (index != -1){
                this.IN.add(splitCmd[2]);
            }
        }
        else if (cmd3.contains(splitCmd[0])){
            index = splitCmd[1].indexOf("Temp_");
            if (index != -1){
                this.IN.remove(splitCmd[1].substring(0,splitCmd[1].length()-1));
            }
            index = splitCmd[2].indexOf("Temp_");
            if (index != -1){
                this.IN.add(splitCmd[2].substring(0,splitCmd[2].length()-1));
            }
            index = splitCmd[3].indexOf("Temp_");
            if (index != -1){
                this.IN.add(splitCmd[3]);
            }
        }
        else if (cmd4.contains(splitCmd[0])){
            index = splitCmd[1].indexOf("Temp_");
            if (index != -1){
                this.IN.add(splitCmd[1].substring(0,splitCmd[1].length()-1));
            }
            index = splitCmd[2].indexOf("Temp_");
            if (index != -1){
                this.IN.add(splitCmd[2].substring(0,splitCmd[2].length()-1));
            }
        }

        if (!(this.IN.equals(clonedSet)))
            this.flag = true;

        return flag;
    }
}
