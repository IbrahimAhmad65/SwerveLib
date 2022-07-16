package BSpline;

import main.Vector2D;

import java.util.ArrayList;
import java.util.function.Supplier;

public class BSplineFollowerBasic {

    private double t = 0;
    private BSplineH spline;
    private ArrayList<Waypoint> waypoints;
    private double tRes;
    private double tR;
    private double backVel;
    private double forVel;

    public BSplineFollowerBasic(BSplineH spline, Waypoint[] waypoints, double tR, double tRes, double backVel, double forVel) {
        for (Waypoint w : waypoints) {
            this.waypoints.add(w);
        }
        this.spline = spline;
        this.tR = tR;
        this.tRes = tRes;
        this.backVel = backVel;
        this.forVel = forVel;
    }

    public Vector2D get(Vector2D posNew) {
        Vector2D pos = posNew.clone();
        Vector2D min = new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        double newT = -1;
        for (double i = -tR + t; i < tR + t; i += tRes) {
//            i = i+tR;
//            System.out.println("i:" + i + "t: " + t + "tR: " + (i+tR));
            if (i > 0 && i < spline.getEquationNumber()) {
                if (spline.evaluatePos(i).clone().subtract(pos).getMagnitude() < min.getMagnitude()) {
                    min = spline.evaluatePos(i).clone();
                    newT = i;
                }
            }
        }
        t = newT;
        System.out.println(min.subtract(pos).scale(backVel).add(spline.evaluateVelocity(t).clone().scale(forVel)));
        return min.subtract(pos).scale(backVel).add(spline.evaluateVelocity(t).clone().scale(forVel));
    }


}
