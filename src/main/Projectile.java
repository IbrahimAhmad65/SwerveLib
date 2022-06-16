package main;

public class Projectile {
    private LinearlyInterpLUT vx;
    private LinearlyInterpLUT vy;
    private Vector2D v = new Vector2D();
    public Projectile(double[] p1, double[] p2, double[] p3, double[] p4){
        vx = new LinearlyInterpLUT(p1, p3);
        vy = new LinearlyInterpLUT(p2, p4);
    }

    public Vector2D tick(double distance){
        v.setXY(vx.get(distance),vy.get(distance));
        return v;
    }
}
