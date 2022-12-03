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

public class FullSimulator {
    private AutoScheduler autoScheduler;
    private Vector2D pos;
    private GoodestFollower goodestFollower;

    private SplineDisplay splineDisplay;

    public FullSimulator(AutoScheduler autoScheduler, String autoName) {
        this.autoScheduler = autoScheduler;
        splineDisplay = new SplineDisplay(autoScheduler.getRegistry().get(autoName).getSpline(), 0, 1, 1300, 700);
        splineDisplay.displayables.add((GoodestFollower) autoScheduler.getRegistry().get(autoName).getFollower());
        splineDisplay.display();
        pos = new Vector2D(0, 0);
        autoScheduler.setPosSupplier(pos::clone);
        goodestFollower = (GoodestFollower) autoScheduler.getRegistry().get(autoName).getFollower();
    }

    public void run() {

        if (!goodestFollower.finished()) {
            pos.add(goodestFollower.get(pos).scale(.001));
            System.out.println(pos);
        }
        splineDisplay.repaint();

    }

    public static void main(String[] args) {
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

        Waypoints w = new Waypoints(new Waypoint(2, () -> {
        }, .4), new Waypoint(0, () -> {
        }, .2), new Waypoint(4, () -> {
        }, .2,true), new Waypoint(6, () -> {
        }, .2), new Waypoint(7, () -> {
        }, .3));

        Vector2D pos = new Vector2D();

        RequiredFollowerPoint[] r = new RequiredFollowerPoint[]{new RequiredFollowerPoint(0,1),new RequiredFollowerPoint(9,3)};
        GoodestFollower goodestFollower = new GoodestFollower(spline,.1,.01,w,new RequiredFollowerPoints(.1,.0,r));

        SingleAuto singleAuto = new SingleAuto("cat",spline,goodestFollower);
        AutoRegistry autoRegistry = new AutoRegistry();
        autoRegistry.addAuto(singleAuto.getAutoName(), singleAuto);
        AutoScheduler autoScheduler = new AutoScheduler(autoRegistry, pos::clone);
        autoScheduler.setCurrentAuto(singleAuto.getAutoName());
        FullSimulator fullSimulator = new FullSimulator(autoScheduler,singleAuto.getAutoName());
        while (true){
            fullSimulator.run();
        }
    }


}
