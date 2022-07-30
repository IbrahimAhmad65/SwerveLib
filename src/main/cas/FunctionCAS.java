package main.cas;

public interface FunctionCAS extends Blob {
    Blob computeSingle(Blob arg);

    Blob computeMulti(Blob arg, Blob[] otherArgs);
}
