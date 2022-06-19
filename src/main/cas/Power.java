package main.cas;

public class Power implements Operation{

    private Blob a;
    private Blob c;

    private Blob b;

    public Power(Blob a, Blob b) {
        this.a = a;
        this.c = b;
        this. b = this;
    }

    public Blob operate() {
        if(a.getClass() == c.getClass()){
            if (a.getClass() == Constant.class){
                return new Constant(Math.pow(((Constant) a).get() ,((Constant) c).get()));
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
