package SplineGenerator.Follower;

import BSpline.BSplineH;
import BSpline.SplinePoint;
import main.Vector2D;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Waypoints {
    ArrayList<Waypoint> waypoints;
    BSplineH bSplineH;
    public Waypoints(Waypoint... waypoints){
        Arrays.sort(waypoints);
        this.waypoints = new ArrayList<Waypoint>(List.of(waypoints));
        SplinePoint[] splinePoints = new SplinePoint[waypoints.length];
        for(int i = 0 ;i < waypoints.length; i++){
            splinePoints[i] = new SplinePoint(new Vector2D(waypoints[i].getT(),waypoints[i].getSpeed()),new Vector2D(1,0));
        }
        bSplineH = new BSplineH(.01,.01,splinePoints);
    }

    public ArrayList<Waypoint> getHasRun(){
        ArrayList<Waypoint> out = new ArrayList<Waypoint>();
        for(Object i : waypoints.toArray()){
            if(((Waypoint) i).hasRun()){
                out.add((Waypoint) i);
            }
        }
        return out;
    }

    public ArrayList<Waypoint> getHasNotRun(){
        ArrayList<Waypoint> out = new ArrayList<Waypoint>();
        for(Object i : waypoints.toArray()){
            if(!((Waypoint) i).hasRun()){
                out.add((Waypoint) i);
            }
        }
        return out;
    }

    public double getSpeed(double t){
//        if(waypoints.size() > 1)
//        return bSplineH.evaluatePos(t).getY();
        return 1;
    }
}
