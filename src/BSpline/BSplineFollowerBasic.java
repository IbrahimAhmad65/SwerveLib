package BSpline;

import BSpline.Graphics.SplineGrapher;
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
        t = 0;
    }

    public Vector2D get(Vector2D pos) {
        Vector2D min = new Vector2D(9999999, 9999999);
        double newT = -1;
        for (double i = -tR + t; i < t + tR; i += tRes) {
            if (i > 0 && i < spline.getEquationNumber()) {
                if (spline.evaluatePos(i).clone().subtract(pos.clone()).getMagnitude() < min.clone().subtract(pos).getMagnitude()) {
                    min = spline.evaluatePos(i).clone();
                    newT = i;
                }
            }
        }
        t = newT;
//        System.out.println(t);
//        if (spline.evaluatePos(t).clone().subtract(pos.clone()).scale(backVel).getMagnitude() < .1) {
//            System.out.println("forward");
            return spline.evaluateVelocity(t).clone();
//        }
//        System.out.println("");
//        return spline.evaluatePos(t).clone().subtract(pos.clone()).scale(backVel);
    }

    public Vector2D get(Vector2D pos, double useless){
//        t = spline.findNearestPointOnInterval(pos,t,t+tR);
        t +=tR;
//        System.out.println(new Vector2D(t+3,useless));
        System.out.println(t + ","  + (t + tR));
        return spline.evaluatePos(t).clone().subtract(pos);
    }


    public static void main(String[] args) {
        SplinePoint[] splinePoints1 = {
                new SplinePoint(new Vector2D(.5, 1), new Vector2D(1, 3)),
                new SplinePoint(new Vector2D(.6, 1), new Vector2D(1, 3)),
                new SplinePoint(new Vector2D(.1, .1), new Vector2D(1, 1))
        };
        BSplineH b = new BSplineH( .01, .1, splinePoints1);
        BSplineFollowerBasic followerBasic = new BSplineFollowerBasic(b, new Waypoint[]{}, .01, .01, 1, .1);
        System.out.println(b.findNearestPoint(new Vector2D(.5,1)));
        Vector2D pos = new Vector2D(.5, 1);
        if (true) {
            b.printSplinePos();
        } else {
            for (int i = 0; i < 1500; i++) {
                System.out.println(pos);
                pos.add(followerBasic.get(pos,i/500.0).clone().scale(.1));
            }
        }
    }
}
