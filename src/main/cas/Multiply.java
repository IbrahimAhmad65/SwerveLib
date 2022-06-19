package main.cas;

public class Multiply implements Operation{

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
            if (a.getClass() == Constant.class){
                return new Constant(((Constant) a).get() * ((Constant) c).get());
            }
            if (a.getClass() == Variable.class && ((Variable) a).getName() == ((Variable) c).getName()) {
                return new Power(a,new Constant(2));
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
