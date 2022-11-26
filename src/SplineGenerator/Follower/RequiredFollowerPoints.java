package SplineGenerator.Follower;

import java.util.Arrays;

// Required Follower Point Code
public class RequiredFollowerPoints {

    private double outR;
    private double inR;

    private RequiredFollowerPoint[] requiredFollowerPoints;

    public RequiredFollowerPoints(double outR, double inR, RequiredFollowerPoint... followerPoints) {
        // The list of all the required points
        requiredFollowerPoints = followerPoints;
        // The radius to determine if the robot has reached the required point
        this.inR = inR;
        // The radius to determine if the robot must start driving towards the required point
        this.outR = outR;
    }

    /**
     * Determines whether the robot is currently in the radius of one or more follower points, If more than one follower point's radii
     * , the following algorithm will first go towards the point with the lowest t value.
     **/
    public boolean isInOuter(double t) {
        for (RequiredFollowerPoint r : requiredFollowerPoints) {
            if (Math.abs(t - r.getT()) < outR && !r.isHasBeenReached()) {
                return true;
            }
        }
        return false;
    }

    public boolean isInner(double t) {
        for (RequiredFollowerPoint r : requiredFollowerPoints) {
            if (Math.abs(t - r.getT()) < inR && !r.isHasBeenReached()) {
                r.setHasBeenReached(true);
                return true;
            }
        }
        return false;
    }

    public RequiredFollowerPoint[] getRequiredFollowerPoints() {
        return requiredFollowerPoints;
    }

    /**
     * Gets the angles of the required follower points ordered based off of increasing t
     * **/
    public double[] getAnglesOrdered() {
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

    /**
     * Gets the t values of the required follower points ordered based off of increasing t
     * **/
    public double[] getTOrdered() {
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

    /**
     * Finds the nearest t of a required follower point, given the current t
     * **/
    public double findNearestT(double t) {
        double min = 9999999;
        double out = -1;
        for (RequiredFollowerPoint r : requiredFollowerPoints) {
            if (Math.abs(t - r.getT()) < min && !r.isHasBeenReached()) {
                min = Math.abs(t - r.getT());
                out = r.getT();
            }
        }
        return out;
    }


}
