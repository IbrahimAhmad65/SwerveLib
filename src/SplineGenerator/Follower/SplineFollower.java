package SplineGenerator.Follower;

import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;
import main.Vector2D;

public class SplineFollower {
    private Spline spline;
    private double t;
    private Vector2D oldPos;
    private double splineR;
    private double splineRes;
    private double lookForward;
    private Waypoints waypoints;

    public SplineFollower(Spline spline, double splineR, double splineRes, double lookForward, Waypoints waypoints) {
        this.spline = spline;
        t = 0;
        this.splineR = splineR;
        this.splineRes = splineRes;
        oldPos = Vector2D.getVector2DFromDVector(spline.get(t).toVector());
        this.lookForward = lookForward;
        this.waypoints = waypoints;
    }

    // look forward method
    public Vector2D get(Vector2D pos) {
//            Vector2D min = spline.get(t - splineR).toVector2D().subtract(pos);
//            Vector2D now = new Vector2D();
//            double newT = t;
//            for (double i = t - splineR; i < t + splineR; i += splineRes) {
//                now.subtract(pos);
//                if (now.getMagnitude() < min.getMagnitude()) {
//                    min = spline.get(i).toVector().toVector2D().subtract(pos);
//                    newT = i;
//                }
//            }
//            t = newT;
        t += lookForward;
//        System.out.println(t);
//        System.out.println(spline.get(t + lookForward).toVector2D().subtract(pos).setMagnitude(waypoints.getSpeed(t)));
        return spline.get(t).toVector2D().subtract(pos).setMagnitude(.001);
    }

    // tangent addition method
    public Vector2D get(Vector2D pos, double forVel, double toVel){
        if (!oldPos.equals(pos)) {
            Vector2D min = spline.get(t).toVector2D().subtract(pos);
            Vector2D now = new Vector2D();
            double newT = t;
            for (double i = t - splineR; i < t + splineR; i += splineRes) {
                now.subtract(pos);
                if (now.getMagnitude() < min.getMagnitude()) {
//                    System.out.println(spline.get(0.1));
                    min = spline.get(i > 0 ? i : .001).toVector().toVector2D().subtract(pos);
                    newT = i;
                }
            }
            t = newT;
        }
        Vector2D currentPos = spline.get(t).toVector2D();
        Vector2D newV = spline.get(t+splineRes).toVector2D();
        Vector2D v = newV.clone().subtract(currentPos).scale(1/.001);
        return v.scale(forVel).add(newV.clone().subtract(pos).scale(-toVel));
    }

    public static void main(String[] args) {
        Waypoints w = new Waypoints(new Waypoint(.1,()->{},2),new Waypoint(0,()->{},2));


        PolynomicSpline spline = new PolynomicSpline(2);
        spline.setPolynomicOrder(5);

        double i = 1;

        // Define Path
        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DDirection(1, 1), new DDirection(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(i, i), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(2 * i, 0), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(i, -i), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-i, i), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-2 * i, 0), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-i, -i), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DDirection(0, 0), new DDirection(0, 0)));

        spline.closed = false;
        InterpolationInfo c1 = new InterpolationInfo();
        c1.interpolationType = Spline.InterpolationType.Linked;
        c1.endBehavior = Spline.EndBehavior.Hermite;
        spline.interpolationTypes.add(c1);

        InterpolationInfo c2 = new InterpolationInfo();
        c2.interpolationType = Spline.InterpolationType.Linked;
        c2.endBehavior = Spline.EndBehavior.Hermite;
        spline.interpolationTypes.add(c2);

        InterpolationInfo c3 = new InterpolationInfo();
        c3.interpolationType = Spline.InterpolationType.Linked;
        c3.endBehavior = Spline.EndBehavior.None;
        spline.interpolationTypes.add(c3);

        InterpolationInfo c4 = new InterpolationInfo();
        c4.interpolationType = Spline.InterpolationType.Linked;
        c4.endBehavior = Spline.EndBehavior.None;
        spline.interpolationTypes.add(c4);
        spline.generate();
        spline.takeNextDerivative();

        SplineFollower splineFollower = new SplineFollower(spline,.1,.01,.001,w);
        Vector2D pos = new Vector2D(0,0);
        for (int j = 0; j < 1000; j++) {
            pos.add(splineFollower.get(pos,.01,-.1));
//
            System.out.println(pos);
//            System.out.println( spline.get(j/126.0));
        }

    }

}
