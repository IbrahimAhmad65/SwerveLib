package main;

// f returns vector takes in scalar
public class Jacobian2D{
    private Function<Double,Double> f1;
    private Function<Double,Double> f2;
    public Jacobian2D(Function<Double,Double> f1, Function<Double,Double> f2){
        this.f1 = f1;
        this.f2 = f2;
    }

    public Matrix getJacobian(double t){
        return new Matrix(new double[][]{{f1.compute(t),f2.compute(t)}});
    }

}
