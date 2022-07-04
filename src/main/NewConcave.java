package main;

import java.util.ArrayList;
import java.util.function.Supplier;

public class NewConcave {
    private Vector2D[] pos;
    private Vector2D[] vel;
    private JacobianDiscrete2D jacobianDiscrete2D;
    Supplier<Matrix> ms = () -> {
        return new Matrix(0, 0);
    };
    private int head;

    public NewConcave(Vector2D[] pos, Vector2D[] vel) {
        this.pos = pos;
        this.vel = vel;
        head = 0;
        jacobianDiscrete2D = new JacobianDiscrete2D(ms);
    }

    public NewConcave add(Vector2D posT, Vector2D velT) {
        pos[head] = posT;
        vel[head] = velT;
        head++;
        head %= pos.length;
        return this;
    }

    public Vector2D[] findConcavity() {
        ArrayList<Vector2D> v = new ArrayList<Vector2D>();
        for (int i = 0; i < pos.length; i++) {
            int finalI = i;
            ms = () -> {
                Matrix m = new Matrix(new double[][]{{pos[finalI].getX(), vel[finalI].getX()}, {pos[finalI].getY(), vel[finalI].getY()}});
                return m;
            };
            jacobianDiscrete2D.setSupplier(ms);
            if (jacobianDiscrete2D.getCritical()) {
                v.add(pos[i]);
//                System.out.println("eyo?");
                System.out.println(vel[i]);
            }
        }
        Vector2D[] l = new Vector2D[v.size()];
        for (int i = 0; i < v.size(); i++) {
            l[i] = (Vector2D) v.toArray()[i];
        }
        return l;
    }

    public static void main(String[] args) {
        Vector2D[] vel = new Vector2D[1000];
        Vector2D[] pos = new Vector2D[vel.length];
        double h = 1.0 / vel.length;
        Function<Vector2D,Double> f = (x)->{return new Vector2D(Math.sin(x),x*Math.sin(x));};
        Function<Vector2D,Double> fPrime = (x)->{return new Vector2D(Math.cos(x),Math.sin(x) + x * Math.cos(x));};
//        Function<Vector2D, Double> f = (x) -> {
//            return new Vector2D(x*x, x);
//        };
//        Function<Vector2D, Double> fPrime = (x) -> {
//            return new Vector2D(2*x, 1);
//        };
        double count = 1.5;
        for (int i = 0; i < vel.length; i++) {
            vel[i] = fPrime.compute(count);
            pos[i] = f.compute(count);
            count += h;
//            System.out.println(vel[i]);
//            System.out.println(pos[i]);
        }
        NewConcave newConcave = new NewConcave(pos, vel);
        Vector2D[] b = newConcave.findConcavity();
        System.out.println("now its concave time");
        for (Vector2D j : b) {
            System.out.println(j);
        }
    }
}
