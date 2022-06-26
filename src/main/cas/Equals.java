package main.cas;

public class Equals implements Blob {

    Blobulus a;
    Blobulus c;

    Blob b;

    Equals(Blobulus a, Blobulus c) {
        this.a = a;
        this.c = c;
        this.b = this;
    }

    public Equals addOnBothSides(Blob o) {
        this.a = new Blobulus(new Add(a.peel()[0], o));
        this.c = new Blobulus(new Add(c.peel()[0], o));
        return this;
    }

    public Equals multiply(Blob o) {
        this.a = new Blobulus(new Multiply(a.peel()[0], o));
        this.c = new Blobulus(new Multiply(c.peel()[0], o));
        return this;
    }

    public Equals lowerPower(Blob o) {
        this.a = new Blobulus(new Power(a.peel()[0], o));
        this.c = new Blobulus(new Power(c.peel()[0], o));
        return this;
    }

    public Equals upperPower(Blob o) {
        this.a = new Blobulus(new Power(o,a.peel()[0]));
        this.c = new Blobulus(new Power(o, c.peel()[0]));
        return this;
    }

    public Equals negate() {
        this.a = new Blobulus(new Negate(a.peel()[0]));
        this.c = new Blobulus(new Negate(c.peel()[0]));
        return this;
    }

    public Equals functionSingleArg(FunctionCAS z) {
        this.a = new Blobulus(z.computeSingle(a.peel()[0]));
        this.c = new Blobulus(z.computeSingle(c.peel()[0]));
        return this;
    }

    public Equals functionMultiArg(FunctionCAS z, Blob[] otherArgs) {
        this.a = new Blobulus(z.computeMulti(a.peel()[0],otherArgs));
        this.c = new Blobulus(z.computeMulti(c.peel()[0],otherArgs));
        return this;
    }

    public Equals flip() {
        this.a = new Blobulus(new Reciprocal(a.peel()[0]));
        this.c = new Blobulus(new Reciprocal(c.peel()[0]));
        return this;
    }

    @Override
    public Blob getInstance() {
        return b;
    }

    @Override
    public Blob[] peel() {
        return new Blob[]{a, c};
    }

    @Override
    public void replace(Blob replacand, Blob replacer) {
        a.replace(replacand, replacer);
        c.replace(replacand, replacer);
    }

    @Override
    public void cascade() {
        a.cascade();
        c.cascade();
    }
}
