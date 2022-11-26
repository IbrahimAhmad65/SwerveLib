package SplineGenerator.Follower;

import main.Vector2D;

import java.util.Objects;

// Clas for storing waypoints
public class Waypoint implements Comparable<Waypoint> {
    private double t;
    private Runnable action;
    private double speed;
    private boolean hasRun;

    private boolean requiredPos = false;

    public Waypoint(double t, Runnable action, double speed) {
        this.t = t;
        this.action = action;
        this.speed = speed;
        hasRun = false;
    }

    public Waypoint(double t, Runnable action, double speed, boolean requiredPos) {
        this(t, action, speed);
        this.requiredPos = requiredPos;
    }

    public double getSpeed() {
        return speed;
    }

    public Runnable getAction() {
        return action;
    }

    public double getT() {
        return t;
    }

    public boolean isRequiredPos() {
        return requiredPos;
    }

    /**
     * Executes the waypoint if the t value is correct
     **/
    public void run(double t) {
        if (this.t > t && !hasRun) {
            action.run();
            hasRun = true;
        }
    }

    /***
     * Allows overriding of whether the waypoint has run or not
     * */
    public void setHasRun(boolean hasRun) {
        this.hasRun = hasRun;
    }

    /**
     * Returns whether the waypoint has run
     */
    public boolean hasRun() {
        return hasRun;
    }

    /***
     * Checks if two waypoints are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Waypoint waypoint = (Waypoint) o;
        return Double.compare(waypoint.t, t) == 0 && Double.compare(waypoint.speed, speed) == 0 && hasRun == waypoint.hasRun && requiredPos == waypoint.requiredPos && Objects.equals(action, waypoint.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(t, action, speed, hasRun, requiredPos);
    }

    /**
     * Compares two waypoints together
     */
    @Override
    public int compareTo(Waypoint o) {
        return (int) (t * 1000 - 1000 * ((Waypoint) o).getT());
    }
}
