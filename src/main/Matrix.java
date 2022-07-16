package main;

import java.security.PublicKey;
import java.util.Arrays;

public class Matrix {
    private double[][] data;

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

    public void setData(double[][] data) {
        this.data = data;
    }

    public void setData(Double[][] data) {
        this.data = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    public double[][] getData() {
        return data;
    }

    public void fill(double value) {
        for (int i = 0; i < data.length; i++) {
            Arrays.fill(data[i], value);
        }
    }

    public void fill(double[] value) {
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(value, 0, data[i], 0, data.length);
        }
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

    public Matrix add(Matrix in) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] += in.data[i][j];
            }
        }
        return this;
    }

    public void subtract(Matrix in) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] += in.data[i][j];
            }
        }
    }


    public Matrix scale(double scalar) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] *= scalar;
            }
        }
        return this;
    }

    public void setRow(int row, double[] rowData){
        data[row] = rowData;
    }

    public Vector2D[] get2DVector(int start){
        Vector2D[] v = new Vector2D[data.length];
        for (int i = 0; i < data.length; i++) {
            v[i] = new Vector2D( data[i][start],data[i][start+1]);
        }
        return v;
    }

    public void setRow(int row, Double[] rowData){
        for (int i = 0; i < rowData.length; i++) {
            data[row][i] = rowData[i];
        }
    }

    public static double[] DoubleTodouble(Double[] d) {
        double[] out = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            out[i] = d[i];
        }
        return out;
    }

    public static Double[] doubleToDouble(double[] d) {
        Double[] out = new Double[d.length];
        for (int i = 0; i < d.length; i++) {
            out[i] = d[i];
        }
        return out;
    }

    public static double[] solve(Matrix left, double[] right) {
        double[][] A = left.data;
        int N = right.length;
        for (int k = 0; k < N; k++) {
            int max = k;
            for (int i = k + 1; i < N; i++)
                if (Math.abs(A[i][k]) > Math.abs(A[max][k])) max = i;
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
                if (Math.abs(A[i][k]) > Math.abs(A[max][k])) max = i;
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

    public Matrix getIdentity(int size) {
        double[][] data = new double[size][size];
        for (int i = 0; i < size; i++) {
            data[i][i] = 1;
        }
        return new Matrix(data);
    }


    public static double determinant(Matrix matrix) {
        if (matrix.data.length != matrix.data[0].length) throw new IllegalStateException("invalid dimensions");
        if (matrix.data.length == 2)
            return matrix.data[0][0] * matrix.data[1][1] - matrix.data[0][1] * matrix.data[1][0];
        double det = 0;
        for (int i = 0; i < matrix.data[0].length; i++)
            det += Math.pow(-1, i) * matrix.data[0][i] * determinant(minor(matrix.data, 0, i));
        return det;
    }

    public Matrix inverse() {
        double[][] inverse = new double[data.length][this.data.length];
        for (int i = 0; i < this.data.length; i++)
            for (int j = 0; j < this.data[i].length; j++)
                inverse[i][j] = Math.pow(-1, i + j) * determinant(minor(this.data, i, j));
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
                inverse[i][j] = Math.pow(-1, i + j) * determinant(minor(matrix.data, i, j));

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
                if (j != column) minor[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
        return new Matrix(minor);
    }

    public Matrix reducedRowEchelon() {

        double[][] rref = new double[this.data.length][this.data[0].length];

        for (int r = 0; r < rref.length; ++r) {
            for (int c = 0; c < rref[r].length; ++c) {
                rref[r][c] = this.data[r][c];
            }
        }

        for (int p = 0; p < rref.length; ++p) {
            double pv = rref[p][p];
            if (pv != 0) {
                double pvInv = 1.0 / pv;
                for (int i = 0; i < rref[p].length; ++i) {
                    rref[p][i] *= pvInv;
                }
            }

            for (int r = 0; r < rref.length; ++r) {
                if (r != p) {
                    double f = rref[r][p];
                    for (int i = 0; i < rref[r].length; ++i) {
                        rref[r][i] -= f * rref[p][i];
                    }
                }
            }
        }
        return new Matrix(rref);
    }

    public int getRank() {
        int counter = 0;
        double[][] datas = reducedRowEchelon().getData();
        for (double[] d : datas) {
            for(double j : d){
                if(j != 0 ){
                    counter++;
                    break;
                }
            }
        }
        return counter;
    }

    public Matrix transform(Function<Double, Double> function) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = function.compute(data[i][j]);
            }
        }
        return this;
    }

    public Matrix clone() {
        return new Matrix(this.data);
    }

    public static void main(String[] args) {
        Matrix matrix = new Matrix(new double[][]{{1, 2, 1}, {-2, -3, 1}, {3, 5, 0}});
        System.out.println(matrix.getRank());
    }

    public int getMaxRank(){
        return Math.min(data.length,data[0].length);
    }

    @Override
    public String toString() {
        return "Matrix =\n" + stringArray(data);
    }

    public static String stringArray(double[][] array) {
        String str = "";
        for (double[] d : array) {
            for (double j : d) {
                str = str + " " + j;
            }
            str = str + "\n";
        }
        return str;
    }
}
