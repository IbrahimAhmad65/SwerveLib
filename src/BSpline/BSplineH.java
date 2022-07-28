package BSpline;

import main.Function;
import main.Vector2D;

public class BSplineH {
    private SplinePoint[] splinePoints;
    private int equationNumber;
    private Function<SplinePoint,Double>[] equations;

    private double t;

    public BSplineH(double splineR, double fieldR, SplinePoint... splinePoints){
        this.splinePoints = splinePoints;
        equationNumber = splinePoints.length-1;
        equations = new Function[equationNumber];
        for (int i = 0; i < equationNumber; i++) {
            double x1 = splinePoints[i].getPos().getX();
            double x2 = splinePoints[i+1].getPos().getX();
            double y1 = splinePoints[i].getPos().getY();
            double y2 = splinePoints[i+1].getPos().getY();
            double tan1 = splinePoints[i].getVelocity().getY() / splinePoints[i].getVelocity().getX();
            double tan2 = splinePoints[i+1].getVelocity().getY() / splinePoints[i+1].getVelocity().getX();
            equations[i] = CubicHermite.computeInterpParametric(x1,x2,y1,y2,tan1,tan2);
            System.out.println(CubicHermite.getDesmosEquations(x1,x2,y1,y2,tan1,tan2,"t"));
        }
        this.t = splineR;
    }

    public Vector2D evaluatePos(double t){
        if(splinePoints[(int)t].getPos().getX() < splinePoints[(int)(t+.9999)].getPos().getX()){
        return equations[(int)t].compute(t-(int)t).getPos();
        }
        return equations[(int)t].compute(t-(int)t).getPos();
    }

    public Vector2D evaluateVelocity(double t){
        return equations[(int)t].compute(t-(int)t).getVelocity();
    }


    public SplinePoint evaluateSplinePoint(double t){
        return equations[(int)t].compute(t);
    }

    public double findNearestPoint(Vector2D pos){
        Vector2D small = new Vector2D(999999999,9999999);
        double j = -1;
        for (double i = 0; i < equationNumber; i+=t) {
            if(evaluatePos(i).clone().subtract(pos).getMagnitude() < small.getMagnitude()){
                small = evaluatePos(i).clone().subtract(pos);
                j=i;
            }
        }
        return j;
    }

    public double findNearestPointOnInterval(Vector2D pos, double lowT, double highT){
        Vector2D small = new Vector2D(999999999,9999999);
        double j = -1;
        for (double i = lowT > 0 ? lowT : 0; i < equationNumber && i < highT; i+=t) {
            if(evaluatePos(i).clone().subtract(pos).getMagnitude() < small.getMagnitude()){
                small = evaluatePos(i).clone().subtract(pos);
                j=i;
            }
        }
        return j;
    }

    private double findNearestPointTest(Vector2D pos){
        Vector2D small = new Vector2D(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY);
        double j = -1;
        for (double i = 0; i < equationNumber; i+=t) {
            if(evaluatePos(i).clone().subtract(pos).getMagnitude() < small.getMagnitude()){
                small = evaluatePos(i);
                j=i;
            }
        }
        return j;
    }
    public void printSplinePos(){
        for (double i = 0; i < equationNumber; i+=t) {
            System.out.println(evaluatePos(i));
        }
    }

    public double getT(){
        return t;
    }

    public int getEquationNumber() {
        return equationNumber;
    }

    public static void main(String[] args) {
        //new SplinePoint(new Vector2D(.1,.1),new Vector2D(1,1)),
        SplinePoint[] splinePoints1 = {
                new SplinePoint(new Vector2D(.5,1),new Vector2D(1,3)),
                new SplinePoint(new Vector2D(.6,1),new Vector2D(1,3)),
                new SplinePoint(new Vector2D(.1,.1),new Vector2D(1,1))};
        SplinePoint[] splinePoints = {new SplinePoint(new Vector2D(.1,.1),new Vector2D(1,1)),
                new SplinePoint(new Vector2D(.1,1),new Vector2D(1,2))};
        BSplineH b = new BSplineH(.01,.1,splinePoints1);
        System.out.println(b.evaluatePos(0));
        System.out.println(b.findNearestPoint(new Vector2D(.5,1)));
        System.out.println(b.evaluatePos(b.findNearestPoint(new Vector2D(.11,1.1))));
//        b.printSplinePos();
    }
}
