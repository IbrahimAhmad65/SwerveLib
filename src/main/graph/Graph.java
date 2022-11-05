package main.graph;

import java.util.HashMap;

public interface Graph {
    HashMap<Node,NodeCollection> nodeMapping = null;

    public default NodeCollection getAdjacent(Node n){
        return nodeMapping.get(n);
    }
}
