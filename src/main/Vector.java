package main;

public class Vector {
    enum DimensionError {
        THROW, ZERO,
    }

    private  DimensionError dimensionError;
    private double[] data;

    public Vector(int dimensions, DimensionError dimensionError) {
        data = new double[dimensions];
        this.dimensionError = dimensionError;
    }

    public Vector(DimensionError dimensionError, double... data) {
        this.data = data;
        this.dimensionError = dimensionError;
    }

    public static double dot(Vector v1, Vector v2, DimensionError dimensionError) {
        dimensionCheck("dot product", dimensionError, v1, v2);
        double out = 0;
        for (int i = 0; i < v1.data.length; i++) {
            try {
                out += v1.data[i] * v2.data[i];
            } catch (Exception e) {
                // must check if throws, shouldnt tho
                e.printStackTrace();
            }
        }
        return out;
    }

    public double[] getData() {
        return data;
    }

    public double getMagnitude() {
        return Math.sqrt(dot(this, this, dimensionError));
    }

    public Vector project(Vector axis) {
        Vector out = axis.clone();
        out.scale(dot(this, axis, dimensionError) / (dot(axis, axis, dimensionError)));
        return out;
    }

    // relinks
    public void relinkData(double[] data) {
        this.data = data;
    }

    // modifies v1
    public static void vectorScale(Vector v1, double scalar) {
        for (int i = 0; i < v1.data.length; i++) {
            v1.data[i] *= scalar;
        }
    }

    public void scale(double scalar) {
        for (int i = 0; i < data.length; i++) {
            data[i] *= scalar;
        }
    }

    public Vector getUnitVector(int dimension) {
        double[] out = new double[data.length];
        out[dimension] = 1;
        return new Vector(dimensionError, out);
    }

    public void setMagnitude(double magnitude) {
        double oldMag = getMagnitude();
        if(oldMag == 0){
            return;
        }
        this.scale(magnitude / (oldMag));
    }

    public double getAngleBetween(Vector vector1) {
        return dot(this, vector1, dimensionError) / (getMagnitude() * vector1.getMagnitude());
    }

    // Does not unlink but is slow
    public void setDataWithShadowing(double[] data) {
        dimensionCheck("shadow copying", dimensionError, this, new Vector(dimensionError, data));
        for (int i = 0; i < data.length; i++) {
            this.data[i] = data[i];
        }

    }

    // Preserves shadowing
    public void addToSelf(Vector input) {
        dimensionCheck("self addition", dimensionError, this, input);
        for (int i = 0; i < input.data.length; i++) {
            data[i] += input.data[i];
        }
    }

    public void subtractFromSelf(Vector input) {
        dimensionCheck("self subtraction", dimensionError, this, input);
        for (int i = 0; i < input.data.length; i++) {
            data[i] -= input.data[i];
        }
    }

    public static Vector add(Vector vector1, Vector vector2, DimensionError dimensionError) {
        dimensionCheck("Static addition", dimensionError, vector1, vector2);
        Vector out = new Vector(dimensionError, vector1.data.clone());
        for (int i = 0; i < vector1.data.length; i++) {
            out.data[i] += vector2.data[i];
        }
        return out;
    }

    public static Vector subtract(Vector vector1, Vector vector2, DimensionError dimensionError) {
        dimensionCheck("Static addition", dimensionError, vector1, vector2);
        Vector out = new Vector(dimensionError, vector1.data.clone());
        for (int i = 0; i < vector1.data.length; i++) {
            out.data[i] -= vector2.data[i];
        }
        return out;
    }

    private static void dimensionCheck(String methodName, DimensionError dimensionError, Vector vector1, Vector vector2) {
        if (vector1.data.length != vector2.data.length) {
            System.out.println("Dimensions are different on " + methodName + ": " + vector1.data.length + " <- Vector 1 Length | Vector 2 Length -> " + vector2.data.length);
            if (dimensionError == DimensionError.THROW) {
                throw new IllegalArgumentException("Dimensions are different on " + methodName + ": " + vector1.data.length + " <- Vector 1 Length | Vector 2 Length -> " + vector2.data.length);
            }
        }
    }

    public Vector clone() {
        return new Vector(dimensionError, this.data.clone());
    }
}
