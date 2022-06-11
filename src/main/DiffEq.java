package main;

// not planning any implicit methods bc solving with newtons method for every iteration is worse than cranking up step size
public class DiffEq {

    public static double rk4(Function2Args<Double, Double> fPrime, int iterations, double givenX, double givenY, double X) {
        double k1, k2, k3, k4, y = givenY, x = givenX, h;
        h = (X - givenX) / (iterations);
        System.out.println(h);
        for (int i = 0; i < iterations; i++) {
            k1 = fPrime.compute(x, y);
            x+=h/2;
            k2 = fPrime.compute(x , y + h * k1 / 2);
            k3 = fPrime.compute(x , y + h * k2 / 2);
            k4 = fPrime.compute(x+h/2,y+ h *k3);
            y += 1.0/6 * h *(k1 + 2*k2 +2*k3 + k4);
            x+=h/2;
        }
        return y;
    }

    // Really bad, rk4 is just better. cant think of when euler is better
    public static double forwardEuler(Function2Args<Double,Double> fPrime, int iterations, double givenX, double givenY, double X){
        double y = givenY, x = givenX, h = (X - givenX) / (iterations);
        for (int i = 0; i < iterations; i++) {
            y+= h * fPrime.compute(x,y);
            x+=h;
        }
        return y;
    }


    public static void main(String[] args) {
        double b =DiffEq.rk4((x,y) ->{return 2*x;},20,1,1,4);
        System.out.println(b);
        b =DiffEq.forwardEuler((x,y) ->{return 2*x;},999,1,1,4);
        System.out.println(b);
    }
}
