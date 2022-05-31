package main;

public class NewtonRaphson {
    private Function f;
    private Function fPrime;
    public NewtonRaphson(Function<Double,Double> f, Function<Double,Double> fPrime){
        this.f = f;
        this.fPrime = fPrime;
    }
    public double approximate(double initial, int iterations){
        double current = initial;
        for(int i = 0; i < iterations; i++){
            current = current - ((Double)f.compute(current)).doubleValue()/((Double)fPrime.compute(current)).doubleValue();
        }
        return current;
    }

    public double smartApproximate(double initial, double iterations, Function<Boolean,Double> threshold){
        double current = initial;
        double oldCurrent = 0;
        for(int i = 0; i < iterations; i++){
            oldCurrent = current;
            current = current - ((Double)f.compute(current)).doubleValue()/((Double)fPrime.compute(current)).doubleValue();
            if(threshold.compute(current - oldCurrent)){
                System.out.println("Premature Exiting on Newtons Method at i= " + i + " Iterations= " + iterations);
                break;
            }
        }
        return current;
    }

    public static void main(String[] args) {
        NewtonRaphson newtonRaphson = new NewtonRaphson((x)->{return (x*Math.pow(2.718,x)) - 5;},(x)->{return (x*Math.pow(2.718,x) + Math.pow(2.718,x));});
//        NewtonRaphson newtonRaphson = new NewtonRaphson((x)->{return(x -3);},(x)->{return 1;});
        double b = newtonRaphson.approximate(1,5);
        System.out.println(b);
        b = newtonRaphson.smartApproximate(1,100,(x)->{return(Math.abs(x) < .0000001);});
        System.out.println(b);
    }
}
