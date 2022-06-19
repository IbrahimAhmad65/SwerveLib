package main.cas;

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
            if (a.getClass() == Constant.class){
                return new Constant(((Constant) a).get() + ((Constant) c).get());
            }
            if (a.equals(c)) {
                return new Multiply(new Constant(2),a);
            }
        }

        return b;
    }

    public Blob getInstance() {
        return b;
    }

    public Blob[] peel() {
        return new Blob[]{a,c};
    }
}
