package main.cas;

import java.util.Objects;

public class Add implements Operation {

    private Blob a;
    private Blob c;

    private Blob b;

    public Add(Blob a, Blob b) {
        this.a = a;
        this.c = b;
        this.b = this;
    }

    public Blob operate() {
        if (a.getClass() == c.getClass()) {
            if (a.getClass() == Constant.class) {
                return new Constant(((Constant) a).get() + ((Constant) c).get());
            }
            if (a.equals(c)) {
                return new Multiply(new Constant(2), a);
            }
            if (a.getClass() == Negate.class) {
                if (a.peel()[0].equals(b)) {
                    return new Negate(new Multiply(new Constant(2), b));
                }
            }
            if (b.getClass() == Negate.class) {
                if (b.peel()[0].equals(a)) {
                    return new Negate(new Multiply(new Constant(2), a));
                }

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
        Add add = (Add) o;
        return a.equals(add.a) && c.equals(add.c) || a.equals(add.c) && c.equals(add.a);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, c);
    }

    @Override
    public String toString() {
        return"(" + a.toString() + " + " + c.toString() + ")";
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
