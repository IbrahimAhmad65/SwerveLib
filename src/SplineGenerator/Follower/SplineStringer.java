package SplineGenerator.Follower;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

//package SplineGenerator.Follower;
//
//import SplineGenerator.GUI.SplineDisplay;
//import SplineGenerator.Splines.PolynomicSpline;
//import SplineGenerator.Splines.Spline;
//import SplineGenerator.Util.DControlPoint;
//import SplineGenerator.Util.DDirection;
//import SplineGenerator.Util.DVector;
//import SplineGenerator.Util.InterpolationInfo;
//import main.LinearlyInterpLUT;
//import main.Vector2D;
//
///**
// * Class that contains multiple followable objects, and connects them back to back,
// * the end of one spline's position must be the beginning of the next one.
// * It is recommended to have the end of one spline, and the beginning of another have the same derivatives for smoothness,
// * but is not required for this class to function
// * <p>
// * This is a followable object, and thus may be passed to the spline follower classes
// */
//public class SplineStringer implements Followable {
//    // The array Of Splines
//    private Followable[] splines;
//    // The array of the spline lengths
//    private double[] splineLengths;
//    // The array of the spline lengths, but it cumulates
//    private double[] cascadedSplineLength;
//    // LUTs used for mapping T values
//    private LinearlyInterpLUT fromTtoCascadedSplineLengths;
//    private LinearlyInterpLUT fromCascadedSplineLengthsToT;
//    // Array used for T mapping
//    private double[] goUp;
//
//    public SplineStringer(Followable... splines) {
//        this.splines = splines;
//        // Preparing Arrays to be passed into the LUTS
//        splineLengths = new double[splines.length];
//        goUp = new double[splines.length];
//        cascadedSplineLength = new double[splines.length];
//        for (int i = 0; i < splines.length; i++) {
//            goUp[i] = i;
//            splineLengths[i] = splines[i].getNumPieces();
//        }
//        cascadedSplineLength[0] = 0;
//        for (int i = 1; i < splines.length; i++) {
//            cascadedSplineLength[i] = cascadedSplineLength[i - 1] + splines[i - 1].getNumPieces();
//        }
//
//        // Instantiating LUTS for T mapping
//        fromTtoCascadedSplineLengths = new LinearlyInterpLUT(goUp, cascadedSplineLength);
//        fromCascadedSplineLengthsToT = new LinearlyInterpLUT(cascadedSplineLength, goUp);
//    }
//
//    /**
//     * Returns a position given a t value
//     */
//    public DVector get(double t) {
//        return splines[mapTIn(t)].get(mapTOut(t)).toVector();
//    }
//
//    /**
//     * Returns the gradient of the splines given a t value
//     **/
//    public DVector evaluateDerivative(double t, int derivative) {
//        return splines[mapTIn(t)].evaluateDerivative(mapTOut(t), derivative).toVector();
//    }
//
//    /**
//     * Maps t for a specific spline to a global t
//     */
//    private double mapTOut(double t) {
//        return t - (int) fromTtoCascadedSplineLengths.get(mapTIn(t));
//    }
//
//    /**
//     * Maps a global t for all the splines to a t for a specific spline
//     */
//    private int mapTIn(double t) {
//        return (int) fromCascadedSplineLengthsToT.get(t);
//    }
//
//    /**
//     * Returns the number of total segments of the splines, same as spline1.getNumPieces() + spline2.getNumPieces() + spline3.getNumPieces()
//     ***/
//    public int getNumPieces() {
////        int sum = 0;
////        for (Followable f: splines) {
////            sum += f.getNumPieces();
////        }
//        return (int) cascadedSplineLength[cascadedSplineLength.length - 1] + splines[splines.length - 1].getNumPieces();
////        return sum;
//    }
//
//    // Test Code for this class
//    public static void main(String[] args) {
//        PolynomicSpline spline = new PolynomicSpline(2, 1);
//        double i = 5;
//        spline.addControlPoint(new DControlPoint(new DVector(0.0, 0.0)));
//        spline.addControlPoint(new DControlPoint(new DVector(i, i)));
//        spline.addControlPoint(new DControlPoint(new DVector(2.0 * i, 0.0)));
//        spline.addControlPoint(new DControlPoint(new DVector(i, -i)));
//        spline.addControlPoint(new DControlPoint(new DVector(0.0, 0.0)));
//        spline.addControlPoint(new DControlPoint(new DVector(-i, i)));
//        spline.addControlPoint(new DControlPoint(new DVector(-2.0 * i, 0.0)));
//        spline.addControlPoint(new DControlPoint(new DVector(-i, -i)));
//        spline.addControlPoint(new DControlPoint(new DVector(0, 0.0)));
//        spline.closed = false;
//
//
//        InterpolationInfo c1 = new InterpolationInfo();
////        c1.interpolationType = Spline.InterpolationType.None;
////        c1.endBehavior = Spline.EndBehavior.None;
////        spline.interpolationTypes.add(c1);
//
//
//        spline.generate();
//
//
//        spline.takeNextDerivative();
//
//        c1.interpolationType = Spline.InterpolationType.Linked;
//        c1.endBehavior = Spline.EndBehavior.Hermite;
////        spline.interpolationTypes.add(c1);
//        InterpolationInfo c2 = new InterpolationInfo();
//        c2.interpolationType = Spline.InterpolationType.Linked;
//        c2.endBehavior = Spline.EndBehavior.Hermite;
////        spline.interpolationTypes.add(c2);
//        InterpolationInfo c3 = new InterpolationInfo();
//        c3.interpolationType = Spline.InterpolationType.Linked;
//        c3.endBehavior = Spline.EndBehavior.None;
////        spline.interpolationTypes.add(c3);
//        InterpolationInfo c4 = new InterpolationInfo();
//        c4.interpolationType = Spline.InterpolationType.Linked;
//        c4.endBehavior = Spline.EndBehavior.None;
////        spline.interpolationTypes.add(c4);
//        //----
//        PolynomicSpline spline2 = new PolynomicSpline(2);
//        spline2.setPolynomicOrder(5);
//        spline2.addControlPoint(new DControlPoint(new DVector(0.0, 0.0), new DDirection(1.0, 1.0), new DDirection(0.0, 0.0)));
//        spline2.addControlPoint(new DControlPoint(new DVector(1, 0.0), new DDirection(3.0, 0.0), new DDirection(0.0, 0.0)));
//        spline2.closed = false;
//
//        spline2.interpolationTypes.add(c1);
//        spline2.interpolationTypes.add(c2);
//        spline2.interpolationTypes.add(c3);
//        spline2.interpolationTypes.add(c4);
//
//        spline2.generate();
//        spline2.takeNextDerivative();
//        //--//
//        SplineStringer splineStringer = new SplineStringer(spline, spline2);
//
//        Waypoints w = new Waypoints(new Waypoint(0, () -> {
//        }, .2), new Waypoint(7, () -> {
//        }, .2));
//
//        RequiredFollowerPoint[] r = new RequiredFollowerPoint[]{new RequiredFollowerPoint(0, 0), new RequiredFollowerPoint(9, 0)};
//        Follower posBasicSplineFollower = new GoodestFollower(splineStringer, .1, .01, w,  new RequiredFollowerPoints(.1, .01, r));
//
//        Vector2D pos = spline.get(0).toVector2D();
//
//
////        SplineDisplay splineDisplay = new SplineDisplay(spline, 0, 1, 500, 500);
////        splineDisplay.displayables.add(posBasicSplineFollower);
//        for (int j = 0; j < 2500 && !posBasicSplineFollower.finished(); j++) {
//            pos.add(posBasicSplineFollower.get(pos).scale(1));
//            System.out.println(pos);
//
////            System.out.println(splineStringer.get(j*splineStringer.getNumPieces()/250.));
//
//        }
//    }
//}
public class SplineStringer implements Followable {
    private Followable[] followables;
    private int[] cascL;
    private int[] cascH;
    public SplineStringer(Followable... splines){
        this.followables = splines;
        cascL = new int[splines.length];
        cascH = new int[splines.length];
        cascL[0] = 0;
        cascH[0] = splines[0].getNumPieces();
        for (int i = 1; i < cascL.length; i++) {
            cascH[i] = cascH[i-1] + splines[i].getNumPieces();
        }
        for (int i = 1; i < cascL.length; i++) {
            cascL[i] = cascH[i-1];
        }
        for (int n: cascL) {
            System.out.println(n);
        }
        for (int n: cascH) {
            System.out.println(n);
        }
    }

    @Override
    public int getNumPieces() {
        return cascH[cascH.length-1];
    }

    @Override
    public DPoint get(double t) {
        for (int i = 0; i < cascL.length; i++) {
            if(Math.floor(t)+.1 > cascL[i] && t < cascH[i]){
                return followables[i].get(t - cascL[i]);
            }
        }
        return followables[followables.length-1].get(followables[followables.length-1].getNumPieces());
    }

    @Override
    public DVector evaluateDerivative(double t, int dimension) {
        for (int i = 0; i < cascL.length; i++) {
            if(Math.floor(t)+.1 > cascL[i] && t < cascH[i]){
                return followables[i].evaluateDerivative(t - cascL[i],dimension);
            }
        }
        return followables[followables.length - 1].evaluateDerivative(followables[followables.length -1].getNumPieces(),dimension);
    }
}


