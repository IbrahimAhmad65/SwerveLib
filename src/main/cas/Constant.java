package main.cas;

import java.util.Objects;

public class Constant implements Argument {

    private double number;
    Blob b;

    public Constant(double number) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constant constant = (Constant) o;
        return Double.compare(constant.number, number) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return " " + number + " ";
    }

    public void replace(Blob replacand, Blob replacer) {
        if(replacand.equals(this)){
            this.number = ((Constant) replacer).number;
        }
    }

    @Override
    public void cascade() {
    }
}
