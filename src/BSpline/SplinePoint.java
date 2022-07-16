package BSpline;

import main.Vector2D;

public class SplinePoint {
    private Vector2D pos;
    private Vector2D v;
    public SplinePoint(Vector2D pos, Vector2D v){
        this.pos = pos;
        this.v = v;
    }

    public Vector2D getPos(){
        return pos;
    }

    public Vector2D getVelocity(){
        return v;
    }

    public SplinePoint clone(){
        return new SplinePoint(pos.clone(),v.clone());
    }
}
