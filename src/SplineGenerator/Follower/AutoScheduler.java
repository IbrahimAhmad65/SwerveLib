package SplineGenerator.Follower;

import SplineGenerator.Util.DVector;
import main.Vector2D;

import java.lang.reflect.Array;
import java.util.function.Supplier;

public class AutoScheduler {
    private AutoRegistry registry;
    private String currentAuto = "";
    private Supplier<Vector2D> posSupplier;
    public AutoScheduler(AutoRegistry registry, Supplier<Vector2D> posSupplier){
        this.registry = registry;
        this.posSupplier = posSupplier;
    }

    public void setRegistry(AutoRegistry registry){
        this.registry = registry;
    }

    public void setCurrentAuto(String name){
        currentAuto = name;
    }

    public Vector2D getVelocity(){
        return registry.get(currentAuto).getFollower().get(posSupplier.get());
    }

    public void runWaypoints(){
        Follower follower = registry.get(currentAuto).getFollower();
        double t = follower.findTOnSpline(posSupplier.get());
        Waypoint[] waypoints = new Waypoint[follower.getWaypoints().getWaypoints().size()];
        follower.getWaypoints().getWaypoints().toArray(waypoints);
        for (Waypoint i : waypoints) {
            i.run(t);
        }
    }
}
