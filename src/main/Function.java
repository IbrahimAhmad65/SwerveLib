package main;

public interface Function<W,T> {
    abstract W  compute(T x);
}
