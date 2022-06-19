package main.cas;

public class Constant implements Argument{

    private double number;
    Blob b;
    public Constant(double number){
        this.number = number;
        b = this;
    }

    public boolean isConstant() {
        return true;
    }

    public Argument derivative(Argument d) {
        return new Constant(0);
    }

    public boolean isIntegralDefined(Argument d) {
        return true;
    }

    public boolean isIntegralDefined() {
        return true;
    }

    public Argument integral(Argument d) {
        return d;
    }

    public double get() {
        return number;
    }

    public Blob getInstance() {
        return b;
    }

    public Blob[] peel() {
        return new Blob[]{b};
    }
}
