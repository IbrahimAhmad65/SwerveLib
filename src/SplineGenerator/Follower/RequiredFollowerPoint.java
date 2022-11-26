package SplineGenerator.Follower;

public class RequiredFollowerPoint implements Comparable<RequiredFollowerPoint> {
    /**
     * The T value of this point
     **/
    private double t;
    /**
     * The angle of this point
     */
    private double angle;
    /**
     * Whether this point has been reached
     **/
    private boolean hasBeenReached;

    public RequiredFollowerPoint(double t, double angle) {
        this.t = t;
        this.angle = angle;
        this.hasBeenReached = false;
    }

    /**
     * Returns the t value of this required follower point
     **/
    public double getT() {
        return t;
    }

    /**
     * Gets the angle of this required follower point
     **/
    public double getAngle() {
        return angle;
    }

    /**
     * Returns whether this required follower point has been reached
     **/
    public boolean isHasBeenReached() {
        return hasBeenReached;
    }

    /**
     * Sets this required follower point to have been reached
     **/
    public void setHasBeenReached(boolean hasBeenReached) {
        this.hasBeenReached = hasBeenReached;
    }

    @Override
    public int compareTo(RequiredFollowerPoint o) {
        if (getT() == o.getT()) {
            return 0;
        }
        return getT() > o.getT() ? 1 : -1;
    }
}