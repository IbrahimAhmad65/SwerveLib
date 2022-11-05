package BSpline;

import main.Function;
import main.Vector2D;

public class CubicHermite {

    double x1, x2, y1, y2, tan1, tan2;

    // contains x f(x) f'(x)
    public CubicHermite(double x1, double x2, double y1, double y2, double tan1, double tan2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.tan1 = tan1;
        this.tan2 = tan2;
    }



    public static Function<Vector2D, Double> computeInterp(double x1, double x2, double y1, double y2, double tan1, double tan2) {
        double x1S = x1;
        double x2S = x2;
        double y1S = y1;
        double y2S = y2;
        double tan1S = tan1;
        double tan2S = tan2;
        boolean flip = false;
        if(x1-x2 == 0f){
            x1S = y1;
            x2S = y2;
            y1S = x1;
            y2S = x2;
            tan1S = 1.0/tan1;
            tan2S = 1.0/tan2;
            if(tan1S == Double.POSITIVE_INFINITY ||tan1S == Double.NEGATIVE_INFINITY || tan2S == Double.POSITIVE_INFINITY || tan2S == Double.NEGATIVE_INFINITY){
                System.out.println("tan1 " + tan1 + " tan2 " +tan2);
                throw new IllegalArgumentException("your numbers are bad lol");
            }
            flip = true;
        }
        double[] one = {y1S, y1S, y2S, y2S};
        double[] two = {tan1S, (y2S - y1S) / (x2S - x1S), tan2S};
        double[] three = {(two[1] - two[0]) / (x2S - x1S), (two[2] - two[1]) / (x2S - x1S)};
        double four = (three[1] - three[0]) / (x2S - x1S);
        double finalX1S = x1S;
        double finalX2S = x2S;
        boolean finalFlip = flip;
        return (x) -> {
            double v = four * (x - finalX2S) * (x - finalX1S) * (x - finalX1S);
            if (finalFlip){
                return new Vector2D(one[0] + two[0] * (x - finalX1S) + three[0] * (x - finalX1S) * (x - finalX1S) + v,x);
            }
            return new Vector2D(x,one[0] + two[0] * (x - finalX1S) + three[0] * (x - finalX1S) * (x - finalX1S) + v);
        };
    }

    public static Function<SplinePoint, Double> computeInterpParametric(double x1, double x2, double y1, double y2, double tan1, double tan2) {
        double x1S = x1;
        double x2S = x2;
        double y1S = y1;
        double y2S = y2;
        double tan1S = tan1;
        double tan2S = tan2;
        boolean flip = false;
        if(x1-x2 == 0f){
            x1S = y1;
            x2S = y2;
            y1S = x1;
            y2S = x2;
            tan1S = 1.0/tan1;
            tan2S = 1.0/tan2;
            if(tan1S == Double.POSITIVE_INFINITY ||tan1S == Double.NEGATIVE_INFINITY || tan2S == Double.POSITIVE_INFINITY ||tan2S == Double.NEGATIVE_INFINITY){
                System.out.println("tan1 " + tan1 + " tan2 " +tan2);
                throw new IllegalArgumentException("your numbers are bad lol");
            }
            flip = true;
        }
        double[] one = {y1S, y1S, y2S, y2S};
        double[] two = {tan1S, (y2S - y1S) / (x2S - x1S), tan2S};
        double[] three = {(two[1] - two[0]) / (x2S - x1S), (two[2] - two[1]) / (x2S - x1S)};
        double four = (three[1] - three[0]) / (x2S - x1S);
        double finalX1S = x1S;
        double finalX2S = x2S;
        boolean finalFlip = flip;

        return (x) -> {
            double min = Math.min(finalX1S,finalX2S);
            double max = Math.max(finalX1S,finalX2S);
            double xS = x*(max - min) + min;
            Vector2D pos;
            Vector2D newPos;
            Vector2D vel;
            double h = .0001;
            if (finalFlip){
                pos = new Vector2D(one[0] + two[0] * (xS - finalX1S) + three[0]
                        * (xS - finalX1S) * (xS - finalX1S) + four * (xS - finalX2S) * (xS - finalX1S) * (xS - finalX1S),xS);
                xS += h;
                newPos = new Vector2D(one[0] + two[0] * (xS - finalX1S) + three[0]
                        * (xS - finalX1S) * (xS - finalX1S) + four * (xS - finalX2S) * (xS - finalX1S) * (xS - finalX1S),xS);
                vel = newPos.clone().subtract(pos).scale(1/h);
                if(newPos.clone().subtract(pos).getX() < 0){
                    vel.scaleX(-1);
                }
                if(newPos.clone().subtract(pos).getY() < 0){
                    vel.scaleY(-1);
                }
                return new SplinePoint(pos,vel);
            }
            pos = new Vector2D(xS,one[0] + two[0] * (xS - finalX1S) + three[0]
                    * (xS - finalX1S) * (xS - finalX1S) + four * (xS - finalX2S) * (xS - finalX1S) * (xS - finalX1S));
            xS += h;
            newPos = new Vector2D(xS,one[0] + two[0] * (xS - finalX1S) + three[0]
                    * (xS - finalX1S) * (xS - finalX1S) + four * (xS - finalX2S) * (xS - finalX1S) * (xS - finalX1S));
            vel = newPos.clone().subtract(pos).scale(1/h);
            if(newPos.clone().subtract(pos).getX() < 0){
                vel.scaleX(-1);
            }
            if(newPos.clone().subtract(pos).getY() < 0){
                vel.scaleY(-1);
            }
            return new SplinePoint(pos,vel);
        };
    }

