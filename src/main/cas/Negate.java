package main.cas;

public class Negate implements Operation{

    Blob a;
    Blob b;

    public Negate(Blob a) {
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
            return new Constant(-((Constant) a).get());
        }
        return b;
    }

    @Override
    public String toString() {
        return " - " + a.toString();
    }

    public void replace(Blob replacand, Blob replacer) {
        if(a.equals(replacand)){
            a = replacer;
        }
        a.replace(replacand,replacer);
        this.operate();
    }

    @Override
    public void cascade() {
        a.cascade();
        if(a.getClass().getInterfaces()[0] == Operation.class){
            a = ((Operation) a).operate();
        }
    }
}
