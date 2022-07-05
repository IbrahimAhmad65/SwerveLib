package main;

import java.util.function.Supplier;

public class JacobianDiscrete2D {
    Supplier<Matrix> ms;
    public JacobianDiscrete2D(Supplier<Matrix> ms){
        this.ms = ms;
    }

    public Matrix getJacobian(){
        return new Matrix(new double[][]{{ms.get().getData()[0][1],ms.get().getData()[1][1]}}).T();
    }

    public void setSupplier(Supplier<Matrix> ms){
        this.ms = ms;
    }

    public boolean getCritical(double error){
        return Math.abs(getJacobian().getData()[0][0]) <= error || Math.abs(getJacobian().getData()[1][0]) <= error;
    }
}
