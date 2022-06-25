package main.cas;

public interface Argument extends Blob {

    boolean isConstant();

    Argument derivative(Argument d);

    boolean isIntegralDefined(Argument d);

    Argument integral(Argument d);


}
