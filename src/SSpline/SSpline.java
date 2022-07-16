package SSpline;

import main.Vector2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Supplier;

public class SSpline {
    private ArrayList<Row> points;
    // must be the same length and match up
    // points should not go past 45k,45k or else issues will be had
    public SSpline(Vector2D[] points, double[] velocities, double[] angle){
        this.points = new ArrayList<Row>();
        for (int i = 0; i < points.length -1; i++) {
            Vector2D v = new Vector2D(points[i+1].getX()-points[i].getX(),points[i+1].getY()-points[i].getY());
            this.points.add(new Row(points[i],v.setMagnitude(velocities[i]),angle[i]));
        }
        this.points.add(new Row(points[points.length-1],new Vector2D(0,0),angle[points.length-1]));
        this.points.add(new Row(new Vector2D(100000,100000),new Vector2D(0,0),-4527));
    }

    public Vector2D getVelocity(Vector2D pos){
        Row near = findNearestPoint(pos);
        Row nearV = near.clone();
        nearV.v = near.v.subtract(pos).scale(1);
        if(nearV.v.getMagnitude() < .01){
            points.remove(near);
            near = findNearestPoint(pos);
            nearV = near.clone();
            nearV.v = near.v.subtract(pos).scale(1);
        }
        if(points.size() > 1){
            return nearV.v.clone();
        }
        if(points.size() == 0){
            return (new Vector2D(0,0));
        }
//        System.out.println(points);
        return nearV.v;
    }

    public Row findNearestPoint(Vector2D pos){
        for (int i = 0; i < points.size(); i++) {
            points.get(i).v  = points.get(i).v.subtract(pos);
        }
        Collections.sort(points);
        for (int i =0; i < points.size(); i++) {
            this.points.get(i).v.add(pos);
        }
        if(points.size() == 1){
            return new Row(pos,new Vector2D(0,0),-4527);
        }
        return points.get(0);
    }
    
    public static void main(String[] args) {
        Vector2D[] v = new Vector2D[]{new Vector2D(1,1),new Vector2D(1,2),new Vector2D(2,2),new Vector2D(6,7)};
        SSpline sSpline = new SSpline(v,new double[]{1,1,1,1},new double[]{1,2,3,4});
        Vector2D pos = new Vector2D(0,0);
        System.out.println("(1,1)");
        System.out.println("(1,2)");
        System.out.println("(2,2)");
        System.out.println("(6,7)");
        for (int i = 0; i < 1000; i++) {
            pos.add(sSpline.getVelocity(pos).scale(.05));
            System.out.println("(" + pos.getX()+","+pos.getY() + ")");
        }
    }
}
