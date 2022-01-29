
import java.util.*;

public class InterferenceGraph
{

    ArrayList <ArrayList <InterferenceGraphNode>> edges;       // a list of current edges in graph.
    ArrayList <InterferenceGraphNode> nodes;                                    // a list of all nodes.
    Stack<InterferenceGraphNode> stk;

    public InterferenceGraph() {
        this.edges = new ArrayList <ArrayList<InterferenceGraphNode>>();
        this.nodes = new ArrayList <InterferenceGraphNode>();
        this.stk = new Stack<InterferenceGraphNode>();
    }





    public int calculate_real_degree(InterferenceGraphNode node){
        String node_name = node.node_name;
        int count = 0;
        for (int i = 0; i < this.edges.size(); i++) {
            if (this.edges.get(i).get(0).node_name.equals(node_name) || this.edges.get(i).get(1).node_name.equals(node_name)) {
                count++;
            }
        }
        return count;

    }


    public void remove_node_from_graph_and_add_to_stack(InterferenceGraphNode node){
        this.stk.push(node); //add to stack
        node.isInStack = true; //change flag

        ArrayList<Integer> indexes_to_remove = new ArrayList<Integer>();
        for (int i = 0; i < this.edges.size(); i++) { //remove all edges from the graph which contain <node>
            if (this.edges.get(i).get(0).node_name.equals(node.node_name) || this.edges.get(i).get(1).node_name.equals(node.node_name)) {
                //this.edges.remove(i);
                indexes_to_remove.add(i);
            }
        }
        for(int i = 0; i < indexes_to_remove.size(); i++) {
            this.edges.remove(indexes_to_remove.get(i));
        }
    }

    public HashSet<Integer> neighbors_colors(InterferenceGraphNode node) {
        HashSet<Integer> color_set = new HashSet<>();
        for (InterferenceGraphNode neighbor : node.neighbors) {
            if (!neighbor.isInStack) {
                color_set.add(neighbor.color);
            }
        }
        return color_set;
    }

    public void select_on_graph(){
        //clean the graph and fill the stack with all the nodes
        while (this.stk.size() < this.nodes.size()){
            for (int i = 0; i < this.nodes.size(); i++) {
                InterferenceGraphNode curr_node = this.nodes.get(i);
                int degree = this.calculate_real_degree(curr_node);
                if (degree < 10 && !(curr_node.isInStack)) {
                    this.remove_node_from_graph_and_add_to_stack(curr_node);
                }
            }
        }

        while (!this.stk.empty()){
            InterferenceGraphNode curr_node = this.stk.pop();
            curr_node.isInStack = false;
            HashSet<Integer> color_set = this.neighbors_colors(curr_node);
            for (int i = 0; i < 9; i++) {
                if (!color_set.contains(i)){
                    curr_node.color = i;
                    break;
                }
            }
        }
    }

    public void print_nodes_colors(){
        for (int i = 0; i < this.nodes.size(); i++) {
            System.out.println(this.nodes.get(i).node_name + " color = " + this.nodes.get(i).color);
        }
    }


}
