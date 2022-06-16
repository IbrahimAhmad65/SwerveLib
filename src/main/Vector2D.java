package main;

// Class for handleing all forms of vector2D math
public class  Vector2D {

    // Data in this class is stored in cartesian coordinates
    private double x;
    private double y;

    // Nothing gets passed into constructor
    public Vector2D() {

    }

    public Vector2D(double x, double y) {
        setXY(x, y);
    }

    public Vector2D(Vector2D v1, Vector2D v2) {
        setX(v2.getX() - v1.getX());
        setY(v2.getY() - v1.getY());
    }


    public static Vector2D fromPolar(double theta, double r) {
        Vector2D vector2D = new Vector2D();
        vector2D.setPolar(theta, r);
        return vector2D;
    }

    // Setting x and Y through cartesian coordinates
    public Vector2D setXY(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2D setX(double x) {
        this.x = x;
        return this;
    }

    public Vector2D setY(double y) {
        this.y = y;
        return this;
    }

    public Vector2D set(Vector2D vector2D) {
        x = vector2D.getX();
        y = vector2D.getY();
        return this;
    }

    // Adds this vector2D to vector2D "that" in the cartesian space
    public Vector2D add(Vector2D that) {
        return addSelf(that);
//        Vector2D out = new Vector2D();
//        out.setXY(x+that.getX(),y+that.getY());
    }

    public Vector2D addSelf(Vector2D that) {
        this.setXY(x + that.getX(), y + that.getY());
        return this;
    }

    // Sets x and Y using in terms of theta and magnitude
    public Vector2D setPolar(double theta, double magnitude) {
        this.x = magnitude * Math.cos(theta);
        this.y = magnitude * Math.sin(theta);
        return this;
    }

    public Vector2D setTheta(double theta) {
        setPolar(theta, getMagnitude());
        return this;
    }

    public Vector2D addTheta(double theta) {
        setTheta(this.getTheta() + theta);
        return this;
    }

    public Vector2D setMagnitude(double magnitude) {
        double oldMag = getMagnitude();
        if (oldMag == 0) {
            return this;
        } else if (magnitude == 0) {
            x = 0;
            y = 0;
        }

        x *= (magnitude / oldMag);
        y *= (magnitude / oldMag);

        return this;
    }

    // Scales the vector2D using a double scalar
    public Vector2D scale(double scalar) {
        scaleX(scalar);
        scaleY(scalar);
        return this;
    }

    // Scales X
    public void scaleX(double scalar) {
        x *= scalar;
    }

    // Scales Y
    public void scaleY(double scalar) {
        y *= scalar;
    }

    // Gets X
    public double getX() {
        return x;
    }

    // Gets Y
    public double getY() {
        return y;
    }

    // Gets theta
    public double getTheta() {
        return minusPiTo2Pi(Math.atan2(y, x));
    }

    public double getATAN2() {
        return Math.atan2(y, x);
    }

    // Gets magnitude
    public double getMagnitude() {
        return pythagorean(x, y);
    }

    private double pythagorean(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    public double getDistance(Vector2D vector2D) {
        return pythagorean(x - vector2D.x, y - vector2D.y);
    }

    private double minusPiTo2Pi(double theta) {
        if (theta < 0) return theta + 2 * 3.1416;
        return theta;
    }

    public Vector2D clone() {
        return new Vector2D(getX(), getY());
    }

    @Override
    public String toString() {
        return "Vector2D{" + "x=" + x + ", y=" + y + '}';
    }
}