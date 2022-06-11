package main;

public class Zeros {
    public static double newtonApproximate(Function<Double, Double> f, Function<Double, Double> fPrime, double initial, int iterations) {
        double current = initial;
        for (int i = 0; i < iterations; i++) {
            current = current - ((Double) f.compute(current)).doubleValue() / ((Double) fPrime.compute(current)).doubleValue();
        }
        return current;
    }

    public static double  newtonSmartApproximate(Function<Double, Double> f, Function<Double, Double> fPrime, double initial, int iterations, Function<Boolean, Double> threshold) {
        double current = initial;
        double oldCurrent = 0;
        for (int i = 0; i < iterations; i++) {
            oldCurrent = current;
            current = current - ((Double) f.compute(current)).doubleValue() / ((Double) fPrime.compute(current)).doubleValue();
            if (threshold.compute(current - oldCurrent) && (i + 1) < iterations) {
                System.out.println("Premature Exiting on Newtons Method at iterations= " + (i + 1) + " Target Iterations= " + iterations);
                break;
            }
        }
        return current;
    }
}
