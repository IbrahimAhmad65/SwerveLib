package SplineGenerator.Follower;

public class SingleAuto {

    private Follower follower;
    private Followable spline;
    private String autoName;
    private double autoStartingOffset = 0;
    public SingleAuto(String autoName,Followable spline, Follower follower){
        this.follower = follower;
        this.spline = spline;
        this.autoName = autoName;
    }
    public SingleAuto(String autoName,Followable spline, Follower follower, double autoStartingOffset){
        this.follower = follower;
        this.spline = spline;
        this.autoName = autoName;
        this.autoStartingOffset = autoStartingOffset;
    }

    public Followable getSpline() {
        return spline;
    }

    public double getAutoStartingOffset() {
        return autoStartingOffset;
    }

    public Follower getFollower() {
        return follower;
    }

    public String getAutoName() {
        return autoName;
    }
}
