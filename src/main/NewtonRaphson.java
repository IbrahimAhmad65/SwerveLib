package main;

public class NewtonRaphson {

    public NewtonRaphson() {
//        this.f = f;
//        this.fPrime = fPrime;
    }

    public static double approximate(Function<Double, Double> f, Function<Double, Double> fPrime, double initial, int iterations) {
        double current = initial;
        for (int i = 0; i < iterations; i++) {
            current = current - ((Double) f.compute(current)).doubleValue() / ((Double) fPrime.compute(current)).doubleValue();
        }
        return current;
    }

    public static double smartApproximate(Function<Double, Double> f, Function<Double, Double> fPrime, double initial, int iterations, Function<Boolean, Double> threshold) {
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

    // Move all vars to left hand side, ie f and f prime righthand side is a changing number, use for faster runtime bc no reconstructing functions
    public static double solve(Function<Double, Double> f, Function<Double, Double> fPrime, double initial, double iterations, double rightHandSide) {
        double current = initial;
        for (int i = 0; i < iterations; i++) {
            current = current - (((Double) f.compute(current)).doubleValue() - rightHandSide) / ((Double) fPrime.compute(current)).doubleValue();
        }
        return current;
    }

    public static double smartSolve(Function<Double, Double> f, Function<Double, Double> fPrime, double initial, double iterations, double rightHandSide, Function<Boolean, Double> threshold) {
        double current = initial;
        double oldCurrent = 0;
        for (int i = 0; i < iterations; i++) {
            oldCurrent = current;
            current = current - (((Double) f.compute(current)).doubleValue() - rightHandSide) / ((Double) fPrime.compute(current)).doubleValue();
            if (threshold.compute(current - oldCurrent) && (i + 1) < iterations) {
                System.out.println("Premature Exiting on Newtons Method at iterations= " + (i + 1) + " Target Iterations= " + iterations);
                break;
            }
        }
        return current;
    }


    public static void main(String[] args) {
        Function<Double, Double> f2 = (x) -> {
            return (x * Math.pow(2.718, x) - 16);
        };

        Function<Double, Double> f = (x) -> {
            return (x * Math.pow(2.718, x));
        };
        Function<Double, Double> fPrime = (x) -> {
            return (x * Math.pow(2.718, x) + Math.pow(2.718, x));
        };
//        NewNewton n = new NewNewton();
        System.out.println(NewtonRaphson.approximate(f2, fPrime, 1, 12));
        System.out.println(NewtonRaphson.solve(f, fPrime, 1, 12, 16));
        System.out.println(NewtonRaphson.smartSolve(f, fPrime, 1, 12, 16, (x) -> {
            return (Math.abs(x) < .0001);
        }));
    }
}
