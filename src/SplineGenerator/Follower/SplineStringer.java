package SplineGenerator.Follower;

import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;
import main.LinearlyInterpLUT;
import main.Vector2D;

public class SplineStringer implements Followable{
    private Spline[] splines;
    private double[] splineLengths;

    private double[] cascadedSplineLength;

    private LinearlyInterpLUT linearlyInterpLUT;
    private LinearlyInterpLUT linearlyInterpLUT2;
    private double[] goUp;
    public SplineStringer(Spline... splines){
        this.splines = splines;
        splineLengths = new double[splines.length];
        goUp = new double[splines.length];
        cascadedSplineLength = new double[splines.length];
        for (int i = 0; i < splines.length; i++) {
            goUp[i] = i;
            splineLengths[i] = splines[i].getNumPieces();
        }
        cascadedSplineLength[0] = 0;
        for (int i = 1; i < splines.length; i++) {
            cascadedSplineLength[i] = cascadedSplineLength[i-1] + splines[i-1].getNumPieces();
        }

        linearlyInterpLUT = new LinearlyInterpLUT(goUp, cascadedSplineLength);
        linearlyInterpLUT2 = new LinearlyInterpLUT(cascadedSplineLength,goUp);
        }
    //
    public DVector get(double t){
        return splines[mapTIn(t)].get(mapTOut(t)).toVector();
    }

    public DVector evaluateDerivative(double t, int derivative) {
        return splines[mapTIn(t)].evaluateDerivative(mapTOut(t),derivative).toVector();
    }


    public double mapTOut(double t){
        return t - (int)linearlyInterpLUT.get(mapTIn(t));
    }

    public int mapTIn(double t){
        return (int)linearlyInterpLUT2.get(t);
    }

    public static void main(String[] args) {

        //----
        PolynomicSpline spline = new PolynomicSpline(2);
        spline.setPolynomicOrder(5);
        double i = 1.0;
        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{0.0, 0.0}), new DDirection(new double[]{1.0, 1.0}), new DDirection(new double[]{0.0, 0.0})}));
        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{i, i}), new DDirection(new double[]{1.0, 0.0})}));
        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{2.0 * i, 0.0}), new DDirection(new double[]{1.0, 0.0})}));
        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{i, -i}), new DDirection(new double[]{1.0, 0.0})}));
        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{0.0, 0.0}), new DDirection(new double[]{1.0, 0.0})}));
        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{-i, i}), new DDirection(new double[]{1.0, 0.0})}));
        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{-2.0 * i, 0.0}), new DDirection(new double[]{1.0, 0.0})}));
        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{-i, -i}), new DDirection(new double[]{1.0, 0.0})}));
        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{0.0, 0.0}), new DDirection(new double[]{0.0, 0.0}), new DDirection(new double[]{0.0, 0.0})}));
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
        //----
        PolynomicSpline spline2 = new PolynomicSpline(2);
        spline2.setPolynomicOrder(5);
        spline2.addControlPoint(new DControlPoint(new DVector(0.0, 0.0),
                new DDirection(1.0, 1.0), new DDirection(0.0, 0.0)));
        spline2.addControlPoint(new DControlPoint(new DVector(1, 0.0),
                new DDirection(3.0, 0.0), new DDirection(0.0, 0.0)));
        spline2.closed = false;

        spline2.interpolationTypes.add(c1);
        spline2.interpolationTypes.add(c2);
        spline2.interpolationTypes.add(c3);
        spline2.interpolationTypes.add(c4);

        spline2.generate();
        spline2.takeNextDerivative();
        //--//
        SplineStringer splineStringer = new SplineStringer(spline,spline2);
        splineStringer.get(8.5);

        Waypoints w = new Waypoints(new Waypoint(2, () -> {
        }, .4), new Waypoint(0, () -> {
        }, .2), new Waypoint(4, () -> {
        }, .2,true), new Waypoint(6, () -> {
        }, .2), new Waypoint(7, () -> {
        }, .3));

        RequiredFollowerPoint[] r = new RequiredFollowerPoint[]{new RequiredFollowerPoint(.5)};
        PosExtraEnhancedSplineFollower posBasicSplineFollower = new PosExtraEnhancedSplineFollower(splineStringer, .1
                , .01, .5, .01, w, new RequiredFollowerPoints(.1,.01,r));

        Vector2D pos = new Vector2D(5, 0);
        for (int j = 0; j < 1000 && !posBasicSplineFollower.finished(); j++) {
            try {
                pos.add(posBasicSplineFollower.get(pos));
                System.out.println(pos);
            } catch (Exception e) {
            }
        }
    }

    public int getNumPieces() {
        return (int)cascadedSplineLength[cascadedSplineLength.length-1] + splines[splines.length-1].getNumPieces();
    }
}
