package main.cas;

public class Reciprocal implements Operation {
    Blob a;
    Blob b;

    public Reciprocal(Blob a) {
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
    public Blob operate() {
        if (a.getClass() == Constant.class) {
            return new Constant(1.0 / ((Constant) a).get());
        }
        return b;
    }
}
