package SplineGenerator.Follower;

import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;
import main.Vector2D;

public class PosBasicSplineFollower implements Follower {
    protected Spline spline;
    protected double t;
    protected Vector2D oldPos;
    protected double splineR;
    protected double splineRes;
    protected Waypoints waypoints;

    protected double forVel;
    protected double toVel;

    public PosBasicSplineFollower(Spline spline, double splineR, double splineRes, double forVel, double toVel, Waypoints waypoints) {
        this.spline = spline;
        t = 0;
        this.splineR = splineR;
        this.splineRes = splineRes;
        oldPos = Vector2D.getVector2DFromDVector(spline.get(t).toVector());
        this.waypoints = waypoints;
        this.toVel = toVel;
        this.forVel = forVel;
    }


    // tangent addition method
    public Vector2D get(Vector2D pos) {
        t = this.findTOnSpline(pos);
        Vector2D currentPos = spline.get(t).toVector2D();
        Vector2D v = spline.evaluateDerivative(t, 1).toVector2D();
        return v.clone().scale(forVel).add(currentPos.clone().subtract(pos).scale(toVel)).setMagnitude(.2);
    }

    public double findTOnSpline(Vector2D pos) {
        Vector2D min = spline.get(t).toVector2D().subtract(pos);
        Vector2D now = new Vector2D();
        double newT = t;
        for (double i = t - splineR; i < t + splineR; i += splineRes) {
            now.subtract(pos);
            if (now.getMagnitude() < min.getMagnitude()) {
                min = spline.get(i > 0 ? i : .001).toVector().toVector2D().subtract(pos);
                newT = i;
            }
        }
        return newT;
    }

    public Vector2D findPosOnSpline(Vector2D pos) {
        Vector2D min = spline.get(t).toVector2D().subtract(pos);
        Vector2D now = new Vector2D();
        for (double i = t - splineR; i < t + splineR; i += splineRes) {
            now.subtract(pos);
            if (now.getMagnitude() < min.getMagnitude()) {
                min = spline.get(i > 0 ? i : .001).toVector().toVector2D().subtract(pos);
            }
        }
        return min;
    }

    public static void main(String[] args) {
        Waypoints w = new Waypoints(new Waypoint(8, () -> {
        }, 2), new Waypoint(0, () -> {
        }, 2));


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

        PosBasicSplineFollower posBasicSplineFollower = new PosBasicSplineFollower(spline, .1, .01, .01, .25, w);
        Vector2D pos = new Vector2D(5, 0);
        for (int j = 0; j < 1000; j++) {
            try {
                pos.add(posBasicSplineFollower.get(pos));
            } catch (Exception e) {
            }
        }

    }

}
