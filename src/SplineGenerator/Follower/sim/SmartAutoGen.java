package SplineGenerator.Follower.sim;

import SplineGenerator.Follower.SingleAuto;
import main.Vector2D;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SmartAutoGen {
    private static HashMap<String, Vector2D[]> listOfPositions;
    private static HashMap<String, AutoGenWaypoint[]> commandList;
    private static HashMap<String, Double> listOfAngles;
    private static double referenceSpeed = 3;
    private static double rotationValue = -Math.PI / 2;
    // need to shift to string arrs
    private static String immuneToRestrictedZone = "stationOut";
    private static int immuneToRestrictionOrder = 5;
    private static String initializationAction = "PIH";
    private static String baseAction = "PH";
    private static Polygon restrictedZones[];
    private static HashMap<Integer, Vector2D[]> restrictedZonesRecoveryIn;
    private static HashMap<Integer, Vector2D[]> restrictedZonesRecoveryOut;
    private static int restrictedZonesRecoveryTotalTimes = 4;

    static {
        listOfPositions = new HashMap<>();
        commandList = new HashMap<>();
        listOfAngles = new HashMap<>();
        restrictedZonesRecoveryIn = new HashMap<>();
        restrictedZonesRecoveryOut = new HashMap<>();

        configureConstants();
    }

    private static void configureConstants() {
        listOfPositions.put("s1Co", new Vector2D[]{new Vector2D(-6.31, .89), new Vector2D(-2, 0)});
        listOfPositions.put("s1Cu", new Vector2D[]{new Vector2D(-6.31, .34), new Vector2D(-2, 0)});
        listOfPositions.put("s2Co", new Vector2D[]{new Vector2D(-6.31, -.205), new Vector2D(-2, 0)});
        listOfPositions.put("s3Co", new Vector2D[]{new Vector2D(-6.31, -.76), new Vector2D(-2, 0)});
        listOfPositions.put("s2Cu", new Vector2D[]{new Vector2D(-6.31, -1.31), new Vector2D(-2, 0)});
        listOfPositions.put("s4Co", new Vector2D[]{new Vector2D(-6.31, -1.856), new Vector2D(-2, 0)});
        listOfPositions.put("s5Co", new Vector2D[]{new Vector2D(-6.31, -2.41), new Vector2D(-2, 0)});
        listOfPositions.put("s3Cu", new Vector2D[]{new Vector2D(-6.31, -2.946), new Vector2D(-2, 0)});
        listOfPositions.put("s6Co", new Vector2D[]{new Vector2D(-6.31, -3.52), new Vector2D(-2, 0)});
        listOfAngles.put("s1Co", 0.0);
        listOfAngles.put("s1Cu", 0.0);
        listOfAngles.put("s2Co", 0.0);
        listOfAngles.put("s3Co", 0.0);
        listOfAngles.put("s2Cu", 0.0);
        listOfAngles.put("s4Co", 0.0);
        listOfAngles.put("s5Co", 0.0);
        listOfAngles.put("s3Cu", 0.0);
        listOfAngles.put("s6Co", 0.0);


        listOfPositions.put("gmp1", new Vector2D[]{new Vector2D(-1.199, .4946), new Vector2D(1, 0)});
        listOfPositions.put("gmp2", new Vector2D[]{new Vector2D(-1.199, -.706), new Vector2D(1, 0)});
        listOfPositions.put("gmp3", new Vector2D[]{new Vector2D(-1.199, -1.909), new Vector2D(1, 0)});
        listOfPositions.put("gmp4", new Vector2D[]{new Vector2D(-1.199, -3.101), new Vector2D(1, 0)});
        listOfAngles.put("gmp1", 10.0);
        listOfAngles.put("gmp2", -3.0);
        listOfAngles.put("gmp3", 72.0);
        listOfAngles.put("gmp4", 1.0);


        listOfPositions.put("stationIn", new Vector2D[]{new Vector2D(-5, -1.07), new Vector2D(4, 0)});
        listOfPositions.put("stationOut", new Vector2D[]{new Vector2D(-4, -1.07), new Vector2D(-4, 0)});
        listOfPositions.put("stationOutTransit", new Vector2D[]{new Vector2D(-6, -1.07), new Vector2D(4, 0)});
        listOfAngles.put("stationIn", 0.0);
        listOfAngles.put("stationOut", 0.0);
        listOfAngles.put("stationOutTransit", 0.0);


        commandList.put("none", new AutoGenWaypoint[]{new AutoGenWaypoint("none", 0, -1)});
        commandList.put("PIH", new AutoGenWaypoint[]{new AutoGenWaypoint("placeConeHighInit", 0, referenceSpeed * 1.5)});
        commandList.put("cone", new AutoGenWaypoint[]{new AutoGenWaypoint("pickCone", -.8, referenceSpeed), new AutoGenWaypoint("none", -.1, .25), new AutoGenWaypoint("none", .1, .25), new AutoGenWaypoint("unpick", .3, referenceSpeed)});
        commandList.put("cube", new AutoGenWaypoint[]{new AutoGenWaypoint("pickCubeA", -.7, referenceSpeed), new AutoGenWaypoint("none", -.1, .9), new AutoGenWaypoint("none", .1, .9), new AutoGenWaypoint("unpick", .3, referenceSpeed)});
        commandList.put("PH", new AutoGenWaypoint[]{new AutoGenWaypoint("PlaceHighCont", -.65, referenceSpeed), new AutoGenWaypoint("none", -.3, referenceSpeed / 3.0), new AutoGenWaypoint("dropWithWait", 0, referenceSpeed * .8 / 3.0)});
        commandList.put("lock", new AutoGenWaypoint[]{new AutoGenWaypoint("lockSwerve", -.1, .6), new AutoGenWaypoint("none", -.5, .6)});

        Polygon stationTopHalf = new Polygon(new Vector2D[]{new Vector2D(-5.3, 0), new Vector2D(-3.37, 0), new Vector2D(-3.37, -1.31), new Vector2D(-5.3, -1.31)});
        Polygon stationBottomHalf = new Polygon(new Vector2D[]{new Vector2D(-5.3, -2.54), new Vector2D(-3.37, -2.54), new Vector2D(-3.37, -1.31), new Vector2D(-5.3, -1.31)});

        restrictedZones = new Polygon[]{stationTopHalf, stationBottomHalf};


        Vector2D topOfStationRecoveryPosition = new Vector2D(-4.31, .444);
        Vector2D topOfStationVel = new Vector2D(3, 0);

        Vector2D bottomOfStationRecoveryPosition = new Vector2D(-4.31, -3.2);
        Vector2D bottomOfStationRecoveryVelocity = new Vector2D(3, 0);
        restrictedZonesRecoveryIn.put(0, new Vector2D[]{topOfStationRecoveryPosition, topOfStationVel});
        restrictedZonesRecoveryIn.put(1, new Vector2D[]{bottomOfStationRecoveryPosition, bottomOfStationRecoveryVelocity});
        restrictedZonesRecoveryOut.put(0, new Vector2D[]{topOfStationRecoveryPosition.clone(), topOfStationVel.clone().scaleX(-1)});
        restrictedZonesRecoveryOut.put(1, new Vector2D[]{bottomOfStationRecoveryPosition.clone(), bottomOfStationRecoveryVelocity.clone().scaleX(-1)});
    }

    public static void createAuto(String autoData) {
        String[] segments = autoData.split(",");
        ArrayList<String> segmentList = new ArrayList<>();
        for (String segment : segments) {
            segmentList.add(segment);
        }
        String name = segmentList.remove(0);
        int numControlPoints = segmentList.size();
        ArrayList<Vector2D[]> controlPoints = new ArrayList<>();
        ArrayList<AutoGenWaypoint> waypoints = new ArrayList<>();
        ArrayList<AutoGenAngle> angles = new ArrayList<>();

        boolean hasInitializeStringBeenAdded = false;
        for (int i = 0; i < numControlPoints; i++) {
            String currentPoint = segmentList.remove(0);
            String[] currentPointSplit = currentPoint.split("_");
            String position = currentPointSplit[0];
            String action = currentPointSplit[1];
            controlPoints.add(listOfPositions.get(position));
            angles.add(new AutoGenAngle(i, listOfAngles.get(position)));
            if (!hasInitializeStringBeenAdded && action.equals(action)) {
                action = initializationAction;
                hasInitializeStringBeenAdded = true;
            }
            for (AutoGenWaypoint waypoint : commandList.get(action)) {
                waypoint.setDeltaT(waypoint.getDeltaT() + i);
                waypoints.add(waypoint);
            }
        }
        SingleAuto auto = createAutoBase(name, controlPoints, waypoints, angles);
        for (int i = 0; i < restrictedZonesRecoveryTotalTimes && hasRestrictedZoneFailure(auto); i++) {
            auto = applyRestrictedZoneRecovery(auto, controlPoints, waypoints, angles);
        }
        for (double i = 0; i < auto.getSpline().getNumPieces(); i += .01) {
            System.out.println(auto.getSpline().get(i).toVector2D().addTheta(rotationValue));
        }
    }


    private static SingleAuto createAutoBase(String name, ArrayList<Vector2D[]> controlPoints, ArrayList<AutoGenWaypoint> waypoints, ArrayList<AutoGenAngle> angles) {
        // FIXME Ordering can be fixed by swapping to a JSONArray, kinda big change so dont do it now
        JSONObject mainJSONContainer = new JSONObject();
        mainJSONContainer.put("Name", name);
        mainJSONContainer.put("thetaOffset", 90.0);
        mainJSONContainer.put("Closed", false);


        JSONArray controlPointsForContainer = new JSONArray();
        for (Vector2D[] controlPoint : controlPoints) {
            JSONObject controlPointSingleIndex = new JSONObject();
            controlPointSingleIndex.put("X", controlPoint[0].getX());
            controlPointSingleIndex.put("Y", controlPoint[0].getY());
            controlPointSingleIndex.put("Vx", controlPoint[1].getX());
            controlPointSingleIndex.put("Vy", controlPoint[1].getY());
//            if (controlPoint[0].equals(listOfPositions.get(immuneToRestrictedZone)[0])) {
//                System.out.println("in immunity restricted zone order bypass");
//                controlPointSingleIndex.put("order", immuneToRestrictionOrder);
//            } else
            {
                for (int i = 0; i < restrictedZonesRecoveryIn.size(); i++) {
                    if (restrictedZonesRecoveryIn.get(i)[0].equals(controlPoint[0])) {
                        controlPointSingleIndex.put("order", 5);
                        break;
                    }
                }
            }
            if (!controlPointSingleIndex.has("order")) {
                controlPointSingleIndex.put("order", 1);
            }

            controlPointsForContainer.put(controlPointSingleIndex);
        }


        JSONArray waypointsForContainer = new JSONArray();
        for (AutoGenWaypoint waypoint : waypoints) {
            JSONObject waypointIndex = new JSONObject();
            waypointIndex.put("t", waypoint.getDeltaT());
            waypointIndex.put("Speed", waypoint.getSpeed());
            waypointIndex.put("Command", waypoint.getName());
            waypointsForContainer.put(waypointIndex);
        }
        JSONArray anglesForContainer = new JSONArray();
        for (AutoGenAngle angle : angles) {
            JSONObject angleIndex = new JSONObject();
            angleIndex.put("t", angle.getDeltaT());
            angleIndex.put("angle", angle.getAngle());
            anglesForContainer.put(angleIndex);
        }


        mainJSONContainer.put("ControlPoints", controlPointsForContainer);
        mainJSONContainer.put("WayPoints", waypointsForContainer);
        mainJSONContainer.put("RequiredPoints", anglesForContainer);
        try {
            FileWriter writer = new FileWriter("/home/ibrahim/robotics/SwerveLib/src/SplineGenerator/Follower/sim/autogen.json");
            writer.write(mainJSONContainer.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return JSONAutoParser.generateAutoFromJSON(mainJSONContainer, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean hasRestrictedZoneFailure(SingleAuto autIn) {
        for (int j = 0; j < restrictedZones.length; j++) {
            for (double i = 0; i < autIn.getSpline().getNumPieces() - .01; i += .01) {
                if (restrictedZones[j].isWithin(autIn.getSpline().get(i).toVector2D().addTheta(rotationValue))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static SingleAuto applyRestrictedZoneRecovery(SingleAuto basePath, ArrayList<Vector2D[]> controlPoints, ArrayList<AutoGenWaypoint> waypoints, ArrayList<AutoGenAngle> angles) {
        for (int j = 0; j < restrictedZones.length; j++) {
            for (double i = 0; i < basePath.getSpline().getNumPieces() - .01; i += .01) {
                if (restrictedZones[j].isWithin(basePath.getSpline().get(i).toVector2D().addTheta(rotationValue))) {
                    if (basePath.getSpline().get(Math.ceil(i)).toVector2D().equals(listOfPositions.get(immuneToRestrictedZone)[0].clone().addTheta(-rotationValue))) {
                        continue;
                    }

                    int restrictedZoneViolated = j;
                    boolean pathMovingTowardsIn = controlPoints.get((int) Math.floor(i))[0].clone().getX() < controlPoints.get((int) Math.floor(i + 1))[0].clone().getX();
                    if (pathMovingTowardsIn) {
                        controlPoints.add((int) Math.floor(i) + 1, restrictedZonesRecoveryIn.get(restrictedZoneViolated));
                    } else {
                        controlPoints.add((int) Math.floor(i) + 1, restrictedZonesRecoveryOut.get(restrictedZoneViolated));
                    }
                    for (int k = 0; k < waypoints.size(); k++) {
                        if (waypoints.get(k).getDeltaT() > i) {
                            waypoints.get(k).setDeltaT(waypoints.get(k).getDeltaT() + 1);
                        }
                    }
                    for (int k = 0; k < angles.size(); k++) {
                        if (angles.get(k).getDeltaT() > i) {
                            angles.get(k).setDeltaT(angles.get(k).getDeltaT() + 1);
                        }
                    }
                    break;
                }
            }
        }
        return createAutoBase(basePath.getAutoName(), controlPoints, waypoints, angles);
    }

    public static void main(String[] args) {
        SmartAutoGen.createAuto("coneCubeCone3Station,s1Co_PH,gmp1_cube,s1Cu_PH,gmp2_cone,s2Co_PH,stationOutTransit_none,stationOut_lock");
        try {
            FullJSONSimulator fullJSONSimulator = new FullJSONSimulator("/home/ibrahim/robotics/SwerveLib/src/SplineGenerator/Follower/sim/autogen.json");
            fullJSONSimulator.simAll(.01, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            if (((points[i].getY() > vector2D.getY()) != (points[j].getY() > vector2D.getY())) && (vector2D.getX() < (points[j].getX() - points[i].getX()) * (vector2D.getY() - points[i].getY()) / (points[j].getY() - points[i].getY()) + points[i].getX()))
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
        return "Polygon{" + "points=" + Arrays.toString(points) + '}';
    }
}