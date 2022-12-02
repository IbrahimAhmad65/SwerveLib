package main.discrete;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Polygon {
    private int lines;
    private HashMap<Line, PointCollection[]> lineToPoints;
    private HashMap<PointCollection[], Line> pointsToLine;
    private int n;

    public Polygon(int n) {
        lines = 0;
        pointsToLine = new HashMap<PointCollection[], Line>();
        lineToPoints = new HashMap<Line, PointCollection[]>();
        this.n = n;
    }

    public boolean addLine(Line n) {
        lineToPoints.put(new Line(0, 2), new PointCollection[]{new PointCollection(1)});
        Set[] arr = new Set[lineToPoints.size()];
        lineToPoints.keySet().toArray(arr);
//        for (int i = 0; i < arr.length; i++) {
//        }

        PointCollection pointCollectionLow;
        PointCollection pointCollectionHigh;
        int low = Math.min(n.getPoint1(),n.getPoint2());
        int high = Math.max(n.getPoint1(),n.getPoint2());
        int[] lowArr  = new int[this.n-(high-low)];
//        int[] highArr = new int[this.n-]
        for (int i = 1; i < low; i++) {

        }
        return false;
    }


    public static void main(String[] args) {
        Polygon p = new Polygon(5);
        p.addLine(new Line(0, 1));
    }
}
