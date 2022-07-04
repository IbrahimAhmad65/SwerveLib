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

    public boolean getCritical(){
//        System.out.println(getJacobian().getRank();
//        System.out.println(getJacobian());
//        return getJacobian().getRank() < getJacobian().getMaxRank();
        return Math.abs(getJacobian().getData()[0][0]) <= 1e-3 || Math.abs(getJacobian().getData()[1][0]) <= 1e-3;
    }
}
