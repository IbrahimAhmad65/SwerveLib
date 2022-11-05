package main.graph;

public class Node<T>{
    private T begin;
    private T end;

    public Node(T begin, T end){
        this.begin = begin;
        this.end = end;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != getClass()){
            return false;
        }
        return (begin.equals(((Node)obj).getBegin())) && (end.equals(((Node)obj).getEnd()));
    }

    public T getBegin() {
        return begin;
    }

    public T getEnd() {
        return end;
    }
}
