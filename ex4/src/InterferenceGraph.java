
import java.util.*;

public class InterferenceGraph
{

    ArrayList <ArrayList <InterferenceGraphNode>> edges;       // a list of current edges in graph.
    ArrayList <InterferenceGraphNode> nodes;                                    // a list of all nodes.

    public InterferenceGraph() {
        this.edges = new ArrayList <ArrayList<InterferenceGraphNode>>();
        this.nodes = new ArrayList <InterferenceGraphNode>();
    }

}
