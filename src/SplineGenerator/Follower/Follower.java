package SplineGenerator.Follower;

import main.Vector2D;

public interface Follower {

    Vector2D get(Vector2D pos);
    public Vector2D findPosOnSpline(Vector2D pos);
    public double findTOnSpline(Vector2D pos);

    public Waypoints getWaypoints();
}
