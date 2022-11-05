package main.graph;

public class Edge implements Comparable{
    Node begin;
    Node end;

    public Edge(Node begin, Node end){
        this.begin = begin;
        this.end = end;
    }

    public Edge(Node[] nodes){
        begin = nodes[0];
        end = nodes[1];
    }

    public Node getEnd(){
        return end;
    }
    public  Node getBegin(){
        return begin;
    }

    public void setBegin(Node begin) {
        this.begin = begin;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    public  Node get(boolean endType){
        if(endType){
            return end;
        }else {
            return begin;
        }
    }
    public  Node[] get(){
        return new Node[]{begin, end};
    }

    public int compareTo(Object o){
        return 0;
    }
}
