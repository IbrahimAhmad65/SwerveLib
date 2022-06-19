package main.cas;

public class Negate implements Operation{

    Blob a;
    Blob b;
    public Negate(Blob a){
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
            if (a.getClass() == Constant.class){
                return new Constant(- ((Constant) a).get());
            }
        return b;
    }
}
