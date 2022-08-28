package SplineGenerator.Follower;

import java.util.ArrayList;
import java.util.Arrays;

public class RequiredFollowerPoints {

//    private ArrayList<RequiredFollowerPoint> requiredFollowerPoints;
    private double outR;
    private double inR;

    private RequiredFollowerPoint[] requiredFollowerPoints;
    public RequiredFollowerPoints(double outR, double inR, RequiredFollowerPoint... followerPoints){
        requiredFollowerPoints = followerPoints;
//        requiredFollowerPoints = new ArrayList<RequiredFollowerPoint>();
//        for (RequiredFollowerPoint r: followerPoints) {
//            requiredFollowerPoints.add(r);
//        }
        this.inR = inR;
        this.outR = outR;
    }

    public boolean isInOuter(double t){
        for (RequiredFollowerPoint r:requiredFollowerPoints) {
            if(Math.abs(t - r.getT()) < outR && !r.isHasBeenReached()){
                return true;
            }
        }
        return false;
    }

    public boolean isInner(double t){
        for (RequiredFollowerPoint r:requiredFollowerPoints) {
            if(Math.abs(t - r.getT()) < inR && !r.isHasBeenReached()){
                r.setHasBeenReached(true);
                return true;
            }
        }
        return false;
    }

    public RequiredFollowerPoint[] getRequiredFollowerPoints() {
        return requiredFollowerPoints;
    }

    public double[] getAnglesOrdered(){
        double[] angles = new double[requiredFollowerPoints.length];
        RequiredFollowerPoint[] temp = new RequiredFollowerPoint[requiredFollowerPoints.length];
        for (int i = 0; i < requiredFollowerPoints.length; i++) {
            temp[i] = requiredFollowerPoints[i];
        }
        Arrays.sort(temp);
        for (int i = 0; i < angles.length; i++) {
            angles[i] = temp[i].getAngle();
        }
        return angles;
    }

    public double[] getTOrdered(){
        double[] angles = new double[requiredFollowerPoints.length];
        RequiredFollowerPoint[] temp = new RequiredFollowerPoint[requiredFollowerPoints.length];
        for (int i = 0; i < requiredFollowerPoints.length; i++) {
            temp[i] = requiredFollowerPoints[i];
        }
        Arrays.sort(temp);
        for (int i = 0; i < angles.length; i++) {
            angles[i] = temp[i].getT();
        }
        return angles;

    }

    public double findNearestT(double t){
        double min = 9999999;
        double out = -1;
        for (RequiredFollowerPoint r:requiredFollowerPoints) {
            if(Math.abs(t - r.getT()) < min && !r.isHasBeenReached()){
                min = Math.abs(t - r.getT());
                out = r.getT();
            }
        }
        return out;
    }


}
