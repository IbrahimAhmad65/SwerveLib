package main.cas;

import java.util.Objects;

public class Power implements Operation {

    private Blob a;
    private Blob c;

    private Blob b;

    public Power(Blob a, Blob b) {
        this.a = a;
        this.c = b;
        this.b = this;
    }

    public Blob operate() {
        if (a.getClass() == c.getClass()) {
            if (a.getClass() == Constant.class) {
                return new Constant(Math.pow(((Constant) a).get(), ((Constant) c).get()));
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
        Power power = (Power) o;
        return a.equals(power.a) && c.equals(power.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, c);
    }
}
