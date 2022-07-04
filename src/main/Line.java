package main;

public class Line implements Function<Double,Double>{
    private Vector2D low;
    private double slope = 0;
    public Line(Vector2D pointA, Vector2D pointB){
        low = pointA.clone();
        slope = (pointA.getY() - pointB.getY())/(pointA.getX() - pointB.getX());
    }

    public Double compute(Double x) {
        double off = x - low.getX();
        System.out.println(off);
        return off*slope+low.getY();
    }

    public static void main(String[] args) {
        Line b = new Line(new Vector2D(1,2),new Vector2D(2,3));
        System.out.println(b.compute(.5));
    }
}
