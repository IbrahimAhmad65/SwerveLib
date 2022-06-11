package main;

public interface FunctionNargs<W,T> {
    abstract W compute(T[] args);
}
