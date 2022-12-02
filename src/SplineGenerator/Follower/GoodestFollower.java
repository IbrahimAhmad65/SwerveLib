package SplineGenerator.Follower;

import BSpline.BSplineH;
import BSpline.SplinePoint;
import SplineGenerator.GUI.DisplayGraphics;
import SplineGenerator.GUI.Displayable;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;
import main.LinearlyInterpLUT;
import main.Vector2D;

import java.awt.*;

public class GoodestFollower implements Follower, Displayable {
    Followable followable;
    protected double t = 0;
    protected  Vector2D pos = new Vector2D();
    protected double splineR = 0;
    protected double splineRes = 0;
    protected Waypoints waypoints;
    private BSplineH angleSpline;
    private LinearlyInterpLUT angleTLut;
    private Vector2D followerVel = new Vector2D();
    public GoodestFollower(Followable followable, double splineR, double splineRes, Waypoints waypoints, RequiredFollowerPoints requiredFollowerPoints) {
        waypoints.addWaypoint(new Waypoint(followable.getNumPieces(), () -> {
        }, 0));
        this.followable = followable;
        this.splineR = splineR;
        this.splineRes = splineRes;
        this.waypoints = waypoints;


        SplinePoint[] splinePoints =new SplinePoint[requiredFollowerPoints.getRequiredFollowerPoints().length];
        double[] arr = requiredFollowerPoints.getAnglesOrdered();
        double[] arr2 = requiredFollowerPoints.getTOrdered();
        double[] indexArr = new double[splinePoints.length];
        for (int i = 0; i < splinePoints.length; i++) {
            splinePoints[i] = new SplinePoint(new Vector2D(arr2[i],arr[i]), new Vector2D(1,0));
            indexArr[i] = i;
        }
        angleTLut = new LinearlyInterpLUT(arr2,indexArr);
        angleSpline = new BSplineH(.1,.1,splinePoints);
    }

    @Override
    public Vector2D get(Vector2D pos) {
        this.pos = pos;
        Vector2D nearestPos = findPosOnSpline(pos);
        Vector2D out = nearestPos.subtract(pos).scale(30);
        out.add(followable.evaluateDerivative(t, 1).toVector2D());
        out.setMagnitude(waypoints.getSpeed(t));
        this.followerVel = out.clone();
        return out;
    }

    /**
     * Finds the nearest t on the spline given a position
     **/
    public double findTOnSpline(Vector2D pos) {

        double distMin = 9e307;
        double tempT = 0;
        for (double j = 0; j < followable.getNumPieces()-.00001; j += splineRes){
            Vector2D temp = followable.get(j).toVector2D();

            if(temp.getDistance(pos) < distMin){
                distMin = temp.getDistance(pos);
                tempT = j;
            }
        }
        this.t = tempT;
        return this.t;
    }

    @Override
    public Waypoints getWaypoints() {
        return waypoints;
    }

    /**
     * Finds the nearest position on the spline given a position
     **/
    public Vector2D findPosOnSpline(Vector2D pos) {
        Vector2D min = new Vector2D(9e307,9e307);

        double distMin = 9e307;
        double tempT = 0;
        for (double j = 0; j < followable.getNumPieces()-.00001; j += splineRes){
            Vector2D temp = followable.get(j).toVector2D();

            if(temp.getDistance(pos) < distMin){
                distMin = temp.getDistance(pos);
                min = temp.clone();
                tempT = j;
            }
        }
        this.t = tempT;
        return min;
    }
    private double tSpinMap(double t){
        return angleTLut.get(t);
    }
    public double getSpin(Vector2D pos){
        double tempT = findTOnSpline(pos);
        return angleSpline.evaluatePos(tSpinMap(tempT)).getY();
    }

    public boolean finished() {
        return t >= followable.getNumPieces() - .01;
    }

    public static void main(String[] args) {



        PolynomicSpline spline = new PolynomicSpline(2, 1);
        spline.addControlPoint(new DControlPoint(new DVector(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(1, 1)));
        spline.addControlPoint(new DControlPoint(new DVector(3, -1)));
        InterpolationInfo interpolationInfo = new InterpolationInfo();
        interpolationInfo.interpolationType = Spline.InterpolationType.None;
        interpolationInfo.endBehavior = Spline.EndBehavior.None;
        spline.interpolationTypes.add(interpolationInfo);
        spline.generate();
        spline.takeNextDerivative();

//        PolynomicSpline spline = new PolynomicSpline(2);
//        spline.setPolynomicOrder(5);
//        byte i = 1;
//
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{0,0}), new DDirection(new double[]{0, -1}), new DDirection(new double[]{0.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{0, -1.2}), new DDirection(new double[]{-.1, -1})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{-2.54,-1.2}), new DDirection(new double[]{1, 0}), new DDirection(new double[]{0.0, 0.0})}));
//
//
//        spline.closed = false;
//        InterpolationInfo c1 = new InterpolationInfo();
//        c1.interpolationType = Spline.InterpolationType.Linked;
//        c1.endBehavior = Spline.EndBehavior.Hermite;
//        spline.interpolationTypes.add(c1);
//
//        InterpolationInfo c2 = new InterpolationInfo();
//        c2.interpolationType = Spline.InterpolationType.Linked;
//        c2.endBehavior = Spline.EndBehavior.Hermite;
//        spline.interpolationTypes.add(c2);
//
//        InterpolationInfo c3 = new InterpolationInfo();
//        c3.interpolationType = Spline.InterpolationType.Linked;
//        c3.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c3);
//
//        InterpolationInfo c4 = new InterpolationInfo();
//        c4.interpolationType = Spline.InterpolationType.Linked;
//        c4.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c4);
//        spline.generate();
//        spline.takeNextDerivative();


        Waypoints w = new Waypoints(new Waypoint(0, () -> {
        }, .4), new Waypoint(3, () -> {
        }, .2));
        RequiredFollowerPoint[] r = new RequiredFollowerPoint[]{new RequiredFollowerPoint(0,0),new RequiredFollowerPoint(8,Math.PI)};
        GoodestFollower goodestFollower = new GoodestFollower(spline,.1,.01,w,new RequiredFollowerPoints(.1,.0,r));
        Vector2D pos = new Vector2D(-1,2);
        SplineDisplay splineDisplay = new SplineDisplay(spline,0,1,1300,700);
        splineDisplay.displayables.add(goodestFollower);
        splineDisplay.display();
        while (true){
            if(!goodestFollower.finished()){
            pos.add(goodestFollower.get(pos).scale(.0001));
            System.out.println(pos);}
            splineDisplay.repaint();
        }
    }

    @Override
    public void display(DisplayGraphics graphics) {
        graphics.paintPoint(pos.toDVector(),0,1,new Color(255,255,255));
        graphics.paintVector(pos.toDVector(),followerVel.toDVector());
    }
}
