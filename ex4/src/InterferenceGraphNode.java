
import java.util.*;

public class InterferenceGraphNode
{
    String node_name;
    ArrayList<InterferenceGraphNode> neighbors;

    public InterferenceGraphNode(String node_name) {
        this.node_name = node_name;
        this.neighbors = new ArrayList<InterferenceGraphNode>();
    }

}
