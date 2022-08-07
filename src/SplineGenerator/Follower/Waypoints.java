package SplineGenerator.Follower;

import BSpline.BSplineH;
import BSpline.SplinePoint;
import main.LinearlyInterpLUT;
import main.Vector2D;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Waypoints {
    ArrayList<Waypoint> waypoints;
    BSplineH bSplineH;
    double[] tValues;

    private LinearlyInterpLUT tLut;

    private double tR = 0;


    public Waypoints(Waypoint... waypoints) {
        Arrays.sort(waypoints);
        this.waypoints = new ArrayList<Waypoint>(List.of(waypoints));
        contruct();
    }

    public void contruct() {
        SplinePoint[] splinePoints = new SplinePoint[waypoints.size()];
        tValues = new double[waypoints.size()];
        double[] tArr = new double[waypoints.size()];
        for (int i = 0; i < waypoints.size(); i++) {
            splinePoints[i] = new SplinePoint(new Vector2D(waypoints.get(i).getT(), waypoints.get(i).getSpeed()), new Vector2D(1, 0));
            tValues[i] = waypoints.get(i).getT();
            tArr[i] = i;

        }
        tLut = new LinearlyInterpLUT(tValues, tArr);
//        for (int i = 0; i < tValues.length; i++) {
//            System.out.print(tValues[i] + " ");
//        }
//        System.out.println();
//        for (int i = 0; i < tArr.length; i++) {
//            System.out.print(tArr[i] + " ");
//        }
        bSplineH = new BSplineH(.01, .01, splinePoints);
    }

    public ArrayList<Waypoint> getHasRun() {
        ArrayList<Waypoint> out = new ArrayList<Waypoint>();
        for (Object i : waypoints.toArray()) {
            if (((Waypoint) i).hasRun()) {
                out.add((Waypoint) i);
            }
        }
        return out;
    }

    public ArrayList<Waypoint> getHasNotRun() {
        ArrayList<Waypoint> out = new ArrayList<Waypoint>();
        for (Object i : waypoints.toArray()) {
            if (!((Waypoint) i).hasRun()) {
                out.add((Waypoint) i);
            }
        }
        return out;
    }

    public void addWaypoint(Waypoint waypoint) {
        waypoints.add(waypoint);
        contruct();
    }

    public Waypoints getRequiredPosWaypoints(){
        ArrayList<Waypoint> w = new ArrayList<Waypoint>();
        for (int i = 0; i < waypoints.size(); i++) {
            if(waypoints.get(i).isRequiredPos()){
                w.add(waypoints.get(i));
            }
        }
        Waypoint[] waypointsO = new Waypoint[w.size()];
        w.add(waypoints.get(0));
        w.toArray(waypointsO);
        return new Waypoints(waypointsO);
    }

    public double getSpeed(double t) {
//        System.out.println(t);

//        System.out.println("tLut: " + tLut.get(t));
//        System.out.println(t);
        return bSplineH.evaluatePos(tLut.get(t)).getY();
    }

    public void removeWaypoint(Waypoint w){
        for (int i = 0; i < waypoints.size(); i++) {
            if(w.equals(waypoints.get(i))){
                waypoints.remove(i);
            }
        }
    }

    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }
}
