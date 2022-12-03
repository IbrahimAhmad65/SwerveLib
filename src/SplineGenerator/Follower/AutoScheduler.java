package SplineGenerator.Follower;

import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;
import main.Vector2D;

import java.lang.reflect.Array;
import java.util.function.Supplier;
// Class for executing an auto
public class AutoScheduler {
    // Registry
    private AutoRegistry registry;
    // Current Selected Auto
    private String currentAuto = "";
    // Supplier for the position of the robot
    private Supplier<Vector2D> posSupplier;
    public AutoScheduler(AutoRegistry registry, Supplier<Vector2D> posSupplier){
        this.registry = registry;
        this.posSupplier = posSupplier;
    }

    // Sets the position supplier
    public void setPosSupplier(Supplier<Vector2D> posSupplier) {
        this.posSupplier = posSupplier;
    }

    // Declares whether the auto has been completed or not
    public boolean isFinished(){
        return registry.get(currentAuto).getFollower().finished();
    }

    // Sets the registry
    public void setRegistry(AutoRegistry registry){
        this.registry = registry;
    }

    // Sets the name of the current selected auto
    public void setCurrentAuto(String name){
        currentAuto = name;
    }

    public String getCurrentAuto() {
        return currentAuto;
    }

    //Returns the autoRegistry
    public AutoRegistry getRegistry() {
        return registry;
    }

    // Returns the velocity of the selected auto's follower
    public Vector2D getVelocity(){
        return registry.get(currentAuto).getFollower().get(posSupplier.get());
    }

    // Returns the angle from the selected auto's follower
    public double getSpin(){return registry.get(currentAuto).getFollower().getSpin(posSupplier.get());}

    // Executes the waypoints
    public void runWaypoints(){
        Follower follower = registry.get(currentAuto).getFollower();
        double t = follower.findTOnSpline(posSupplier.get());
        Waypoint[] waypoints = new Waypoint[follower.getWaypoints().getWaypoints().size()];
        follower.getWaypoints().getWaypoints().toArray(waypoints);
        for (Waypoint i : waypoints) {
            i.run(t);
        }
    }

    // Tester Code
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
        PosExtraEnhancedSplineFollowerButWithSpin posBasicSplineFollower = new PosExtraEnhancedSplineFollowerButWithSpin(spline, .1
                , .01, .5, .01, w, new RequiredFollowerPoints(.1,.01,r));
        SingleAuto singleAuto = new SingleAuto("cat",spline,posBasicSplineFollower);
        AutoRegistry autoRegistry = new AutoRegistry();
        autoRegistry.addAuto(singleAuto.getAutoName(), singleAuto);
        AutoScheduler autoScheduler = new AutoScheduler(autoRegistry, pos::clone);
        autoScheduler.setCurrentAuto(singleAuto.getAutoName());
        Vector2D spin = new Vector2D();
        for (int j = 0; j < 1000; j++) {
            pos.add(autoScheduler.getVelocity());
            spin.setX(autoScheduler.getSpin());
            System.out.println("(" + spin.getX() + "," + j +")");
        }
    }
}
