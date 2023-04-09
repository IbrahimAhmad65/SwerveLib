package SplineGenerator.Follower.sim;

import SplineGenerator.Follower.SingleAuto;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Util.DControlPoint;
import main.Vector2D;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SmartAutoGen {
    private static HashMap<String, Vector2D[]> posMap;
    private static HashMap<String, AutoGenWaypoint[]> waypointMapAboutZero;
    private static HashMap<String, Double> angleMap;
    private static double transitSpeed = 3;
    private static String initActions = "PIH";
    private static String fallBackAction = "PH";
    private static Polygon restrictedZones[];
    private static HashMap<Integer, Vector2D[]> transitIn;
    private static HashMap<Integer, Vector2D[]> transitOut;


    static {
        posMap = new HashMap<>();
        waypointMapAboutZero = new HashMap<>();
        angleMap = new HashMap<>();

        // Must be redone every new game
        posMap.put("s1Co", new Vector2D[]{new Vector2D(-6.31, .89), new Vector2D(-2, 0)});
        posMap.put("s1Cu", new Vector2D[]{new Vector2D(-6.31, .34), new Vector2D(-2, 0)});
        posMap.put("s2Co", new Vector2D[]{new Vector2D(-6.31, -.205), new Vector2D(-2, 0)});
        posMap.put("s3Co", new Vector2D[]{new Vector2D(-6.31, -.76), new Vector2D(-2, 0)});
        posMap.put("s2Cu", new Vector2D[]{new Vector2D(-6.31, -1.31), new Vector2D(-2, 0)});
        posMap.put("s4Co", new Vector2D[]{new Vector2D(-6.31, -1.856), new Vector2D(-2, 0)});
        posMap.put("s5Co", new Vector2D[]{new Vector2D(-6.31, -2.41), new Vector2D(-2, 0)});
        posMap.put("s3Cu", new Vector2D[]{new Vector2D(-6.31, -2.946), new Vector2D(-2, 0)});
        posMap.put("s6Co", new Vector2D[]{new Vector2D(-6.31, -3.52), new Vector2D(-2, 0)});
        angleMap.put("s1Co", 0.0);
        angleMap.put("s1Cu", 0.0);
        angleMap.put("s2Co", 0.0);
        angleMap.put("s3Co", 0.0);
        angleMap.put("s2Cu", 0.0);
        angleMap.put("s4Co", 0.0);
        angleMap.put("s5Co", 0.0);
        angleMap.put("s3Cu", 0.0);
        angleMap.put("s6Co", 0.0);


        posMap.put("gmp1", new Vector2D[]{new Vector2D(-1.199, .4946), new Vector2D(1, 0)});
        posMap.put("gmp2", new Vector2D[]{new Vector2D(-1.199, -.706), new Vector2D(1, 0)});
        posMap.put("gmp3", new Vector2D[]{new Vector2D(-1.199, -1.909), new Vector2D(1, 0)});
        posMap.put("gmp4", new Vector2D[]{new Vector2D(-1.199, -3.101), new Vector2D(1, 0)});
        angleMap.put("gmp1", 10.0);
        angleMap.put("gmp2", -3.0);
        angleMap.put("gmp3", 72.0);
        angleMap.put("gmp4", 1.0);


        posMap.put("stationIn", new Vector2D[]{new Vector2D(-5, -1.07), new Vector2D(1, 0)});
        posMap.put("stationOut", new Vector2D[]{new Vector2D(-4, -1.07), new Vector2D(-1, 0)});
        angleMap.put("stationIn", 0.0);
        angleMap.put("stationOut", 0.0);


        waypointMapAboutZero.put("PIH", new AutoGenWaypoint[]{new AutoGenWaypoint("placeConeHighInit", 0, transitSpeed * 1.5)});
        waypointMapAboutZero.put("cone", new AutoGenWaypoint[]{new AutoGenWaypoint("pickCone", -.8, transitSpeed), new AutoGenWaypoint("none", -.1, .25), new AutoGenWaypoint("none", .1, .25), new AutoGenWaypoint("unpick", .3, transitSpeed)});
        waypointMapAboutZero.put("cube", new AutoGenWaypoint[]{new AutoGenWaypoint("pickCubeA", -.7, transitSpeed), new AutoGenWaypoint("none", -.1, .9), new AutoGenWaypoint("none", .1, .9), new AutoGenWaypoint("unpick", .3, transitSpeed)});
        waypointMapAboutZero.put("PH", new AutoGenWaypoint[]{new AutoGenWaypoint("PlaceHighCont", -.65, transitSpeed), new AutoGenWaypoint("none", -.3, transitSpeed / 3.0), new AutoGenWaypoint("dropWithWait", 0, transitSpeed * .8 / 3.0)});
        waypointMapAboutZero.put("lock", new AutoGenWaypoint[]{new AutoGenWaypoint("lockSwerve", -.1, .6), new AutoGenWaypoint("none", -.5, .6)});

        Polygon stationTop = new Polygon(new Vector2D[]{new Vector2D(-5.3, 0), new Vector2D(-3.37, 0), new Vector2D(-3.37, -1.31), new Vector2D(-5.3, -1.31)});
        Polygon stationBot = new Polygon(new Vector2D[]{new Vector2D(-5.3, -2.54), new Vector2D(-3.37, -2.54), new Vector2D(-3.37, -1.31), new Vector2D(-5.3, -1.31)});

        restrictedZones = new Polygon[]{stationTop, stationBot};


        Vector2D topTransit = new Vector2D(-4.31, .444);
        Vector2D topVel = new Vector2D(-3, 0);

        Vector2D botTransit = new Vector2D(-4.31, -3.2);
        Vector2D botVel = new Vector2D(-3, 0);
        transitIn = new HashMap<>();
        transitIn.put(0, new Vector2D[]{topTransit, topVel});
        transitIn.put(1, new Vector2D[]{botTransit, botVel});
        transitOut = new HashMap<>();
        transitOut.put(0, new Vector2D[]{topTransit, topVel.clone().scaleX(-1)});
        transitOut.put(1, new Vector2D[]{botTransit, botVel.clone().scaleX(-1)});

    }

    public static void createAuto(String autoData) {
        String[] args = autoData.split(",");
        ArrayList<String> argsList = new ArrayList<>();
        for (String arg : args) {
            argsList.add(arg);
        }
        String name = argsList.remove(0);
        int numControlPoints = argsList.size();
        ArrayList<Vector2D[]> controlPoints = new ArrayList<>();
        ArrayList<AutoGenWaypoint> waypoints = new ArrayList<>();
        ArrayList<AutoGenAngle> angles = new ArrayList<>();

        boolean firstItrOfString = true;
        for (int i = 0; i < numControlPoints; i++) {
            String arg = argsList.remove(0);
            String[] argSplit = arg.split("_");
            String pos = argSplit[0];
            String action = argSplit[1];
            controlPoints.add(posMap.get(pos));
            angles.add(new AutoGenAngle(i, angleMap.get(pos)));
            if (firstItrOfString && action.equals(action)) {
                action = initActions;
                firstItrOfString = false;
            }
            for (AutoGenWaypoint waypoint : waypointMapAboutZero.get(action)) {
                waypoint.setDeltaT(waypoint.getDeltaT() + i);
                waypoints.add(waypoint);
            }


        }
        SingleAuto auto = createAutoLocal(name, controlPoints, waypoints, angles);

        for (int j = 0; j < 10; j++) {
            boolean violation = false;
            for (Polygon restrictedZone : restrictedZones) {
                double tFailure = -1;
                for (double i = 0; i < auto.getSpline().getNumPieces(); i += .01) {
                    if (restrictedZone.isWithin(auto.getSpline().get(i).toVector2D().addTheta(-Math.PI / 2))) {
                        violation = true;
                        tFailure = i;
                        System.out.println("Violation at " + i);
                        break;
                    }
                }
                if (violation) {
                    int trueIndex = (int) Math.floor(tFailure);
                    if (trueIndex < controlPoints.size() - 1) {
                        int transitFix = computeTransitFailureMode(controlPoints.get(trueIndex)[0]);
                        boolean transitIn = controlPoints.get(trueIndex)[0].clone().subtract(controlPoints.get(trueIndex+1)[0]).getX() < 0;
                        if(transitIn){
                            controlPoints.add(trueIndex+1,SmartAutoGen.transitIn.get(transitFix));
                        } else {
                            controlPoints.add(trueIndex+1,SmartAutoGen.transitOut.get(transitFix));
                        }
                        for (AutoGenWaypoint waypoint : waypoints) {
                            if(waypoint.getDeltaT() >= transitFix+1){
                                waypoint.setDeltaT(waypoint.getDeltaT()+1);
                            }
                        }
                        for (AutoGenAngle angle : angles) {
                            if(angle.getDeltaT() >= transitFix+1){
                                angle.setDeltaT(angle.getDeltaT()+1);
                            }
                        }
                        auto = createAutoLocal(name, controlPoints, waypoints, angles);
                        break;
                    }
                }
            }
        }

    }


    private static int computeTransitFailureMode(Vector2D pos) {
        for (int i = 0; i < restrictedZones.length; i++) {
            if (restrictedZones[i].isWithin(pos)) {
                return i;
            }
        }
        return -1;
    }


    private static SingleAuto createAutoLocal(String name, ArrayList<Vector2D[]> controlPoints, ArrayList<AutoGenWaypoint> waypoints, ArrayList<AutoGenAngle> angles) {
        // FIXME Ordering can be fixed by swapping to a JSONArray, kinda big change so dont do it now
        JSONObject json = new JSONObject();
        json.put("Name", name);
        json.put("thetaOffset", 90.0);
        json.put("Closed", false);


        JSONArray controlPointMap = new JSONArray();
        for (Vector2D[] controlPoint : controlPoints) {
            JSONObject controlPointData = new JSONObject();
            controlPointData.put("X", controlPoint[0].getX());
            controlPointData.put("Y", controlPoint[0].getY());
            controlPointData.put("Vx", controlPoint[1].getY());
            controlPointData.put("Vy", controlPoint[1].getY());
            controlPointData.put("order", transitIn.containsValue(controlPoint[0]) || transitOut.containsValue(controlPoint[0]) ? 5 : 1);
            controlPointMap.put(controlPointData);
        }


        JSONArray waypointMap = new JSONArray();
        for (AutoGenWaypoint waypoint : waypoints) {
            JSONObject waypointData = new JSONObject();
            waypointData.put("t", waypoint.getDeltaT());
            waypointData.put("Speed", waypoint.getSpeed());
            waypointData.put("Command", waypoint.getName());
            waypointMap.put(waypointData);
        }
        JSONArray angleMap = new JSONArray();
        for (AutoGenAngle angle : angles) {
            JSONObject angleData = new JSONObject();
            angleData.put("t", angle.getDeltaT());
            angleData.put("angle", angle.getAngle());
            angleMap.put(angleData);
        }


        json.put("ControlPoints", controlPointMap);
        json.put("WayPoints", waypointMap);
        json.put("RequiredPoints", angleMap);
        try {
            FileWriter writer = new FileWriter("/home/ibrahim/robotics/SwerveLib/src/SplineGenerator/Follower/sim/autogen.json");
            writer.write(json.toString());
            writer.flush();
//            json.write(new FileWriter("/home/ibrahim/autgen.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return JSONAutoParser.generateAutoFromJSON(json, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        SmartAutoGen.createAuto("NAME,s1Co_PH,gmp1_cube,s2Co_PH,gmp2_cone");
    }

}

class Polygon {
    Vector2D[] points;

    public Polygon(Vector2D[] points) {
        this.points = points;
    }

    public void setPoints(Vector2D[] points) {
        this.points = points;
    }

    public boolean isWithin(Vector2D vector2D) {
        int i, j;
        boolean c = false;
        for (i = 0, j = points.length - 1; i < points.length; j = i++) {
            if (((points[i].getY() > vector2D.getY()) != (points[j].getY() > vector2D.getY())) &&
                    (vector2D.getX() < (points[j].getX() - points[i].getX()) * (vector2D.getY() - points[i].getY()) / (points[j].getY() - points[i].getY()) + points[i].getX()))
                c = !c;
        }
        return c;
    }

    public Vector2D[] getPoints() {
        return points;
    }

    public Vector2D getCenter() {
        double x = 0;
        double y = 0;
        for (Vector2D point : points) {
            x += point.getX();
            y += point.getY();
        }
        return new Vector2D(x / points.length, y / points.length);
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "points=" + Arrays.toString(points) +
                '}';
    }
}