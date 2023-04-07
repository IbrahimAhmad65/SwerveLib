package SplineGenerator.Follower.sim;

import SplineGenerator.Follower.SingleAuto;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Util.DControlPoint;
import main.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;

public class AutoGen {
    private static HashMap<String, Vector2D> posMap;
    private static HashMap<String, AutoGenWaypoint[]> waypointMapAboutZero;
    private static HashMap<String, Double> angleMap;
    private static double transitSpeed = 3;
    private static String initActions = "PIH";
    private static String fallBackAction = "PH";


    static {
        posMap = new HashMap<>();
        waypointMapAboutZero = new HashMap<>();
        angleMap = new HashMap<>();

        // Must be redone every new game
        posMap.put("s1Co", new Vector2D(-6.31, .89));
        posMap.put("s1Cu", new Vector2D(-6.31, .34));
        posMap.put("s2Co", new Vector2D(-6.31, -.205));
        posMap.put("s3Co", new Vector2D(-6.31, -.76));
        posMap.put("s2Cu", new Vector2D(-6.31, -1.31));
        posMap.put("s4Co", new Vector2D(-6.31, -1.856));
        posMap.put("s5Co", new Vector2D(-6.31, -2.41));
        posMap.put("s3Cu", new Vector2D(-6.31, -2.946));
        posMap.put("s6Co", new Vector2D(-6.31, -3.52));
        angleMap.put("s1Co", 0.0);
        angleMap.put("s1Cu", 0.0);
        angleMap.put("s2Co", 0.0);
        angleMap.put("s3Co", 0.0);
        angleMap.put("s2Cu", 0.0);
        angleMap.put("s4Co", 0.0);
        angleMap.put("s5Co", 0.0);
        angleMap.put("s3Cu", 0.0);
        angleMap.put("s6Co", 0.0);


        posMap.put("gmp1", new Vector2D(-1.199, .4946));
        posMap.put("gmp2", new Vector2D(-1.199, -.706));
        posMap.put("gmp3", new Vector2D(-1.199, -1.909));
        posMap.put("gmp4", new Vector2D(-1.199, -3.101));


        posMap.put("stationIn", new Vector2D(-5, -1.07));
        posMap.put("stationOut", new Vector2D(-4, -1.07));

        waypointMapAboutZero.put("PIH", new AutoGenWaypoint[]{new AutoGenWaypoint("placeConeHighInit", 0, transitSpeed * 1.5)});
        waypointMapAboutZero.put("cone", new AutoGenWaypoint[]{new AutoGenWaypoint("pickCone", -.8, transitSpeed), new AutoGenWaypoint("none", -.1, .25), new AutoGenWaypoint("none", .1, .25), new AutoGenWaypoint("unpick", .3, transitSpeed)});
        waypointMapAboutZero.put("cube", new AutoGenWaypoint[]{new AutoGenWaypoint("pickCubeA", -.7, transitSpeed), new AutoGenWaypoint("none", -.1, .9), new AutoGenWaypoint("none", .1, .9), new AutoGenWaypoint("unpick", .3, transitSpeed)});
        waypointMapAboutZero.put("PH", new AutoGenWaypoint[]{new AutoGenWaypoint("PlaceHighCont", -.7, transitSpeed), new AutoGenWaypoint("none", -.3, transitSpeed / 3.0), new AutoGenWaypoint("dropWithWait", transitSpeed * .8 / 3.0, transitSpeed)});
        waypointMapAboutZero.put("lock", new AutoGenWaypoint[]{new AutoGenWaypoint("lockSwerve", -.1, .6), new AutoGenWaypoint("none", -.5, .6)});


    }

    /**
     * Auto Data takes the form of a string with the following format:
     * The following is cone cube cone 2 auto from Charged up 2023 washatator
     * NAME,s1Co_PH,gmp1_cube,s2Co_PH,gmp2_cone
     */
    public SingleAuto createAuto(String autoData) {
        PolynomicSpline out = new PolynomicSpline(2, 1);
        String[] args = autoData.split(",");
        ArrayList<String> argsList = new ArrayList<>();
        for (String arg : args) {
            argsList.add(arg);
        }
        String name = argsList.remove(0);
        int numWaypoints = argsList.size();

        boolean firstItrOfString = true;
        for (int i = 0; i < numWaypoints; i++) {
            String arg = argsList.remove(0);
            String[] argSplit = arg.split("_");
            String pos = argSplit[0];
            String action = argSplit[1];
            out.addControlPoint(new DControlPoint(posMap.get(pos).toDVector()));
            if (firstItrOfString && action.equals(initActions)) {
                action = initActions;
                firstItrOfString = false;
            }


        }
        return new SingleAuto(name, out, null);
    }

}

class AutoGenWaypoint {
    private double deltaT;
    private String name;
    private double speed;

    AutoGenWaypoint(String name, double deltaT, double speed) {
        this.name = name;
        this.deltaT = deltaT;
        this.speed = speed;
    }

    public double getDeltaT() {
        return deltaT;
    }

    public double getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }
}
