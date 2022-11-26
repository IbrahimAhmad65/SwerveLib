package SplineGenerator.Follower;

import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;
import main.Vector2D;

// Spline follower with required point movement modifications but no spin yet
public class PosExtraEnhancedSplineFollower extends PosBasicSplineFollower implements Follower {

    // The required follower points
    private RequiredFollowerPoints requiredFollowerPoints;

    // The Constructor
    public PosExtraEnhancedSplineFollower(Followable spline, double splineR, double splineRes, double maxToVel, double toVelSearchRadius, Waypoints waypoints, RequiredFollowerPoints r) {
        super(spline, splineR, splineRes, maxToVel, toVelSearchRadius, waypoints);
        // Instantiating the follower points for this class
        this.requiredFollowerPoints = r;
        // Adding the stopping waypoint
        waypoints.addWaypoint(new Waypoint(spline.getNumPieces() - .00001, () -> {
        }, 0));
        // Doing the pre execution optimization to find the ratio between the towards the spline velocity
        // (used for error correction when drifting off the spline)
        // and the velocity given by the gradient (the one that makes the robot actually traverse the spline)
        double minOffSum = 99999;
        double tToVel = 0;
        super.forVel = .01;
        // Running maxToVel / toVelSearchRadius paths
        for (double j = 0; j < maxToVel; j += toVelSearchRadius) {
            double offSum = 0;
            Vector2D pos = new Vector2D();
            // Trying a new toVel, and resetting the class to run again
            super.toVel = j;
            boolean exit = false;
            t = 0;
            // Running a path
            for (int k = 0; k < 1000 && !finished(); k++) {
                pos.add(this.get(pos));
                offSum += pos.clone().subtract(findPosOnSpline(pos)).getMagnitude();
            }
            // Updating the local best ratio so far
            if (offSum < minOffSum) {
                tToVel = super.toVel;
                minOffSum = offSum;
            }
        }
        // Updating the back to the spline velocity scaling to the ideal one found in the simulations
        super.toVel = tToVel;
        t = 0;

    }

    /**
     * A method that uses the standard following method as has been described in the documentation file, not to be called into
     **/
    private Vector2D superget(Vector2D pos) {
        double newT = findTOnSpline(pos);
        // Stop if we are about to overdrive the path, prevents code from crashing due to an array out of bounds exception
        if (newT >= super.spline.getNumPieces() - .01) {
            return new Vector2D(0, 0);
        }
        // Stop if we are about to overdrive the path, prevents code from crashing due to an array out of bounds exception
        Vector2D baseVel = super.get(pos);
        return baseVel.setMagnitude(waypoints.getSpeed(newT));
    }

    /**
     * A method to show whether the auto has been completed yet or not
     * **/
    public boolean finished() {
        return t >= spline.getNumPieces() - .01;
    }

    /**
     * A method that combines the standard following method as has been described in the documentation file,
     * but also uses the required follower point algorithm if within a follower point's outer radius
     **/
    public Vector2D get(Vector2D pos) {
        // Updates the t value
        double tempT = findTOnSpline(pos);
        // Checks if we have just completed a required follower point
        if (requiredFollowerPoints.isInner(tempT)) {
            return superget(pos);
        }
        // Checks if we need to act according to a required follower point
        if (requiredFollowerPoints.isInOuter(t)) {
            Vector2D v = spline.get(requiredFollowerPoints.findNearestT(tempT)).toVector2D();
            return v.subtract(pos).setMagnitude(waypoints.getSpeed(tempT));
        }
        // If we are not in either radius of a required follower return the standard follower output
        return superget(pos);
    }


    /***
     * Test code for this class
     * **/
    public static void main(String[] args) {

        Waypoints w = new Waypoints(new Waypoint(0, () -> {
        }, .4), new Waypoint(0.5500000000000003, () -> {
        }, .2), new Waypoint(0.6100000000000003, () -> {
        }, .2, true));

        Waypoints w2 = new Waypoints(new Waypoint(2, () -> {
        }, .4), new Waypoint(0, () -> {
        }, .2), new Waypoint(4, () -> {
        }, .2), new Waypoint(6, () -> {
        }, .2), new Waypoint(7, () -> {
        }, .3));

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


        System.out.println(findTOnSplineStatic(new Vector2D(1, 2), spline, .01));
        System.out.println(findTOnSplineStatic(new Vector2D(3, 4), spline, .01));
        System.out.println(findTOnSplineStatic(new Vector2D(2.5, 5.5), spline, .01));


        RequiredFollowerPoint[] r = new RequiredFollowerPoint[]{new RequiredFollowerPoint(.5, 0)};
        PosExtraEnhancedSplineFollower posBasicSplineFollower = new PosExtraEnhancedSplineFollower(spline, .1, .01, .5, .01, w, new RequiredFollowerPoints(.1, .01, r));

        Vector2D pos = new Vector2D(5, 0);
        for (int j = 0; j < 1000 && !posBasicSplineFollower.finished(); j++) {
            try {
                pos.add(posBasicSplineFollower.get(pos));
                System.out.println(pos);
            } catch (Exception e) {
            }
        }
    }

    // Merely allows for findTOnSpline to be called from a static context
    public static double findTOnSplineStatic(Vector2D pos, Spline spline, double splineRes) {
        Vector2D min = new Vector2D(99999.0, 99999.0);
        double newT = 0.0;
        new Vector2D();

        for (double i = 0; i < spline.getNumPieces(); i += splineRes) {
            double b = i > 0.0 ? i : 1.0E-4;
            Vector2D check = spline.get(b < (double) spline.getNumPieces() ? b : (double) spline.getNumPieces() - 1.0E-4).toVector2D().subtract(pos);
            if (check.getMagnitude() < min.getMagnitude()) {
                min = check.clone();
                newT = b < (double) spline.getNumPieces() ? b : (double) spline.getNumPieces() - 1.0E-4;
            }
        }
        return newT;
    }


}