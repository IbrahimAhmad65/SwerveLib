package SplineGenerator.Follower;

import BSpline.BSplineH;
import BSpline.SplinePoint;
import main.LinearlyInterpLUT;
import main.Vector2D;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Class for holding waypoints
public class Waypoints {
    // Arraylist that stores all the waypoints
    ArrayList<Waypoint> waypoints;
    // Spline used for the speed mapping
    BSplineH bSplineH;
    // Array of all the t values that have waypoints
    double[] tValues;

    // LUT for all the t values of waypoints
    private LinearlyInterpLUT tLut;


    public Waypoints(Waypoint... waypoints) {
        // Sorts the waypoints passed in
        Arrays.sort(waypoints);
        // Converts waypoints to local array
        this.waypoints = new ArrayList<Waypoint>(List.of(waypoints));
        contruct();
    }

    /**
     * When ever array or arraylist changes are made in this class,
     * this should be called to properly reconstruct the necessary objects
     * */
    public void contruct() {
        // Making an array of spline points for use in bSplineH
        SplinePoint[] splinePoints = new SplinePoint[waypoints.size()];
        // Creating the T values for the bSplineH
        tValues = new double[waypoints.size()];
        double[] tArr = new double[waypoints.size()];
        // Filling objects to be passed into the LUT and the Spline
        for (int i = 0; i < waypoints.size(); i++) {
            splinePoints[i] = new SplinePoint(new Vector2D(waypoints.get(i).getT(), waypoints.get(i).getSpeed()), new Vector2D(1, 0));
            tValues[i] = waypoints.get(i).getT();
            tArr[i] = i;
        }
        // Instantiating the LUT and bSplineH
        tLut = new LinearlyInterpLUT(tValues, tArr);
        bSplineH = new BSplineH(.01, .01, splinePoints);
    }

    // Returns the waypoints that have run yet
    public ArrayList<Waypoint> getHasRun() {
        ArrayList<Waypoint> out = new ArrayList<Waypoint>();
        for (Object i : waypoints.toArray()) {
            if (((Waypoint) i).hasRun()) {
                out.add((Waypoint) i);
            }
        }
        return out;
    }

    // Returns the waypoints that have already run
    public ArrayList<Waypoint> getHasNotRun() {
        ArrayList<Waypoint> out = new ArrayList<Waypoint>();
        for (Object i : waypoints.toArray()) {
            if (!((Waypoint) i).hasRun()) {
                out.add((Waypoint) i);
            }
        }
        return out;
    }

    /**
     * Adds another waypoint, points must be added in terms of increasing t
     * */
    public void addWaypoint(Waypoint waypoint) {
        waypoints.add(waypoint);
        contruct();
    }

    /**
     * Returns the speed at a given t
     * */
    public double getSpeed(double t) {
        return bSplineH.evaluatePos(tLut.get(t)).getY();
    }

    /**
     * removes a waypoint
     * */
    public void removeWaypoint(Waypoint w){
        for (int i = 0; i < waypoints.size(); i++) {
            if(w.equals(waypoints.get(i))){
                waypoints.remove(i);
            }
        }
        contruct();
    }

    /**
     * Returns the arraylist of waypoints
     * */
    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }
}
