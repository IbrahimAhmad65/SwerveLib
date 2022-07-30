package SSpline;

import main.Vector2D;

public class Row implements Comparable{
    public Vector2D v;
    public Vector2D velocity;
    public double angle;
    public Row(Vector2D v, Vector2D velocity, double angle){
        this.v = v;
        this.velocity = velocity;
        this.angle = angle;
    }

    public Row clone(){
        return new Row(this.v.clone(),this.velocity.clone(),this.angle);
    }

    @Override
    public String toString() {
        return "Row{" +
                "v=" + v +
                ", velocity=" + velocity +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return  v.getMagnitude() - ((Row) o).v.getMagnitude() > 0 ? 1 : (v.getMagnitude() - ((Row) o).v.getMagnitude() == 0 ? 0 : -1);
    }
}
