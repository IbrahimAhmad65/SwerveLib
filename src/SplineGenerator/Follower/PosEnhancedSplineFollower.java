package SplineGenerator.Follower;

import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;
import main.Vector2D;

public class PosEnhancedSplineFollower extends PosBasicSplineFollower implements Follower {

    public PosEnhancedSplineFollower(Spline spline, Waypoints w) {
        this(spline, .1, .01, .5, .01, w);
    }

    public PosEnhancedSplineFollower(Spline spline, double splineR, double splineRes, double maxToVel, double toVelSearchRadius, Waypoints waypoints) {
        super(spline, splineR, splineRes, 0, maxToVel, waypoints);
        waypoints.addWaypoint(new Waypoint(spline.pieces - .00001, () -> {
        }, 0));

        double minOffSum = 99999;
        double tToVel = 0;
        super.forVel = .01;
        for (double j = 0; j < maxToVel; j += toVelSearchRadius) {
            double offSum = 0;
            Vector2D pos = new Vector2D();
            super.toVel = j;
            boolean exit = false;
            t = 0;
            for (int k = 0; k < 1000 && !exit; k++) {
                pos.add(this.get(pos));
                offSum += pos.clone().subtract(findPosOnSpline(pos)).getMagnitude();
                if (findTOnSpline(pos) > spline.pieces - .001) {
                    exit = true;
                }
//                System.out.println(offSum);
            }
            if (offSum < minOffSum) {
                tToVel = super.toVel;
                minOffSum = offSum;
            }
        }
        super.toVel = tToVel;
        System.out.println("toVel " + toVel);
        System.out.println("forVel " + forVel);
        t = 0;
    }

    public PosEnhancedSplineFollower(Spline spline, double splineR, double splineRes, double maxToVel, double toVelSearchRadius, Vector2D initialPos, Waypoints waypoints) {
        super(spline, splineR, splineRes, 0, maxToVel, waypoints);
        waypoints.addWaypoint(new Waypoint(spline.pieces - .00001, () -> {
        }, 0));

        double minOffSum = 99999;
        double tToVel = 0;
        super.forVel = .01;
        for (double j = 0; j < maxToVel; j += toVelSearchRadius) {
            double offSum = 0;
            Vector2D pos = initialPos.clone();
            super.toVel = j;
            boolean exit = false;
            t = 0;
            for (int k = 0; k < 1000 && !exit; k++) {
                pos.add(this.get(pos));
                offSum += pos.clone().subtract(findPosOnSpline(pos)).getMagnitude();
                if (findTOnSpline(pos) > spline.pieces - .001) {
                    exit = true;
                }
            }
            if (offSum < minOffSum) {
                tToVel = super.toVel;
                minOffSum = offSum;
            }
        }
        super.toVel = tToVel;
        System.out.println("toVel " + toVel);
        System.out.println("forVel " + forVel);
        t = 0;
    }

    public PosEnhancedSplineFollower(Spline spline, Vector2D initalPos, Waypoints w) {
        this(spline, .1, .01, .5, .01, initalPos, w);

    }

    // base speed is .2
    @Override
    public Vector2D get(Vector2D pos) {
        double newT = findTOnSpline(pos);
//        System.out.println("Pos " + pos + " t: " + t);
//        System.out.println(pos);
        if (newT >= super.spline.pieces - .01) {
            return new Vector2D(0, 0);
        }
        Vector2D baseVel = super.get(pos);

        return baseVel.setMagnitude(waypoints.getSpeed(t));
    }

    public boolean finished() {
        return t >= spline.pieces - .01;
    }


    public static void main(String[] args) {
        Waypoints w = new Waypoints(new Waypoint(2, () -> {
        }, .4), new Waypoint(0, () -> {
        }, .2), new Waypoint(4, () -> {
        }, .2), new Waypoint(6, () -> {
        }, .2), new Waypoint(7, () -> {
        }, .3));

        Waypoints w2 = new Waypoints(new Waypoint(2, () -> {
        }, .4), new Waypoint(0, () -> {
        }, .2), new Waypoint(4, () -> {
        }, .2), new Waypoint(6, () -> {
        }, .2), new Waypoint(7, () -> {
        }, .3));

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

        PosEnhancedSplineFollower posBasicSplineFollower = new PosEnhancedSplineFollower(spline, .1, .01, .5, .01, w);

        Vector2D pos = new Vector2D(5, 0);
        for (int j = 0; j < 1000 && !posBasicSplineFollower.finished(); j++) {
            try {
                pos.add(posBasicSplineFollower.get(pos));
                System.out.println(pos);
            } catch (Exception e) {
            }
        }
    }

}