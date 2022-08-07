package SplineGenerator.Follower;

public class RequiredFollowerPoint{
    private double t;
    private boolean hasBeenReached;
    public RequiredFollowerPoint(double t){
        this.t = t;
        this.hasBeenReached = false;
    }

    public double getT() {
        return t;
    }

    public boolean isHasBeenReached() {
        return hasBeenReached;
    }

    public void setHasBeenReached(boolean hasBeenReached) {
        this.hasBeenReached = hasBeenReached;
    }
}