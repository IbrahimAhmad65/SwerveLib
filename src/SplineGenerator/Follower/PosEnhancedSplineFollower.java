package SplineGenerator.Follower;

import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;
import main.Vector2D;

public class PosEnhancedSplineFollower extends PosBasicSplineFollower implements Follower {

    public PosEnhancedSplineFollower(Spline spline, double splineR, double splineRes, double maxForVel, double maxToVel, double forVelSearchRadius, double toVelSearchRadius, double stoppingRadius, Waypoints waypoints) {
        super(spline, splineR, splineRes, maxForVel, maxToVel, waypoints);
        waypoints.addWaypoint(new Waypoint(spline.pieces, () -> {
        }, 0));
        double minOffSum = 99999;
        super.toVel = -1;
        super.forVel = -1;
        for (double i = forVelSearchRadius; i < maxForVel; i++) {
            for (double j = toVelSearchRadius; j < maxToVel; j++) {
                double offSum = 0;
                Vector2D pos = new Vector2D();
                for (int k = 0; k < 1000; k++) {
                    pos.add(this.get(pos));
                    offSum += pos.clone().subtract(findPosOnSpline(pos)).getMagnitude();
                }
                if( offSum < minOffSum){
                    super.toVel = j;
                    super.forVel = i;
                }
            }
        }
    }

    // base speed is .2
    @Override
    public Vector2D get(Vector2D pos) {
        double t = super.findTOnSpline(pos);
        Vector2D baseVel = super.get(pos);
        if (t >= super.spline.pieces) {
            baseVel.setMagnitude(0);
        }
        baseVel.setMagnitude(super.waypoints.getSpeed(t));
        return baseVel;
    }

    public static void main(String[] args) {
        Waypoints w = new Waypoints(new Waypoint(7, () -> {
        }, .2), new Waypoint(0, () -> {
        }, .2));


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

        PosEnhancedSplineFollower posBasicSplineFollower = new PosEnhancedSplineFollower(spline,
                .1, .01, .01, .25,.001,.01,.5, w);
        Vector2D pos = new Vector2D(5, 0);
        for (int j = 0; j < 1000; j++) {
            try {
                pos.add(posBasicSplineFollower.get(pos));
            } catch (Exception e) {
                System.out.println("oof");
            }
        }
    }

}
