package BSpline;


import main.LinearlyInterpLUT;

import main.Vector2D;

public class VectorField {
    private double[][][] vArray;
    private double t;
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private Vector2D centerField;
    // doesnt use interpolation, maybe ill do that later, inputs must be rectangular
    public VectorField(double[][][] vArray, double t, Vector2D centerField){
        this.vArray = vArray;
        xMin = -(vArray.length/2.0)*t/2;
        xMax = (vArray.length/2.0)*t/2;
        yMin = -(vArray[0].length/2.0)*t/2;
        yMax = (vArray[0].length/2.0)*t/2;
        System.out.println(xMin);
        System.out.println(xMax);
        this.t = t;
        this. centerField = centerField;
    }

    public Vector2D get(Vector2D pos){
        Vector2D newPos = pos.clone().subtract(centerField);
        int x = (int)(Math.floor((newPos.getX() - xMin)/t));
        int y = (int)(Math.floor((newPos.getY() - yMin)/t));
        return new Vector2D(vArray[x][y][0],vArray[x][y][1]);

    }

    public static void main(String[] args) {
        double[] a = {1,1};
        double[] b = {-1,-1};
        double[] c = {1,-1};
        double[] d = {-1,1};
        double[][][] v= {{b,c},{d,a}};
        VectorField vectorField = new VectorField(v,.2,new Vector2D(1,1));
        System.out.println(vectorField.get(new Vector2D(1.1,1.1)));
    }
}
