
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.PrintWriter;

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
                    //System.out.println(line);
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
                            if (!splitted_cmds[1].equals("abort")){
                                CFGNode dest_node = find_cfg_node(cur_cfg, splitted_cmds[1] + ":");
                                cur_node.successors.add(dest_node);
                            }
                        }
                    } else if (splitted_cmds[0].equals("beq")) {
                        if (!splitted_cmds[3].equals("abort")) {
                            CFGNode dest_node = find_cfg_node(cur_cfg, splitted_cmds[3] + ":");
                            cur_node.successors.add(dest_node);
                        }
                        if (cur_cfg.size() > j + 1) {
                            cur_node.successors.add(cur_cfg.get(j + 1));
                        }
                    } else if (splitted_cmds[0].equals("bne")) {
                        if (!splitted_cmds[3].equals("abort")) {
                            CFGNode dest_node = find_cfg_node(cur_cfg, splitted_cmds[3] + ":");
                            cur_node.successors.add(dest_node);
                        }
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
        System.out.println(label);
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
        /** fix a bug that Temps_ that were not inany hash set are not in interference graph **/
        for (int i = 0; i < cfg.size(); i++) {
            String[] cmd_tokens = cfg.get(i).command.split(" ");
            for(int j = 1; j < cmd_tokens.length; j++) {
                if(cmd_tokens[j].contains("Temp_")) {
                    int index = cmd_tokens[j].indexOf("Temp_");
                    if(cmd_tokens[j].endsWith(",")) {
                        String var_name = cmd_tokens[j].substring(index, cmd_tokens[j].length() - 1);
                        if(CheckIfNodeExists(graph, var_name) == null) {
                            InterferenceGraphNode node = new InterferenceGraphNode(var_name);
                            graph.nodes.add(node);
                        }
                    }
                    else if(cmd_tokens[j].endsWith(")")) {
                        String var_name = cmd_tokens[j].substring(index, cmd_tokens[j].length() - 1);
                        if(CheckIfNodeExists(graph, var_name) == null) {
                            InterferenceGraphNode node = new InterferenceGraphNode(var_name);
                            graph.nodes.add(node);
                        }
                    }
                    else {
                        if(CheckIfNodeExists(graph, cmd_tokens[j]) == null) {
                            InterferenceGraphNode node = new InterferenceGraphNode(cmd_tokens[j]);
                            graph.nodes.add(node);
                        }
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

    static public void print_out_sets(ArrayList<CFGNode> cfg) {
        for (int i = 0; i < cfg.size(); i++) {
            CFGNode cur_cfg = cfg.get(i);
            System.out.println(cur_cfg.command.trim());
            for(String var_name : cur_cfg.OUT) {
                System.out.println("\t" + var_name);
            }
        }
    }


    static public void replace_temps(InterferenceGraph graph, ArrayList<CFGNode> cfg, String outputFilename) {
        try {
            FileReader file_reader = new FileReader(outputFilename);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            ArrayList<String> lines = new ArrayList<String>();

            String line = buffered_reader.readLine();
            while (!line.equals(cfg.get(0).command)) {
                lines.add(line);
                line = buffered_reader.readLine();
            }

            String function_name = cfg.get(0).command.substring(0, cfg.get(0).command.length() - 6);
            while (!line.equals(function_name + "_epilogue:")) {
                lines.add(replace_temps_in_line(graph, line));
                line = buffered_reader.readLine();
            }

            while (line != null) {
                lines.add(line);
                line = buffered_reader.readLine();
            }

            PrintWriter file_writer = new PrintWriter(outputFilename);
            for(int i = 0; i < lines.size(); i++) {
                file_writer.println(lines.get(i));
            }
            file_writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    static public String replace_temps_in_line(InterferenceGraph graph, String cmd) {
        String[] cmd_tokens = cmd.trim().split(" ");

        if(cmd_tokens[0].equals("add") || cmd_tokens[0].equals("addu") || cmd_tokens[0].equals("sub")
                || cmd_tokens[0].equals("subu") || cmd_tokens[0].equals("mul") || cmd_tokens[0].equals("div")) {
            String[] new_cmd = new String[4];
            new_cmd[0] = "\t" + cmd_tokens[0];
            if(cmd_tokens[1].contains("Temp_")) {
                String dest = cmd_tokens[1].substring(0, cmd_tokens[1].length() - 1);
                InterferenceGraphNode node = CheckIfNodeExists(graph, dest);
                if(node == null) {
                    System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.exit(0);
                }
                new_cmd[1] = "$t" + node.color + ",";
            }
            else {
                new_cmd[1] = cmd_tokens[1];
            }

            if(cmd_tokens[2].contains("Temp_")) {
                String arg1 = cmd_tokens[2].substring(0, cmd_tokens[2].length() - 1);
                InterferenceGraphNode node = CheckIfNodeExists(graph, arg1);
                if(node == null) {
                    System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.exit(0);
                }
                new_cmd[2] = "$t" + node.color + ",";
            }
            else {
                new_cmd[2] = cmd_tokens[2];
            }

            if(cmd_tokens[3].contains("Temp_")) {
                String arg2 = cmd_tokens[3];
                InterferenceGraphNode node = CheckIfNodeExists(graph, arg2);
                if(node == null) {
                    System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.exit(0);
                }
                new_cmd[3] = "$t" + node.color;
            }
            else  {
                new_cmd[3] = cmd_tokens[3];
            }
            return String.join(" ", new_cmd);
        }
        else if(cmd_tokens[0].equals("move") || cmd_tokens[0].equals("la") || cmd_tokens[0].equals("li")) {
            /**
             *  move register, register
             *  la   register, label
             *  li   register, immediate (a number)
             * */
            String[] new_cmd = new String[3];
            new_cmd[0] = "\t" + cmd_tokens[0];
            if(cmd_tokens[1].contains("Temp_")) {
                String dest = cmd_tokens[1].substring(0, cmd_tokens[1].length() - 1);
                InterferenceGraphNode node = CheckIfNodeExists(graph, dest);
                if(node == null) {
                    System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.exit(0);
                }
                new_cmd[1] = "$t" + node.color + ",";
            }
            else {
                new_cmd[1] = cmd_tokens[1];
            }

            if(cmd_tokens[2].contains("Temp_")) {
                String arg1 = cmd_tokens[2];
                InterferenceGraphNode node = CheckIfNodeExists(graph, arg1);
                if(node == null) {
                    System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.exit(0);
                }
                new_cmd[2] = "$t" + node.color;
            }
            else {
                new_cmd[2] = cmd_tokens[2];
            }
            return String.join(" ", new_cmd);
        }
        else if(cmd_tokens[0].equals("lw") || cmd_tokens[0].equals("lb") || cmd_tokens[0].equals("sw") || cmd_tokens[0].equals("sb")) {
            String[] new_cmd = new String[3];
            new_cmd[0] = "\t" + cmd_tokens[0];
            if(cmd_tokens[1].contains("Temp_")) {
                String dest = cmd_tokens[1].substring(0, cmd_tokens[1].length() - 1);
                InterferenceGraphNode node = CheckIfNodeExists(graph, dest);
                if(node == null) {
                    System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.exit(0);
                }
                new_cmd[1] = "$t" + node.color + ",";
            }
            else {
                new_cmd[1] = cmd_tokens[1];
            }

            if(cmd_tokens[2].contains("Temp_")) {
                int index = cmd_tokens[2].indexOf("Temp_");
                String arg1 = cmd_tokens[2].substring(index, cmd_tokens[2].length() - 1);
                InterferenceGraphNode node = CheckIfNodeExists(graph, arg1);
                if(node == null) {
                    System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.exit(0);
                }
                new_cmd[2] = cmd_tokens[2].replace(arg1, "$t" + node.color);
            }
            else {
                new_cmd[2] = cmd_tokens[2];
            }
            return String.join(" ", new_cmd);
        }
        else if(cmd_tokens[0].equals("blt") || cmd_tokens[0].equals("bge") || cmd_tokens[0].equals("bne")
                || cmd_tokens[0].equals("beq") || cmd_tokens[0].equals("ble") || cmd_tokens[0].equals("bgt")) {
            String[] new_cmd = new String[4];
            new_cmd[0] = "\t" + cmd_tokens[0];

            new_cmd[3] = cmd_tokens[3];
            if(cmd_tokens[1].contains("Temp_")) {
                String arg1 = cmd_tokens[1].substring(0, cmd_tokens[1].length() - 1);
                InterferenceGraphNode node = CheckIfNodeExists(graph, arg1);
                if(node == null) {
                    System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.exit(0);
                }
                new_cmd[1] = "$t" + node.color + ",";
            }
            else {
                new_cmd[1] = cmd_tokens[1];
            }

            if(cmd_tokens[2].contains("Temp_")) {
                String arg2 = cmd_tokens[2].substring(0, cmd_tokens[2].length() - 1);
                InterferenceGraphNode node = CheckIfNodeExists(graph, arg2);
                if(node == null) {
                    System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.exit(0);
                }
                new_cmd[2] = "$t" + node.color + ",";
            }
            else {
                new_cmd[2] = cmd_tokens[2];
            }
            return String.join(" ", new_cmd);
        }
        else if(cmd_tokens[0].equals("bltz")) {
            String[] new_cmd = new String[3];
            new_cmd[0] = "\t" + cmd_tokens[0];
            new_cmd[2] = cmd_tokens[2];
            if(cmd_tokens[1].contains("Temp_")) {
                String arg1 = cmd_tokens[1].substring(0, cmd_tokens[1].length() - 1);
                InterferenceGraphNode node = CheckIfNodeExists(graph, arg1);
                if(node == null) {
                    System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.exit(0);
                }
                new_cmd[1] = "$t" + node.color + ",";
            }
            else {
                new_cmd[1] = cmd_tokens[1];
            }
            return String.join(" ", new_cmd);
        }
        return cmd;
    }
}