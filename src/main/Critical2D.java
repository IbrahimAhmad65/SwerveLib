package main;

import java.util.ArrayList;
import java.util.function.Supplier;

public class Critical2D {
    private Vector2D[] pos;
    private Vector2D[] vel;
    private JacobianDiscrete2D jacobianDiscrete2D;
    Supplier<Matrix> ms = () -> {
        return new Matrix(0, 0);
    };
    private int head;

    public Critical2D(Vector2D[] pos, Vector2D[] vel) {
        this.pos = pos;
        this.vel = vel;
        head = 0;
        jacobianDiscrete2D = new JacobianDiscrete2D(ms);
    }

    public Critical2D add(Vector2D posT, Vector2D velT) {
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
            if (jacobianDiscrete2D.getCritical(1e-3)) {
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

    public static Vector2D[] cleanCrit(Vector2D[] crits, double error) {
        boolean exit = true;
        ArrayList<Vector2D> k = new ArrayList<Vector2D>();
        k.add(crits[0]);
        for (Vector2D v : crits) {
            for (Vector2D j : k) {
                if (v.clone().add(j.clone().scale(-1)).getMagnitude() < error) {
                } else {
                    k.add(v);
                    break;
                }
                System.out.println(k);
            }
        }
        return arrayListToArr(k);
    }


    private static boolean arrayClean(Vector2D[] crits, double error) {

        for (Vector2D v : crits) {
            for (Vector2D j : crits) {
                if (v.clone().add(j.clone().scale(-1)).getMagnitude() < error) {
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<Vector2D> arrayListCopy(ArrayList<Vector2D> v) {
        ArrayList<Vector2D> out = new ArrayList<Vector2D>();
        for (Vector2D j : v) {
            out.add(j);
        }
        return out;
    }

    public static Vector2D[] arrayListToArr(ArrayList<Vector2D> j) {
        Vector2D[] out = new Vector2D[j.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = (Vector2D) j.toArray()[i];
        }
        return out;
    }

    public static void main(String[] args) {
        Vector2D[] vel = new Vector2D[1000];
        Vector2D[] pos = new Vector2D[vel.length];
        double h = 1.0 / vel.length;
        Function<Vector2D, Double> f = (x) -> {
            return new Vector2D(Math.sin(x), x * Math.sin(x));
        };
        Function<Vector2D, Double> fPrime = (x) -> {
            return new Vector2D(Math.cos(x), Math.sin(x) + x * Math.cos(x));
        };
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

        }
        Critical2D critical2D = new Critical2D(pos, vel);
        Vector2D[] b = critical2D.findConcavity();
        System.out.println("now its concave time");
        for (Vector2D j : b) {
            System.out.println(j);
        }
        System.out.println("its cool person time");
        b = Critical2D.cleanCrit(b, .05);
        for (Vector2D j : b) {
            System.out.println(j);
        }
    }
}
