package main.cas;

public class Blobulus implements Blob {
    Blob a;
    Blob b;

    public Blobulus(Blob a) {
        this.a = a;
        this.b = this;
    }

    @Override
    public Blob getInstance() {
        return b;
    }

    @Override
    public Blob[] peel() {
        return new Blob[]{a};
    }

    @Override
    public void replace(Blob replacand, Blob replacer) {
        if (replacand.equals(a)) {
            a = replacer;
        }
        a.replace(replacand,replacer);
    }

    @Override
    public void cascade() {
        a.cascade();
        if (a.getClass().getInterfaces()[0] == Operation.class) {
            a = ((Operation) a).operate();
        }
    }

    @Override
    public String toString() {
        return "" + a;
    }
}
