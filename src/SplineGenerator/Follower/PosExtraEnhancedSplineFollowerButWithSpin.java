
package SplineGenerator.Follower;

        import BSpline.BSplineH;
        import BSpline.SplinePoint;
        import SplineGenerator.Splines.PolynomicSpline;
        import SplineGenerator.Splines.Spline;
        import SplineGenerator.Util.DControlPoint;
        import SplineGenerator.Util.DDirection;
        import SplineGenerator.Util.DVector;
        import SplineGenerator.Util.InterpolationInfo;
        import main.Vector2D;


public class PosExtraEnhancedSplineFollowerButWithSpin extends PosBasicSplineFollower {
    private RequiredFollowerPoints requiredFollowerPoints;
    private BSplineH angleSpline;
    public PosExtraEnhancedSplineFollowerButWithSpin(Followable spline, double splineR, double splineRes,
                                          double maxToVel, double toVelSearchRadius, Waypoints waypoints, RequiredFollowerPoints r) {
        super(spline, splineR, splineRes, maxToVel, toVelSearchRadius, waypoints);
        this.requiredFollowerPoints = r;

        waypoints.addWaypoint(new Waypoint(spline.getNumPieces() - .00001, () -> {
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
            for (int k = 0; k < 1000 && !finished(); k++) {
                pos.add(this.get(pos));
                offSum += pos.clone().subtract(findPosOnSpline(pos)).getMagnitude();
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
        SplinePoint[] splinePoints =new SplinePoint[requiredFollowerPoints.getRequiredFollowerPoints().length];
        double[] arr = requiredFollowerPoints.getAnglesOrdered();
        double[] arr2 = requiredFollowerPoints.getTOrdered();
        for (int i = 0; i < splinePoints.length; i++) {
            splinePoints[i] = new SplinePoint(new Vector2D(arr2[i],arr[i]), new Vector2D(1,0));
        }
        angleSpline = new BSplineH(.1,.1,splinePoints);
    }

    public Vector2D superget(Vector2D pos) {
        double newT = findTOnSpline(pos);
//        System.out.println("Pos " + pos + " t: " + t);
//        System.out.println(pos);
        if (newT >= super.spline.getNumPieces() - .01) {
            return new Vector2D(0, 0);
        }

        Vector2D baseVel = super.get(pos);
        return baseVel.setMagnitude(waypoints.getSpeed(newT));
    }

    public boolean finished() {
        return t >= spline.getNumPieces() - .01;
    }

    public Vector2D get(Vector2D pos){
        double tempT = findTOnSpline(pos);
        if(requiredFollowerPoints.isInner(tempT)){
            return superget(pos);
        }
        if(requiredFollowerPoints.isInOuter(t)){
            Vector2D v = spline.get(requiredFollowerPoints.findNearestT(tempT)).toVector2D();
            return v.subtract(pos).setMagnitude(waypoints.getSpeed(tempT));
        }
        return superget(pos);
    }

    public double getSpin(Vector2D pos){
        double tempT = findTOnSpline(pos);
        return angleSpline.evaluatePos(tempT).getY();
    }

    public static void main(String[] args) {

        //  2 //positions cannot be negative numbers but thats okay :)
        //  3 ControlPoints
        //  4 {
        //  5 0,0,3,1
        //  6 6,8,2,5
        //  7 2,.01,2,5
        //  8 }
        //  9 WayPoints
        // 10 {
        // 11 1,2,.2,print
        // 12 3,4.5,.3,print
        // 13 2.5,5.5,.2,none
        // 14 }
        // 15 Required
        // 16 {
        // 17 1,2
        // 18 }

        //0.37000000000000016
        //0.5500000000000003
        //0.6100000000000003

        Waypoints w = new Waypoints(new Waypoint(0.37000000000000016, () -> {
        }, .4), new Waypoint(0.5500000000000003, () -> {
        }, .2), new Waypoint(0.6100000000000003, () -> {
        }, .2,true));

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


        System.out.println(findTOnSplineCool(new Vector2D(1,2),spline,.01));
        System.out.println(findTOnSplineCool(new Vector2D(3,4),spline,.01));
        System.out.println(findTOnSplineCool(new Vector2D(2.5,5.5),spline,.01));



        RequiredFollowerPoint[] r = new RequiredFollowerPoint[]{new RequiredFollowerPoint(.5,0)};
        PosExtraEnhancedSplineFollower posBasicSplineFollower = new PosExtraEnhancedSplineFollower(spline, .1
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

    public static double findTOnSplineCool(Vector2D pos, Spline spline, double splineRes) {
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
