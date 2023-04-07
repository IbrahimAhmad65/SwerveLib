package SplineGenerator.Follower.sim;

import SplineGenerator.Follower.RequiredFollowerPoint;
import SplineGenerator.Follower.Waypoint;
import main.Vector2D;

import java.util.HashMap;

public class AutoGen {
    private static HashMap<String, Vector2D> posMap;
    private static HashMap<String, Waypoint> waypointMap;
    private static HashMap<String, RequiredFollowerPoint> angleMap;

    static {
        posMap = new HashMap<>();
        waypointMap = new HashMap<>();
        angleMap = new HashMap<>();

        posMap.put("s1Co", new Vector2D(-6.31, .89));
        posMap.put("s1Cu", new Vector2D(-6.31, .34));
        posMap.put("s2Co", new Vector2D(-6.31, -.205));
        posMap.put("s3Co", new Vector2D(-6.31, -.76));
        posMap.put("s2Cu", new Vector2D(-6.31, -1.31));
        posMap.put("s4Co", new Vector2D(-6.31, -1.856));
        posMap.put("s5Co", new Vector2D(-6.31, -2.41));
        posMap.put("s3Cu", new Vector2D(-6.31, -2.946));
        posMap.put("s6Co", new Vector2D(-6.31, -3.52));


        posMap.put("gmp1", new Vector2D(-1.199, .4946));
        posMap.put("gmp2", new Vector2D(-1.199, -.706));
        posMap.put("gmp3", new Vector2D(-1.199, -1.909));
        posMap.put("gmp4", new Vector2D(-1.199, -3.101));

        posMap.put("stationIn",new Vector2D( -5,-1.07));
        posMap.put("stationOut",new Vector2D( -5,-1.07));

    }
}
