package main.cas;

import java.util.Objects;

public class Multiply implements Operation {

    private Blob a;
    private Blob c;

    private Blob b;

    public Multiply(Blob a, Blob c) {
        this.a = a;
        this.c = c;
        this.b = this;
    }

    public Blob operate() {
        if (a.getClass() == c.getClass()) {
            if (a.getClass() == Constant.class) {
                return new Constant(((Constant) a).get() * ((Constant) c).get());
            }
            if (a.getClass() == Variable.class && ((Variable) a).getName() == ((Variable) c).getName()) {
                return new Power(a, new Constant(2));
            }
        }

        if (a.getClass() == Reciprocal.class) {
            if (a.peel()[0].equals(b)) {
                return new Constant(1);
            }
        }
        if (b.getClass() == Reciprocal.class) {
            if (b.peel()[0].equals(a)) {
                return new Constant(1);
            }
        }

        return b;
    }

    public Blob getInstance() {
        return b;
    }

    public Blob[] peel() {
        return new Blob[]{a, c};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Multiply multiply = (Multiply) o;
        return a.equals(multiply.a) && c.equals(multiply.c) || a.equals(multiply.c) && c.equals(multiply.a);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, c);
    }

    @Override
    public String toString() {
        return "("+ a.toString() +" * "+ c.toString()+")";
    }
    public void replace(Blob replacand, Blob replacer) {
        if(a.equals(replacand)){
            a = replacer;
        }
        if(c.equals(replacand)){
            c = replacer;
        }
        a.replace(replacand,replacer);
        c.replace(replacand,replacer);
        this.operate();
    }

    @Override
    public void cascade() {
        a.cascade();
        c.cascade();
        if(c.getClass().getInterfaces()[0] == Operation.class){
            c = ((Operation) c).operate();
        }
        if(a.getClass().getInterfaces()[0] == Operation.class){
            a = ((Operation) a).operate();
        }

    }
}
