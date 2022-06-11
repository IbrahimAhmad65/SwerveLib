package main;

public class Vector3D {

    private double x;
    private double y;
    private double z;

    public  Vector3D() {
    }

    public Vector3D(double x, double y, double z) {
        setXYZ(x, y, z);
    }

    public double dotProduct(Vector3D vectorIn) {
        return (vectorIn.x * x + vectorIn.y * y + vectorIn.z * z);
    }

    public Vector3D crossProduct(Vector3D vectorIn) {
        return Vector3D.add(Vector3D.add(getI().scale(y * vectorIn.z - z * vectorIn.y), getJ().scale(z * vectorIn.x - x * vectorIn.z)), getK().scale(x * vectorIn.y - y * vectorIn.x));
    }

    public static Vector3D add(Vector3D vector3D1, Vector3D vector3D2) {
        Vector3D out = new Vector3D();
        out.setXYZ(vector3D1.x + vector3D2.x, vector3D1.y + vector3D2.y, vector3D1.z + vector3D2.z);
        return out;
    }

    public Vector3D scale(double scalar) {
        setXYZ(x * scalar, y * scalar, z * scalar);
        return this;
    }

    public Vector3D setRadius(double r) {
        setSpherical(r, getAzimuthal(), getPolar());
        return this;
    }

    public double getAngleBetween(Vector3D vectorIn) {
        return dotProduct(vectorIn) / getRadius() / vectorIn.getRadius();
    }

    public Vector3D setSpherical(double radius, double azimuthal, double polar) {
        x = radius * Math.cos(azimuthal) * Math.sin(polar);
        y = radius * Math.sin(azimuthal) * Math.sin(polar);
        z = radius * Math.cos(polar);
        return this;
    }

    private double pythagorean(double a, double b, double c) {
        return Math.sqrt(a * a + b * b + c * c);
    }

    public double getRadius() {
        return pythagorean(x, y, z);
    }


    public double getAzimuthal() {
        return Math.atan2(y, x);
    }

    public double getPolar() {
        return Math.atan2(x, z);
    }

    public Vector3D project(Vector3D axis) {
        Vector3D out = axis.clone();
        out.scale(this.dotProduct(axis) / (axis.dotProduct(axis)));
        return out;
    }

    public Vector3D setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vector2D flattenK() {
        return new Vector2D(getX(), getY());
    }

    public Vector2D flattenI() {
        return new Vector2D(getZ(), getY());
    }

    public Vector2D flattenJ() {
        return new Vector2D(getX(), getZ());
    }

    public static Vector3D getI() {
        return new Vector3D(1, 0, 0);
    }

    public static Vector3D getJ() {
        return new Vector3D(0, 1, 0);
    }

    public static Vector3D getK() {
        return new Vector3D(0, 0, 1);
    }

    ;

    @Override
    public String toString() {
        return "Vector3D{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    public Vector3D clone() {
        return new Vector3D(this.x, this.y, this.z);
    }
}
