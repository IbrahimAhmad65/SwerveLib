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
import org.w3c.dom.Text;

public class FullSimulator {
    private AutoScheduler autoScheduler;
    private Vector2D pos;
    private GoodestFollower goodestFollower;

    private SplineDisplay splineDisplay;

    private double scalePerTick;
    private double timePerTick;

    private TextDisplay textDisplay;
    private DisplayData displayData;

    public FullSimulator(AutoScheduler autoScheduler, String autoName, double scalePerTick, double timePerTick) {
        this.autoScheduler = autoScheduler;
        splineDisplay = new SplineDisplay(autoScheduler.getRegistry().get(autoName).getSpline(), 0, 1, 1300, 700);
        splineDisplay.displayables.add((GoodestFollower) autoScheduler.getRegistry().get(autoName).getFollower());
        pos = new Vector2D(0, 0);
        autoScheduler.setPosSupplier(pos::clone);
        goodestFollower = (GoodestFollower) autoScheduler.getRegistry().get(autoName).getFollower();
        this.timePerTick = timePerTick;
        this.scalePerTick = scalePerTick;
        displayData = new DisplayData();


        textDisplay = new TextDisplay(() -> {
            return displayData;
        }, new Vector2D(3, 20), 15);
        splineDisplay.displayables.add(textDisplay);

        splineDisplay.display();
    }

    public void run() throws Exception {

        displayData.pos = pos.clone();
        displayData.t = goodestFollower.getT();
        displayData.angle = goodestFollower.getSpin(pos);
        displayData.waypoints = autoScheduler.getRegistry().get(autoScheduler.getCurrentAuto()).getFollower().getWaypoints();
        autoScheduler.runWaypoints();

        if (!goodestFollower.finished()) {
            pos.add(goodestFollower.get(pos).scale(scalePerTick));

        }
        Thread.sleep((long) timePerTick);
        splineDisplay.repaint();

    }

    public static void main(String[] args) throws Exception {
//        PolynomicSpline spline = new PolynomicSpline(2);
//        spline.setPolynomicOrder(5);
//        double i = 1.0;
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{0.0, 0.0}), new DDirection(new double[]{1.0, 1.0}), new DDirection(new double[]{0.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{i, i}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{2.0 * i, 0.0}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{i, -i}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{0.0, 0.0}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{-i, i}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{-2.0 * i, 0.0}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{-i, -i}), new DDirection(new double[]{1.0, 0.0})}));
//        spline.addControlPoint(new DControlPoint(new DVector[]{new DVector(new double[]{0.0, 0.0}), new DDirection(new double[]{0.0, 0.0}), new DDirection(new double[]{0.0, 0.0})}));
//        spline.closed = false;
//        InterpolationInfo c1 = new InterpolationInfo();
//        c1.interpolationType = Spline.InterpolationType.Linked;
//        c1.endBehavior = Spline.EndBehavior.Hermite;
//        spline.interpolationTypes.add(c1);
//        InterpolationInfo c2 = new InterpolationInfo();
//        c2.interpolationType = Spline.InterpolationType.Linked;
//        c2.endBehavior = Spline.EndBehavior.Hermite;
//        spline.interpolationTypes.add(c2);
//        InterpolationInfo c3 = new InterpolationInfo();
//        c3.interpolationType = Spline.InterpolationType.Linked;
//        c3.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c3);
//        InterpolationInfo c4 = new InterpolationInfo();
//        c4.interpolationType = Spline.InterpolationType.Linked;
//        c4.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c4);
//        spline.generate();
//        spline.takeNextDerivative();
//
//        Waypoints w = new Waypoints(new Waypoint(0, () -> {
//        }, 2), new Waypoint(2, () -> {
//        }, .2), new Waypoint(4, () -> {
//            System.out.println("hi");
//        }, .2,true), new Waypoint(6, () -> {
//        }, .2), new Waypoint(7, () -> {
//        }, 1));


        PolynomicSpline spline = new PolynomicSpline(2, 1);
        double i = 5;
        spline.addControlPoint(new DControlPoint(new DVector(0.0, 0.0)));
        spline.addControlPoint(new DControlPoint(new DVector(i, i)));
        spline.addControlPoint(new DControlPoint(new DVector(2.0 * i, 0.0)));
        spline.addControlPoint(new DControlPoint(new DVector(0, 0.0)));
        spline.closed = false;


        InterpolationInfo c1 = new InterpolationInfo();
//        c1.interpolationType = Spline.InterpolationType.None;
//        c1.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c1);


        spline.generate();


        spline.takeNextDerivative();

        c1.interpolationType = Spline.InterpolationType.Linked;
        c1.endBehavior = Spline.EndBehavior.Hermite;
        InterpolationInfo c2 = new InterpolationInfo();
        c2.interpolationType = Spline.InterpolationType.Linked;
        c2.endBehavior = Spline.EndBehavior.Hermite;
        InterpolationInfo c3 = new InterpolationInfo();
        c3.interpolationType = Spline.InterpolationType.Linked;
        c3.endBehavior = Spline.EndBehavior.None;
        InterpolationInfo c4 = new InterpolationInfo();
        c4.interpolationType = Spline.InterpolationType.Linked;
        c4.endBehavior = Spline.EndBehavior.None;
        PolynomicSpline spline2 = new PolynomicSpline(2);
        spline2.setPolynomicOrder(5);
        spline2.addControlPoint(new DControlPoint(new DVector(0.0, 0.0), new DDirection(1.0, 1.0), new DDirection(0.0, 0.0)));
        spline2.addControlPoint(new DControlPoint(new DVector(1, 0.0), new DDirection(3.0, 1), new DDirection(0.0, 0.0)));
        spline2.closed = false;

        spline2.interpolationTypes.add(c1);
        spline2.interpolationTypes.add(c2);
        spline2.interpolationTypes.add(c3);
        spline2.interpolationTypes.add(c4);

        spline2.generate();
        spline2.takeNextDerivative();
        //--//
        SplineStringer splineStringer = new SplineStringer(spline, spline2,spline);

        Waypoints w = new Waypoints(splineStringer ,new Waypoint(0, () -> {}, .2)
                , new Waypoint(3, () -> {}, .3)
//                , new Waypoint(3, () -> {}, .5)
        );


        Vector2D pos = new Vector2D();

        RequiredFollowerPoint[] r = new RequiredFollowerPoint[]{new RequiredFollowerPoint(0, Math.PI), new RequiredFollowerPoint(4, -Math.PI)};
        GoodestFollower goodestFollower = new GoodestFollower(splineStringer, .1, .01, w, new RequiredFollowerPoints(.1, .0, r));

        SingleAuto singleAuto = new SingleAuto("empty string", splineStringer, goodestFollower);
        AutoRegistry autoRegistry = new AutoRegistry();
        autoRegistry.addAuto(singleAuto.getAutoName(), singleAuto);
        AutoScheduler autoScheduler = new AutoScheduler(autoRegistry, pos::clone);
        autoScheduler.setCurrentAuto(singleAuto.getAutoName());
        FullSimulator fullSimulator = new FullSimulator(autoScheduler, singleAuto.getAutoName(), .1, 10);
        while (true) {
            fullSimulator.run();
        }
    }


}
