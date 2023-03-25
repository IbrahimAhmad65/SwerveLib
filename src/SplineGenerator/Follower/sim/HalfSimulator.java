package SplineGenerator.Follower.sim;

import SplineGenerator.Follower.*;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;
import main.Vector2D;

public class HalfSimulator {
    private Vector2D pos;
    private GoodestFollower goodestFollower;

    private SplineDisplay splineDisplay;

    private double scalePerTick;
    private double timePerTick;
    public HalfSimulator(Followable spline,Follower follower, double scalePerTick, double timePerTick) {
        splineDisplay = new SplineDisplay(spline, 0, 1, 1300, 700);
        splineDisplay.displayables.add((GoodestFollower) follower);
        splineDisplay.display();
        pos = new Vector2D(0, 0);
        this.timePerTick = timePerTick;
        this.scalePerTick = scalePerTick;
        this.goodestFollower = (GoodestFollower) follower;
    }

    public void run() throws Exception{
        if (!goodestFollower.finished()) {
            pos.add(goodestFollower.get(pos).scale(scalePerTick));
        }
        Thread.sleep((long) timePerTick);
        splineDisplay.repaint();

    }

    public static void main(String[] args) throws Exception {
//        PolynomicSpline spline = new PolynomicSpline(2);
//        spline.setPolynomicOrder(5);
//        double i = 1.0;
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{0.0, 0.0}), new DDirection(new double[]{1.0, 1.0}), new DDirection(new double[]{0.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{i, i}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{2.0 * i, 0.0}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{i, -i}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{0.0, 0.0}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{-i, i}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{-2.0 * i, 0.0}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{-i, -i}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{0.0, 0.0}), new DDirection(new double[]{0.0, 0.0}), new DDirection(new double[]{0.0, 0.0})}));
//        spline.closed = false;
//        InterpolationInfo c1 = new InterpolationInfo();
//        c1.interpolationType = Spline.InterpolationType.Linked;
//        c1.endBehavior = Spline.EndBehavior.Hermite;
//        spline.interpolationTypes.add(c1);
//        InterpolationInfo c2 = new InterpolationInfo();
//        c2.interpolationType = Spline.InterpolationType.Linked;
//        c2.endBehavior = Spline.EndBehavior.Hermite;
//        spline.interpolationTypes.add(c2);
//        InterpolationInfo c3 = new InterpolationInfo();
//        c3.interpolationType = Spline.InterpolationType.Linked;
//        c3.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c3);
//        InterpolationInfo c4 = new InterpolationInfo();
//        c4.interpolationType = Spline.InterpolationType.Linked;
//        c4.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c4);
//        spline.generate();
//        spline.takeNextDerivative();
//
//        Waypoints w = new Waypoints(new Waypoint(0, () -> {
//        }, 2), new Waypoint(2, () -> {
//        }, .2), new Waypoint(4, () -> {
//            System.out.println("hi");
//        }, .2,true), new Waypoint(6, () -> {
//        }, .2), new Waypoint(7, () -> {
//        }, 1));



        PolynomicSpline spline = new PolynomicSpline(2, 1);
        double i = 5;
        spline.addControlPoint(new DControlPoint(new DVector(0.0, 0.0)));
        spline.addControlPoint(new DControlPoint(new DVector(i, i)));
        spline.addControlPoint(new DControlPoint(new DVector(2.0 * i, 0.0)));
        spline.addControlPoint(new DControlPoint(new DVector(0, 0.0)));
        spline.closed = false;


        InterpolationInfo c1 = new InterpolationInfo();
//        c1.interpolationType = Spline.InterpolationType.None;
//        c1.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c1);


        spline.generate();


        spline.takeNextDerivative();

        c1.interpolationType = Spline.InterpolationType.Linked;
        c1.endBehavior = Spline.EndBehavior.Hermite;
//        spline.interpolationTypes.add(c1);
        InterpolationInfo c2 = new InterpolationInfo();
        c2.interpolationType = Spline.InterpolationType.Linked;
        c2.endBehavior = Spline.EndBehavior.Hermite;
//        spline.interpolationTypes.add(c2);
        InterpolationInfo c3 = new InterpolationInfo();
        c3.interpolationType = Spline.InterpolationType.Linked;
        c3.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c3);
        InterpolationInfo c4 = new InterpolationInfo();
        c4.interpolationType = Spline.InterpolationType.Linked;
        c4.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c4);
        //----
        PolynomicSpline spline2 = new PolynomicSpline(2);
        spline2.setPolynomicOrder(5);
        spline2.addControlPoint(new DControlPoint(new DVector(0, 0.0), new DDirection(1.0, 1.0), new DDirection(0.0, 0.0)));
        spline2.addControlPoint(new DControlPoint(new DVector(1, 0.0), new DDirection(3.0, 0.0), new DDirection(0.0, 0.0)));
        spline2.closed = false;

        spline2.interpolationTypes.add(c1);
        spline2.interpolationTypes.add(c2);
        spline2.interpolationTypes.add(c3);
        spline2.interpolationTypes.add(c4);

        spline2.generate();
        spline2.takeNextDerivative();
        //--//
        SplineStringer splineStringer = new SplineStringer(spline, spline2);

        Waypoints w = new Waypoints(spline,new Waypoint(0, () -> {
        }, .2), new Waypoint(7, () -> {
        }, .2));


        Vector2D pos = new Vector2D();

        RequiredFollowerPoint[] r = new RequiredFollowerPoint[]{new RequiredFollowerPoint(0,0),new RequiredFollowerPoint(4,-2e1*Math.PI)};
        GoodestFollower goodestFollower = new GoodestFollower(splineStringer,.1,.01,w,new RequiredFollowerPoints(.1,.0,r));

        HalfSimulator halfSimulator = new HalfSimulator(splineStringer,goodestFollower,.1,10);
        while (true){
            halfSimulator.run();
        }
    }


}
