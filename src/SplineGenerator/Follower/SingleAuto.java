package SplineGenerator.Follower;

/**
 * A class that contains an auto, it's name, it's follower,
 * **/
public class SingleAuto {
    // Follower for the auto
    private Follower follower;
    // Spline for the Auto
    private Followable spline;
    // Name of Auto
    private String autoName;

    public SingleAuto(String autoName,Followable spline, Follower follower){
        this.follower = follower;
        this.spline = spline;
        this.autoName = autoName;
    }
    public SingleAuto(String autoName,Followable spline, Follower follower, double autoStartingOffset){
        this.follower = follower;
        this.spline = spline;
        this.autoName = autoName;
    }

    public Followable getSpline() {
        return spline;
    }

    public Follower getFollower() {
        return follower;
    }

    public String getAutoName() {
        return autoName;
    }
}
