package SplineGenerator.Follower;

public class SingleAuto {

    private Follower follower;
    private Followable spline;
    private String autoName;
    public SingleAuto(String autoName,Followable spline, Follower follower){
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
