
import java.util.*;

public class InterferenceGraphNode
{
    String node_name;
    ArrayList<InterferenceGraphNode> neighbors;
    boolean isInStack;
    int color;

    public InterferenceGraphNode(String node_name) {
        this.node_name = node_name;
        this.neighbors = new ArrayList<InterferenceGraphNode>();
        this.isInStack = false;
        this.color = -1;
    }

}
