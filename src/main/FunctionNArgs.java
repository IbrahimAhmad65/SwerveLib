package main;

public interface FunctionNArgs<W,T> {
    abstract W compute(T[] args);
}
