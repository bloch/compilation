
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class RegisterAllocation {

    static public ArrayList<ArrayList<CFGNode>> BuildCFG(String outputFilename) {
        try {
            ArrayList<ArrayList<CFGNode>> cfg_list = new ArrayList<ArrayList<CFGNode>>();
            FileReader file_reader = new FileReader(outputFilename);
            BufferedReader buffered_reader = new BufferedReader(file_reader);

            String line = buffered_reader.readLine();
            while (!line.equals(".text")) {         // could be null.. shouldn't be.
                line = buffered_reader.readLine();
            }
            line = buffered_reader.readLine();      // code starts here.

            while (!line.equals("abort:")) {
                String function_name = line.substring(0, line.length() - 1);
                cfg_list.add(new ArrayList<CFGNode>());

                for (int i = 0; i < 28; i++) {       // ignore prologue of function(NO TEMPs)
                    line = buffered_reader.readLine();
                }

                while (!line.equals(function_name + "_epilogue:")) {
                    CFGNode cur_node = new CFGNode(line);
                    cfg_list.get(cfg_list.size() - 1).add(cur_node);
                    line = buffered_reader.readLine();
                }

                for (int i = 0; i < 16; i++) {       // ignore epilogue of function(NO TEMPs)
                    line = buffered_reader.readLine();
                }
            }

            for (int i = 0; i < cfg_list.size(); i++) {
                ArrayList<CFGNode> cur_cfg = cfg_list.get(i);
                String function_name = cur_cfg.get(0).command.substring(0, cur_cfg.get(0).command.length() - 6);
                //System.out.println(function_name);
                for (int j = 0; j < cur_cfg.size(); j++) {
                    CFGNode cur_node = cur_cfg.get(j);
                    String[] splitted_cmds = cur_node.command.trim().split(" ");
                    if (splitted_cmds[0].equals("j")) {
                        if (splitted_cmds[1].equals(function_name + "_epilogue")) {
                            if (cur_cfg.size() > j + 1) {
                                cur_node.successors.add(cur_cfg.get(j + 1));
                            }
                        } else {
                            CFGNode dest_node = find_cfg_node(cur_cfg, splitted_cmds[1] + ":");
                            cur_node.successors.add(dest_node);
                        }
                    } else if (splitted_cmds[0].equals("beq")) {
                        CFGNode dest_node = find_cfg_node(cur_cfg, splitted_cmds[3] + ":");
                        cur_node.successors.add(dest_node);
                        if (cur_cfg.size() > j + 1) {
                            cur_node.successors.add(cur_cfg.get(j + 1));
                        }
                    } else if (splitted_cmds[0].equals("bne")) {
                        CFGNode dest_node = find_cfg_node(cur_cfg, splitted_cmds[3] + ":");
                        cur_node.successors.add(dest_node);
                        if (cur_cfg.size() > j + 1) {
                            cur_node.successors.add(cur_cfg.get(j + 1));
                        }
                    } else {
                        if (cur_cfg.size() > j + 1) {
                            cur_node.successors.add(cur_cfg.get(j + 1));
                        }
                    }
                }
            }
            //print_cfg_list(cfg_list);
            return cfg_list;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    static public CFGNode find_cfg_node(ArrayList<CFGNode> cur_cfg, String label) {
        for (int i = 0; i < cur_cfg.size(); i++) {
            CFGNode cur_node = cur_cfg.get(i);
            if (cur_node.command.equals(label)) {
                return cur_node;
            }
        }
        System.out.println("UNREACHABLE CODE!!!!!!!!!!");
        return null;
    }

    static public void print_cfg_list(ArrayList<ArrayList<CFGNode>> cfg_list) {
        for (int i = 0; i < cfg_list.size(); i++) {
            ArrayList<CFGNode> cur_cfg = cfg_list.get(i);
            for (int j = 0; j < cur_cfg.size(); j++) {
                CFGNode cur_node = cur_cfg.get(j);
                System.out.println(cur_node.command.trim());
                for (int w = 0; w < cur_node.successors.size(); w++) {
                    System.out.println("\t" + cur_node.successors.get(w).command.trim());
                }
                System.out.println();
            }
            if (i != cfg_list.size() - 1) {
                System.out.println();
                System.out.println("NEXT FUNCTION: ");
                System.out.println();
            }
        }
    }

    static public void LivenessAnalysis(ArrayList<CFGNode> cfg) {
        boolean stop;
        do {
            stop = true;
            for (int i = cfg.size() - 1; i >= 0; i--) {
                CFGNode cur_node = cfg.get(i);
                boolean changed = cur_node.run_liveness_step();


                System.out.print("OUT: ");
                for (String elem : cur_node.OUT) {
                    System.out.print(elem + ", ");
                }
                System.out.println();
                System.out.println("the command: "+cur_node.command);
                System.out.print("IN: ");
                for (String elem : cur_node.IN) {
                    System.out.print(elem + ", ");
                }
                System.out.println();


                if (changed) {
                    stop = false;
                }
            }
        } while (!stop);
    }

    static public InterferenceGraph BuildInterferenceGraph(ArrayList<CFGNode> cfg) {
        InterferenceGraph graph = new InterferenceGraph();
        for (int i = 0; i < cfg.size(); i++) {
            ArrayList<String> out = new ArrayList<String>(cfg.get(i).OUT);

            for (int j = 0; j < out.size(); j++) {                                   // Build Nodes of Interference Graph.
                String var_name = out.get(j);
                InterferenceGraphNode node = CheckIfNodeExists(graph, var_name);
                if (node == null) {
                    node = new InterferenceGraphNode(var_name);
                    graph.nodes.add(node);
                }
            }

            for (int j = 0; j < out.size(); j++) {                                   // Build Edges of Interference Graph
                String var_name = out.get(j);
                InterferenceGraphNode node = CheckIfNodeExists(graph, var_name);
                if (node == null) {
                    System.out.println("ERROR!!!!! SHOULDN'T HAPPEN!!!");
                    System.exit(0);
                }

                for (int k = j + 1; k < out.size(); k++) {
                    String neighbor_name = out.get(k);
                    InterferenceGraphNode neighbor_node = CheckIfNodeExists(graph, neighbor_name);
                    if (node == null) {
                        System.out.println("ERROR!!!!! SHOULDN'T HAPPEN!!!");
                        System.exit(0);
                    }

                    boolean edge_exists = CheckIfEdgeExists(graph, var_name, neighbor_name);
                    if (!edge_exists) {
                        ArrayList<InterferenceGraphNode> new_edge = new ArrayList<InterferenceGraphNode>();
                        new_edge.add(node);
                        new_edge.add(neighbor_node);
                        graph.edges.add(new_edge);
                        node.neighbors.add(neighbor_node);
                        neighbor_node.neighbors.add(node);
                    }
                }

            }
        }
        print_interference_graph(graph);
        return graph;
    }

    static public InterferenceGraphNode CheckIfNodeExists(InterferenceGraph graph, String var_name) {
        for (int i = 0; i < graph.nodes.size(); i++) {
            if (graph.nodes.get(i).node_name.equals(var_name)) {
                return graph.nodes.get(i);
            }
        }
        return null;
    }

    static public boolean CheckIfEdgeExists(InterferenceGraph graph, String node1_name, String node2_name) {
        for (int i = 0; i < graph.edges.size(); i++) {
            if (graph.edges.get(i).get(0).node_name.equals(node1_name) && graph.edges.get(i).get(1).node_name.equals(node2_name)) {
                return true;
            }
            if (graph.edges.get(i).get(0).node_name.equals(node2_name) && graph.edges.get(i).get(1).node_name.equals(node1_name)) {
                return true;
            }
        }
        return false;
    }


    static public void print_interference_graph(InterferenceGraph graph) {
        System.out.println("Nodes: ");
        for (int i = 0; i < graph.nodes.size(); i++) {
            System.out.println("\t" + graph.nodes.get(i).node_name);
        }
        System.out.println("Edges: ");
        for (int i = 0; i < graph.edges.size(); i++) {
            System.out.println("\t" + graph.edges.get(i).get(0).node_name + ", " + graph.edges.get(i).get(1).node_name);
        }
    }
}