    public static String getDesmosEquations(double x1, double x2, double y1, double y2, double tan1, double tan2, String t) {
        double x1S = x1;
        double x2S = x2;
        double y1S = y1;
        double y2S = y2;
        double tan1S = tan1;
        double tan2S = tan2;
        boolean flip = false;
        if(x1-x2 == 0f){
            x1S = y1;
            x2S = y2;
            y1S = x1;
            y2S = x2;
            tan1S = 1.0/tan1;
            tan2S = 1.0/tan2;
            if(tan1S == Double.POSITIVE_INFINITY ||tan1S == Double.NEGATIVE_INFINITY || tan2S == Double.POSITIVE_INFINITY ||tan2S == Double.NEGATIVE_INFINITY){
                System.out.println("tan1 " + tan1 + " tan2 " +tan2);
                throw new IllegalArgumentException("your numbers are bad lol");
            }
            flip = true;

        }


        double[] one = {y1S, y1S, y2S, y2S};
        double[] two = {tan1S, (y2S - y1S) / (x2S - x1S), tan2S};
        double[] three = {(two[1] - two[0]) / (x2S - x1S), (two[2] - two[1]) / (x2S - x1S)};
        double four = (three[1] - three[0]) / (x2S - x1S);

        if(flip){

            return ("("+one[0] + "+ " + two[0] + " * (" + t + "-" + x1S + ") +" + three[0] + " * (" + t + "-" + x1S + ") * (" + t + "-" + x1S + ") +" + four + " * (" + t + "-" + x2S + ") * (" + t + "-" + x1S + ") * (" + t + "-" + x1S + "), " + t+ ")");
        }
        return ("(" + t + "," + one[0] + "+ " + two[0] + " * (" + t + "-" + x1S + ") +" + three[0] + " * (" + t + "-" + x1S + ") * (" + t + "-" + x1S + ") +" + four + " * (" + t + "-" + x2S + ") * (" + t + "-" + x1S + ") * (" + t + "-" + x1S + "))");
    }

    public static void main(String[] args) {
        String a = CubicHermite.getDesmosEquations(1, 5, 1, 8, 1, 1, "t");
        System.out.println(a);
        System.out.println(CubicHermite.computeInterp(1, 5, 1, 8, 1, 1).compute(1.5));

    }
}
