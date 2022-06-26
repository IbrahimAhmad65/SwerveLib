package main;

import java.util.concurrent.atomic.AtomicInteger;

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

    // broken no use
    public static Vector rk4System(double X, double initialX, double iterations, Vector initialConditions, FunctionNArgs... functions) {
        if (true) {
            throw new RuntimeException("This method bad lol");
        }
        Matrix k1, k2, k3, k4;
        k1 = new Matrix(1, initialConditions.getDimensions());
        k2 = new Matrix(1, initialConditions.getDimensions());
        k3 = new Matrix(1, initialConditions.getDimensions());
        k4 = new Matrix(1, initialConditions.getDimensions());
        Vector vars = new Vector(initialConditions.getDimensions() + 1, Vector.DimensionError.ZERO);
        System.arraycopy(initialConditions.getData(), 0, vars.getData(), 1, initialConditions.getData().length);

        vars.getData()[0] = initialX;
        System.out.println(vars.getData().length + " " + functions.length);
        double h = (X - initialX) / (iterations);
        for (int i = 0; i < iterations; i++) {
//            k2.fill(0);
//            k3.fill(0);
//            k4.fill(0);
            for (int j = 0; j < functions.length; j++) {
                k1.getData()[0][j] = (double) functions[j].compute(vars.getDataDouble());
            }

            for (int j = 0; j < functions.length; j++) {
                k2.getData()[0][j] = (double) functions[j].compute(Matrix.doubleToDouble(k1.clone().scale(h / 2).add(new Matrix(new double[][]{vars.getData()})).getData()[0]));
            }

            for (int j = 0; j < functions.length; j++) {
                k3.getData()[0][j] = (double) functions[j].compute(Matrix.doubleToDouble(k2.clone().scale(h / 2).add(new Matrix(new double[][]{vars.getData()})).getData()[0]));
            }
            for (int j = 0; j < functions.length; j++) {
                k4.getData()[0][j] = (double) functions[j].compute(Matrix.doubleToDouble(k3.clone().scale(h).add(new Matrix(new double[][]{vars.getData()})).getData()[0]));
            }
//
//            k2 = f1Prime.compute(x + h / 2, y + h * k1 / 2, z + h * l1 / 2, o + h * m1 / 2);
//            l2 = f2Prime.compute(x + h / 2, y + h * k1 / 2, z + h * l1 / 2, o + h * m1 / 2);
//            m2 = f3Prime.compute(x + h / 2, y + h * k1 / 2, z + h * l1 / 2, o + h * m1 / 2);
//
//            k3 = f1Prime.compute(x + h / 2, y + h * k2 / 2, z + h * l2 / 2, o + h * m2 / 2);
//            l3 = f2Prime.compute(x + h / 2, y + h * k2 / 2, z + h * l2 / 2, o + h * m2 / 2);
//            m3 = f3Prime.compute(x + h / 2, y + h * k2 / 2, z + h * l2 / 2, o + h * m2 / 2);
//
//
//            k4 = f1Prime.compute(x + h, y + h * k3, z + h * l3, o + h * m3);
//            l4 = f2Prime.compute(x + h, y + h * k3, z + h * l3, o + h * m3);
//            m4 = f3Prime.compute(x + h, y + h * k3, z + h * l3, o + h * m3);
//
//
//            y += 1.0 / 6 * h * (k1 + 2 * k2 + 2 * k3 + k4);
//            z += 1.0 / 6 * h * (l1 + 2 * l2 + 2 * l3 + l4);
//            o += 1.0 / 6 * h * (m1 + 2 * m2 + 2 * m3 + m4);
            for (int j = 1; j < functions.length + 1; j++) {
                vars.getData()[j] += 1 / 6.0 * h * (k1.getData()[0][j - 1] + 2 * k2.getData()[0][j - 1] + 2 * k3.getData()[0][j - 1] + k4.getData()[0][j - 1]);
            }
            vars.getData()[0] += h;
        }
        return vars;
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

    public static double[] rk4System2(Function3Args<Double, Double> f1Prime, Function3Args<Double, Double> f2Prime, int iterations, double givenX, double givenY, double X, double givenZ) {
        double k1, k2, k3, k4, y = givenY, x = givenX, h;
        double l1, l2, l3, l4, z = givenZ;
        h = (X - givenX) / (iterations);
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

    // to lazy and tired to figure out
//    public static Vector rk4System(Vector initialConditions, double X,double iterations, FunctionNargs<Double,Double>... fPrimes){
//        if(initialConditions.getDimensions() != fPrimes.length+1){
//            //failpoint, currently going to throw, but could also fail silently if needed
//            throw new IllegalArgumentException("Number of initial conditions does not match equations");
//        }
//        FunctionNargs<Double,Double>[] newFPrimes = new FunctionNargs[fPrimes.length+1];
//        newFPrimes[0] = (j)->{return Double.valueOf(1);};
//        for(int i = 1; i < newFPrimes.length; i++){
//            newFPrimes[i] = fPrimes[i-1];
//        }
//        Vector k1 = new Vector(Vector.DimensionError.ZERO,new double[fPrimes.length]);
//        Vector k2 = new Vector(Vector.DimensionError.ZERO,new double[fPrimes.length]);
//        Vector k3 = new Vector(Vector.DimensionError.ZERO,new double[fPrimes.length]);
//        Vector k4 = new Vector(Vector.DimensionError.ZERO,new double[fPrimes.length]);
//        Vector vars = new Vector(Vector.DimensionError.ZERO,initialConditions.getData());
//        double h = (X - initialConditions.getData()[0]) / iterations;
//        for (int i = 0; i < iterations;i++){
////            k1.fill(0);
////            k2.fill(0);
////            k3.fill(0);
////            k4.fill(0);
//            for(int j =0; j < fPrimes.length; j++){
//                k1.getData()[j] = fPrimes[j].compute(vars.getDataDouble());
//            }
//            for(int j =0; j < fPrimes.length; j++){
//                k2.getData()[j] = fPrimes[j].compute(vars.selflessOperate(h*k1.getData()[j]/2,(x,y)->{return x+y;}).getDataDouble());
//            }
//            for(int j =0; j < fPrimes.length; j++){
//                k3.getData()[j] = fPrimes[j].compute(vars.selflessOperate(h*k2.getData()[j]/2,(x,y)->{return x+y;}).getDataDouble());
//            }
//            for(int j =0; j < fPrimes.length; j++){
//                k4.getData()[j] = fPrimes[j].compute(vars.selflessOperate(h*k1.getData()[j],(x,y)->{return x+y;}).getDataDouble());
//            }
//            vars.getData()[0] += h;
//            for(int j =1; j < fPrimes.length; j++){
//                vars.getData()[j] += 1.0 / 6 * h * (k1.getData()[j] + 2 * k2.getData()[j] + 2 * k3.getData()[j] + k4.getData()[j]);;
//            }
//        }
//        return vars;
//    }

    public static double[] rk4System3(Function4Args<Double, Double> f1Prime, Function4Args<Double, Double> f2Prime, Function4Args<Double, Double> f3Prime, int iterations, double givenX, double givenY, double X, double givenZ, double givenO) {
        double k1, k2, k3, k4, y = givenY, x = givenX, h;
        double l1, l2, l3, l4, z = givenZ;
        double m1, m2, m3, m4, o = givenO;
        h = (X - givenX) / (iterations);
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

    public static double[] rk4System4(Function5Args<Double, Double> f1Prime, Function5Args<Double, Double> f2Prime, Function5Args<Double, Double> f3Prime, Function5Args<Double, Double> f4Prime, int iterations, double givenX, double givenY, double X, double givenZ, double givenO, double givenP) {
        double k1, k2, k3, k4, y = givenY, x = givenX, h;
        double l1, l2, l3, l4, z = givenZ;
        double m1, m2, m3, m4, o = givenO;
        double n1, n2, n3, n4, p = givenP;
        h = (X - givenX) / (iterations);
        for (int i = 0; i < iterations; i++) {
            k1 = f1Prime.compute(x, y, z, o, p);
            l1 = f2Prime.compute(x, y, z, o, p);
            m1 = f3Prime.compute(x, y, z, o, p);
            n1 = f4Prime.compute(x, y, z, o, p);

            k2 = f1Prime.compute(x + h / 2, y + h * k1 / 2, z + h * l1 / 2, o + h * m1 / 2, p + h * n1 / 2);
            l2 = f2Prime.compute(x + h / 2, y + h * k1 / 2, z + h * l1 / 2, o + h * m1 / 2, p + h * n1 / 2);
            m2 = f3Prime.compute(x + h / 2, y + h * k1 / 2, z + h * l1 / 2, o + h * m1 / 2, p + h * n1 / 2);
            n2 = f4Prime.compute(x + h / 2, y + h * k1 / 2, z + h * l1 / 2, o + h * m1 / 2, p + h * n1 / 2);


            k3 = f1Prime.compute(x + h / 2, y + h * k2 / 2, z + h * l2 / 2, o + h * m2 / 2, p + h * n2 / 2);
            l3 = f2Prime.compute(x + h / 2, y + h * k2 / 2, z + h * l2 / 2, o + h * m2 / 2, p + h * n2 / 2);
            m3 = f3Prime.compute(x + h / 2, y + h * k2 / 2, z + h * l2 / 2, o + h * m2 / 2, p + h * n2 / 2);
            n3 = f4Prime.compute(x + h / 2, y + h * k2 / 2, z + h * l2 / 2, o + h * m2 / 2, p + h * n2 / 2);


            k4 = f1Prime.compute(x + h, y + h * k3, z + h * l3, o + h * m3, p + h * n3);
            l4 = f2Prime.compute(x + h, y + h * k3, z + h * l3, o + h * m3, p + h * n3);
            m4 = f3Prime.compute(x + h, y + h * k3, z + h * l3, o + h * m3, p + h * n3);
            n4 = f4Prime.compute(x + h, y + h * k3, z + h * l3, o + h * m3, p + h * n3);


            y += 1.0 / 6 * h * (k1 + 2 * k2 + 2 * k3 + k4);
            z += 1.0 / 6 * h * (l1 + 2 * l2 + 2 * l3 + l4);
            o += 1.0 / 6 * h * (m1 + 2 * m2 + 2 * m3 + m4);
            p += 1.0 / 6 * h * (n1 + 2 * n2 + 2 * n3 + n4);

            x += h;
        }
        return new double[]{y, z, o, p};
    }


    public static void main(String[] args) {
        double g = 9.81;
        double radius = .33;
        double airDensity = 1.225;
//        }
        double s = 4.0/3 * radius * radius *radius * Math.PI * airDensity/.27;
        double cd = .47;
        double mu = .5 * airDensity * cd * Math.PI * radius * radius/.27;
//        double s = 0;
        //        System.out.println(mu);
//        System.out.println(s);

//        System.out.println("y>=2.62");
//        System.out.println("x=2");


//        System.out.println("(2.4026,2.2999)");
//        for (int j = 0; j < 400; j++) {
//
//            double[] b = DiffEq.rk4System4((t,x, y, vx, vy) -> {
//                return vx;
//            },(t,x, y, vx, vy) -> {
//                return vy;
//            }, (t,x, y, vx, vy) -> {
//                Vector3D vitr = new Vector3D(vx, vy, 0);
//                double sqrt = Math.sqrt(vx * vx + vy * vy);
//                Vector3D omegaV = new Vector3D(0, 0, omega);
//                return -(mu * vx * sqrt - s * vitr.crossProduct(omegaV).project(Vector3D.getI()).getRadius() * omega / Math.abs(omega)) / vx;
//            }, (t,x, y, vx, vy) -> {
//                Vector3D vitr = new Vector3D(vx,vy, 0);
//                double sqrt = Math.sqrt(vx * vx + vy * vy);
//                Vector3D omegaV = new Vector3D(0, 0, omega);
//                return -( g+ mu * vy * sqrt - s * vitr.crossProduct(omegaV).project(Vector3D.getJ()).getRadius() * omega / Math.abs(omega)) / vx;
//            }, 55, 2, 2.5, j*.01, 2, 3,-3);
//            System.out.println("(" + (b[0]) + "," + b[1] + ")");
//        }

        int xCheck;
        for (int j = 0; j < 100; j++) {
            double omega = 3;

            int finalJ = j;
            xCheck = 0;
            AtomicInteger finalXCheck = new AtomicInteger(xCheck);
            double[] b = DiffEq.rk4System3((x, y, vx, vy) -> {
                return vy / vx;
            }, (x, y, vx, vy) -> {
                Vector3D vitr = new Vector3D(0, vx, 0);
                double sqrt = Math.sqrt(vx * vx + vy * vy);
                Vector3D omegaV = new Vector3D(0, 0, omega);
//                finalXCheck.getAndIncrement();
//                if(finalXCheck.get() == 10)
//                System.out.println("(" + (finalJ *.04) + "," + s * vitr.crossProduct(omegaV).project(Vector3D.getI()).getRadius() * omega / Math.abs(omega) + ")");
                return (-mu * vx * sqrt + s * vitr.crossProduct(omegaV).getRadius() * omega / Math.abs(omega)) / vx;
            }, (x, y, vx, vy) -> {
                Vector3D vitr = new Vector3D(vy  ,0, 0);
                double sqrt = Math.sqrt(vx * vx + vy * vy);
                Vector3D omegaV = new Vector3D(0, 0, omega);
//                if(finalXCheck.get() == 10)
//                    System.out.println("(" + (finalJ *.04) + "," + s * vitr.crossProduct(omegaV).project(Vector3D.getJ()).getRadius() * omega / Math.abs(omega) + ")");
                return ( -g -  mu * vy * sqrt + s * vitr.crossProduct(omegaV).getRadius() * omega / Math.abs(omega)) / vx;
            }, 55, 2, 2.5, j*.04, 2, -3 );
//            System.out.println("(" + (j*.01) + "," + b[0] + ")");
        }


//        fmore
//        double[] b = DiffEq.rk4System3((x, y, vx, vy) -> {
//            return vy / vx;
//        }, (x, y, vx, vy) -> {
//            return (-mu * vx * Math.sqrt(vx * vx + vy * vy)) / vx;
//        }, (x, y, vx, vy) -> {
//            return (-g - mu * vy * Math.sqrt(vx * vx + vy * vy)) / vx;
//        }, 55,2.4026 , 2.2999, j*.03, 2, -3);
 }
}
