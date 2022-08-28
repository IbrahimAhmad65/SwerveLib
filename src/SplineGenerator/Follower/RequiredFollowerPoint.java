package SplineGenerator.Follower;

public class RequiredFollowerPoint implements Comparable<RequiredFollowerPoint>{
    private double t;
    private double angle;
    private boolean hasBeenReached;
    public RequiredFollowerPoint(double t, double angle){
        this.t = t;
        this.angle = angle;
        this.hasBeenReached = false;
    }

    public double getT() {
        return t;
    }

    public double getAngle() { return angle;}

    public boolean isHasBeenReached() {
        return hasBeenReached;
    }

    public void setHasBeenReached(boolean hasBeenReached) {
        this.hasBeenReached = hasBeenReached;
    }

    @Override
    public int compareTo(RequiredFollowerPoint o) {
        if(getT() == o.getT()){
            return 0;
        }
        return getT() > o.getT() ? 1 : -1;
    }
}