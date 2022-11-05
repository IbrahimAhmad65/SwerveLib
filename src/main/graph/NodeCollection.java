package main.graph;

import java.util.Arrays;

public class NodeCollection {
    private Node[] nodes;
    public NodeCollection(Node... nodes){
        this.nodes = nodes;
    }

    public Node[] getNodes() {
        return nodes;
    }

    public boolean isInNode(Node n){
        for (Node z: nodes) {
            if(n.equals(z)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeCollection that = (NodeCollection) o;
        return Arrays.equals(nodes, that.nodes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(nodes);
    }
}
