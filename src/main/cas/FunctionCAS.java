package main.cas;

public interface FunctionCAS extends Blob{
    Blob compute(Blob... args);
}
