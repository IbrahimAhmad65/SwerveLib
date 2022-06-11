package main;

// not planning any implicit methods bc solving with newtons method for every iteration is worse than cranking up step size
public class DiffEq {

    public static double rk4(Function2Args<Double, Double> fPrime, int iterations, double givenX, double givenY, double X) {
        double k1, k2, k3, k4, y = givenY, x = givenX, h;
        h = (X - givenX) / (iterations);
        System.out.println(h);
        for (int i = 0; i < iterations; i++) {
            k1 = fPrime.compute(x, y);
            x += h / 2;
            k2 = fPrime.compute(x, y + h * k1 / 2);
            k3 = fPrime.compute(x, y + h * k2 / 2);
            k4 = fPrime.compute(x + h / 2, y + h * k3);
            y += 1.0 / 6 * h * (k1 + 2 * k2 + 2 * k3 + k4);
            x += h / 2;
        }
        return y;
    }

    // Really bad, rk4 is just better. cant think of when euler is better
    public static double forwardEuler(Function2Args<Double, Double> fPrime, int iterations, double givenX, double givenY, double X) {
        double y = givenY, x = givenX, h = (X - givenX) / (iterations);
        for (int i = 0; i < iterations; i++) {
            y += h * fPrime.compute(x, y);
            x += h;
        }
        return y;
    }

    public static double[] rk4System2(Function3Args<Double, Double> f1Prime, Function3Args<Double, Double> f2Prime,
                                      int iterations, double givenX, double givenY, double X, double givenZ) {
        double k1, k2, k3, k4, y = givenY, x = givenX, h;
        double l1, l2, l3, l4, z = givenZ;
        h = (X - givenX) / (iterations);
        System.out.println(h);
        for (int i = 0; i < iterations; i++) {
            k1 = f1Prime.compute(x, y, z);
            l1 = f2Prime.compute(x, y, z);

            k2 = f1Prime.compute(x + h / 2, y + h * k1 / 2, z + h * l1 / 2);
            l2 = f2Prime.compute(x + h / 2, y + h * k1 / 2, z + h * l1 / 2);

            k3 = f1Prime.compute(x + h / 2, y + h * k2 / 2, z + h * l2 / 2);
            l3 = f2Prime.compute(x + h / 2, y + h * k2 / 2, z + h * l2 / 2);

            k4 = f1Prime.compute(x + h, y + h * k3, z + h * l3);
            l4 = f2Prime.compute(x + h, y + h * k3, z + h * l3);


            y += 1.0 / 6 * h * (k1 + 2 * k2 + 2 * k3 + k4);
            z += 1.0 / 6 * h * (l1 + 2 * l2 + 2 * l3 + l4);
            x += h;
        }
        return new double[]{y, x};
    }

    public static double[] rk4System3(Function4Args<Double, Double> f1Prime, Function4Args<Double, Double> f2Prime, Function4Args<Double, Double> f3Prime,
                                      int iterations, double givenX, double givenY, double X, double givenZ, double givenO) {
        double k1, k2, k3, k4, y = givenY, x = givenX, h;
        double l1, l2, l3, l4, z = givenZ;
        double m1, m2, m3, m4, o = givenO;
        h = (X - givenX) / (iterations);
        System.out.println(h);
        for (int i = 0; i < iterations; i++) {
            k1 = f1Prime.compute(x, y, z, o);
            l1 = f2Prime.compute(x, y, z, o);
            m1 = f3Prime.compute(x, y, z, o);

            k2 = f1Prime.compute(x + h / 2, y + h * k1 / 2, z + h * l1 / 2, o + h * m1 / 2);
            l2 = f2Prime.compute(x + h / 2, y + h * k1 / 2, z + h * l1 / 2, o + h * m1 / 2);
            m2 = f3Prime.compute(x + h / 2, y + h * k1 / 2, z + h * l1 / 2, o + h * m1 / 2);

            k3 = f1Prime.compute(x + h / 2, y + h * k2 / 2, z + h * l2 / 2, o + h * m2 / 2);
            l3 = f2Prime.compute(x + h / 2, y + h * k2 / 2, z + h * l2 / 2, o + h * m2 / 2);
            m3 = f3Prime.compute(x + h / 2, y + h * k2 / 2, z + h * l2 / 2, o + h * m2 / 2);


            k4 = f1Prime.compute(x + h, y + h * k3, z + h * l3, o + h * m3);
            l4 = f2Prime.compute(x + h, y + h * k3, z + h * l3, o + h * m3);
            m4 = f3Prime.compute(x + h, y + h * k3, z + h * l3, o + h * m3);


            y += 1.0 / 6 * h * (k1 + 2 * k2 + 2 * k3 + k4);
            z += 1.0 / 6 * h * (l1 + 2 * l2 + 2 * l3 + l4);
            o += 1.0 / 6 * h * (m1 + 2 * m2 + 2 * m3 + m4);

            x += h;
        }
        return new double[]{y, z, o};
    }

    public static void main(String[] args) {
        double[] b = DiffEq.rk4System3((x, y, z, o) -> {
            return -1 / 2.0 * y;
        }, (x, y, z, o) -> {
            return 1 / 2.0 * y - 1 / 4.0 * z;
        }, (x, y, z, o) -> {
            return 1 / 4.0 * z - 1 / 6.0 * o;
        }, 99999, 0, 5, 5, 6, 8);
        System.out.println(b[0] + " " + b[1] + " " + b[2]);
    }
}
