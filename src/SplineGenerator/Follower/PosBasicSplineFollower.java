package SplineGenerator.Follower;

import SplineGenerator.GUI.DisplayGraphics;
import SplineGenerator.GUI.Displayable;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;
import main.Function;
import main.Vector2D;

// Superclass Of Spline Follower Classes
public class PosBasicSplineFollower implements Follower, Displayable {
    protected Followable spline;
    protected double t;
    protected Vector2D oldPos;
    protected double splineR;
    protected double splineRes;
    protected Waypoints waypoints;

    protected double forVel;
    protected double toVel;

    protected Vector2D pos = new Vector2D();
    public PosBasicSplineFollower(Followable spline, double splineR, double splineRes, double forVel, double toVel, Waypoints waypoints, Runnable run) {
        run.run();
        this.spline = spline;
        t = 0;
        this.splineR = splineR;
        this.splineRes = splineRes;
        oldPos = Vector2D.getVector2DFromDVector(spline.get(t).toVector());
        this.waypoints = waypoints;
        this.toVel = toVel;
        this.forVel = forVel;
    }

    public PosBasicSplineFollower(Followable spline, double splineR, double splineRes, double forVel, double toVel, Waypoints waypoints) {
        this.spline = spline;
        t = 0;
        this.splineR = splineR;
        this.splineRes = splineRes;
        oldPos = Vector2D.getVector2DFromDVector(spline.get(t).toVector());
        this.waypoints = waypoints;
        this.toVel = toVel;
        this.forVel = forVel;
    }


    /**
     * Gets the desired velocity of the follower given the current position
     * */
    public Vector2D get(Vector2D pos) {
        this.pos = pos;
        Vector2D currentPos = findPosOnSpline(pos);
        Vector2D v = spline.evaluateDerivative(t, 1).toVector2D();
        return v.clone().scale(forVel).add(currentPos.clone().subtract(pos).scale(toVel)).setMagnitude(1);
    }


    /**
     * Finds the nearest t on the spline given a position
     **/
    public double findTOnSpline(Vector2D pos) {
        Vector2D min = new Vector2D(99999,99999);
        double newT = 0;
        Vector2D check = new Vector2D();
        for (double i = t - splineR; i < t + splineR; i += splineRes) {
            double b = i > 0 ? i : .0001;
            check = spline.get(b < spline.getNumPieces() ? b : spline.getNumPieces() - .0001).toVector2D().subtract(pos);
            if(check.getMagnitude() < min.getMagnitude()){
                min = check.clone();
                newT = b < spline.getNumPieces() ? b : spline.getNumPieces() - .0001;
            }
        }
        t = newT;
        return newT;
    }

    /**
     * Finds the nearest position on the spline given a position
     **/
    public Vector2D findPosOnSpline(Vector2D pos) {
        Vector2D min = new Vector2D(99999,99999);
        Vector2D check = new Vector2D();
        double newT = 0;
        for (double i = t - splineR; i < t + splineR; i += splineRes) {
            double b = i > 0 ? i : .0001;
            check = spline.get(b < spline.getNumPieces() ? b : spline.getNumPieces() - .0001).toVector2D().subtract(pos);
            if(check.getMagnitude() < min.getMagnitude()){
                min = check.clone();
                newT = b < spline.getNumPieces() ? b : spline.getNumPieces() - .0001;
            }
        }
        t = newT;
        return min;
    }

    public boolean finished() {
        return t >= spline.getNumPieces() - .01;
    }
    /**
     * Returns the waypoints that have been associated with this follower
     * **/
    public Waypoints getWaypoints() {
        return waypoints;
    }

    // Tester code to check whether this class actually works
    public static void main(String[] args) {
        Waypoints w = new Waypoints(new Waypoint(8, () -> {
        }, 2), new Waypoint(0, () -> {
        }, 2));


        PolynomicSpline spline = new PolynomicSpline(2);
        spline.setPolynomicOrder(5);

        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DDirection(3, 1), new DDirection(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(6, 8), new DDirection(2, 5)));
        spline.addControlPoint(new DControlPoint(new DVector(2, .01), new DDirection(2, 5), new DDirection(0, 0)));

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

        PosBasicSplineFollower posBasicSplineFollower = new PosBasicSplineFollower(spline, .1,
                .01, .01, .02, w,()->{System.out.println("hola");});
        Vector2D pos = new Vector2D(0, 0);
        for (int j = 0; j < 1000; j++) {
            try {
                pos.add(posBasicSplineFollower.get(pos));
                System.out.println(pos);
            } catch (Exception e) {
            }
        }
//
    }

    @Override
    public void display(DisplayGraphics graphics) {
        graphics.paintPoint(pos.toDVector());
    }
}
