package SplineGenerator.Follower;

import main.Vector2D;

//Interface for spline followers to implement
public interface Follower {

    //Gets the velocity the robot should follow given the current position
    Vector2D get(Vector2D pos);

    // Finds the nearest point on the spline to the provided position
    public Vector2D findPosOnSpline(Vector2D pos);

    // Finds the nearest t value on the spline to the provided position
    public double findTOnSpline(Vector2D pos);

    //Returns the Waypoints object associated with this spline
    public Waypoints getWaypoints();

    public default RequiredFollowerPoints getRequiredPoints(){
        return null;
    }

    // Returns the intended angle of the robot provided the given position, should be provided with the same position as get(Vector2D pos)
    public default double getSpin(Vector2D pos) {
        return 0;
    }

    public boolean finished();

    public default void reset(Vector2D pos) {

    }

    public default double getT(){
        return 0;
    }
}
