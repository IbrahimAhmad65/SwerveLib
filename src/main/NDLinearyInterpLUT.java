package main;
public class NDLinearyInterpLUT{

    Matrix args;
    Matrix outputs;
    public NDLinearyInterpLUT(Matrix args, Matrix outputs){
        this.args = args;
        this.outputs = outputs;
    }

    public static void main(String[] args){
        Matrix j = new Matrix(new double[][]{{10,19,18},{5,2,6,}});
        Matrix k = new Matrix(new double[][]{{5},{6}});
        NDLinearyInterpLUT bob = new NDLinearyInterpLUT(j,k);
    }
        
}
