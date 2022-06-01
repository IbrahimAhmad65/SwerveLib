package main;

public class Matrix {
    double[][] data;

    public Matrix(double[][] data) {
        this.data = data;
    }

    public Matrix(int rows, int columns) {
        data = new double[rows][columns];
    }

    public Matrix T() {
        int m = data.length;
        int n = data[0].length;
        double[][] transposedMatrix = new double[n][m];
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                transposedMatrix[x][y] = data[y][x];
            }
        }
        return new Matrix(transposedMatrix);
    }

    public Matrix multiply(Matrix in) {
        int i, j, k;
        int row1 = data.length;
        int row2 = in.data.length;
        int col1 = data[0].length;
        int col2 = in.data[0].length;
        if (data.length != in.data[0].length) {
            throw new IllegalArgumentException("invalid dimensions");
        }
        double out[][] = new double[row1][col2];
        for (i = 0; i < row1; i++) {
            for (j = 0; j < col2; j++) {
                for (k = 0; k < row2; k++)
                    out[i][j] += this.data[i][k] * in.data[k][j];
            }
        }
        return new Matrix(out);
    }

    public void add(Matrix in) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] += in.data[i][j];
            }
        }
    }

    public void subtract(Matrix in) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] += in.data[i][j];
            }
        }
    }

    public void scale(double scalar) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] *= scalar;
            }
        }
    }


    public static double[] solve(Matrix left, double[] right) {
        double[][] A = left.data;
        int N = right.length;
        for (int k = 0; k < N; k++) {
            int max = k;
            for (int i = k + 1; i < N; i++)
                if (Math.abs(A[i][k]) > Math.abs(A[max][k]))
                    max = i;
            double[] temp = A[k];
            A[k] = A[max];
            A[max] = temp;
            double t = right[k];
            right[k] = right[max];
            right[max] = t;
            for (int i = k + 1; i < N; i++) {
                double factor = A[i][k] / A[k][k];
                right[i] -= factor * right[k];
                for (int j = k; j < N; j++)
                    A[i][j] -= factor * A[k][j];
            }
        }
        double[] solution = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++)
                sum += A[i][j] * solution[j];
            solution[i] = (right[i] - sum) / A[i][i];
        }
        return solution;
    }

    public static double[] invert(Matrix left, double[] right) {
        double[][] A = left.data;
        int N = right.length;
        for (int k = 0; k < N; k++) {
            int max = k;
            for (int i = k + 1; i < N; i++)
                if (Math.abs(A[i][k]) > Math.abs(A[max][k]))
                    max = i;
            double[] temp = A[k];
            A[k] = A[max];
            A[max] = temp;
            double t = right[k];
            right[k] = right[max];
            right[max] = t;
            for (int i = k + 1; i < N; i++) {
                double factor = A[i][k] / A[k][k];
                right[i] -= factor * right[k];
                for (int j = k; j < N; j++)
                    A[i][j] -= factor * A[k][j];
            }
        }
        double[] solution = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++)
                sum += A[i][j] * solution[j];
            solution[i] = (right[i] - sum) / A[i][i];
        }
        return solution;
    }

    public Matrix getIdentity(int size){
        double[][] data = new double[size][size];
        for(int i = 0; i < size; i++){
            data[i][i] = 1;
        }
        return new Matrix(data);
    }


    public static double determinant(Matrix matrix) {
        if (matrix.data.length != matrix.data[0].length)
            throw new IllegalStateException("invalid dimensions");
        if (matrix.data.length == 2)
            return matrix.data[0][0] * matrix.data[1][1] - matrix.data[0][1] * matrix.data[1][0];
        double det = 0;
        for (int i = 0; i < matrix.data[0].length; i++)
            det += Math.pow(-1, i) * matrix.data[0][i]
                    * determinant(minor(matrix.data, 0, i));
        return det;
    }

    public Matrix inverse(){
        double[][] inverse = new double[data.length][this.data.length];
        for (int i = 0; i < this.data.length; i++)
            for (int j = 0; j < this.data[i].length; j++)
                inverse[i][j] = Math.pow(-1, i + j)
                        * determinant(minor(this.data, i, j));
        double det = 1.0 / determinant(this);
        for (int i = 0; i < inverse.length; i++) {
            for (int j = 0; j <= i; j++) {
                double temp = inverse[i][j];
                inverse[i][j] = inverse[j][i] * det;
                inverse[j][i] = temp * det;
            }
        }

        return new Matrix(inverse);
    }

    public static Matrix inverse(Matrix matrix) {
        double[][] inverse = new double[matrix.data.length][matrix.data.length];

        // minors and cofactors
        for (int i = 0; i < matrix.data.length; i++)
            for (int j = 0; j < matrix.data[i].length; j++)
                inverse[i][j] = Math.pow(-1, i + j)
                        * determinant(minor(matrix.data, i, j));

        // adjugate and determinant
        double det = 1.0 / determinant(matrix);
        for (int i = 0; i < inverse.length; i++) {
            for (int j = 0; j <= i; j++) {
                double temp = inverse[i][j];
                inverse[i][j] = inverse[j][i] * det;
                inverse[j][i] = temp * det;
            }
        }

        return new Matrix(inverse);
    }


    private static Matrix minor(double[][] matrix, int row, int column) {
        double[][] minor = new double[matrix.length - 1][matrix.length - 1];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; i != row && j < matrix[i].length; j++)
                if (j != column)
                    minor[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
        return new Matrix(minor);
    }

    public static void main(String[] args) {
        Matrix  matrix = new Matrix(new double[][]{{1,2,3},{4,7,8},{4,9,12}});
        System.out.println(matrix.inverse());
    }

    public Matrix transform(Function<Double,Double> function){
        for(int i = 0; i < data.length; i++){
            for (int j = 0; j < data[0].length; j++){
                data[i][j] = function.compute(data[i][j]);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "Matrix =\n" + stringArray(data);
    }

    private static String stringArray(double[][] array){
        String str = "";
        for(double[] d : array){
            for (double j : d){
                str = str + " " + j;
            }
            str = str + "\n";
        }
        return str;
    }
}
