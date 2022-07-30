package main;

public class Ball {
    private LinearlyInterpLUT vex;
    private LinearlyInterpLUT vey;
    private LinearlyInterpLUT omegaLUT;
    private double mu;
    Vector3D v;

    //                    Vector3D omegaV = new Vector3D(0, 0, omega);
    public Ball(double c, double airDensity, double crossSectionalArea, double mass, double[] vx1,
                double[] vx2, double[] vy1, double[] vy2, double[] omega1, double[] omega2) {
        mu = c * airDensity * crossSectionalArea * .5 / mass;
        vex = new LinearlyInterpLUT(vx1,vx2);
        vey = new LinearlyInterpLUT(vy1,vy2);
        omegaLUT = new LinearlyInterpLUT(omega1,omega2);
    }

    public Vector3D tick(double distance, Vector3D robotVelocity) {
        double[] b = DiffEq.rk4System3((x, y, vx, vy) -> {
            return vy / vx;
        }, (x, y, vx, vy) -> {
            Vector3D vitr = new Vector3D(vx, vy, 0);
            double sqrt = Math.sqrt(vx * vx + vy * vy);
            double omega = omegaLUT.get(sqrt);
            Vector3D omegaV = new Vector3D(0, 0, omega);
            return -(mu * vx * sqrt + vitr.crossProduct(omegaV).project(Vector3D.getI()).getRadius() * omega / Math.abs(omega)) / vx;
        }, (x, y, vx, vy) -> {
            Vector3D vitr = new Vector3D(vx, vy, 0);
            double sqrt = Math.sqrt(vx * vx + vy * vy);
            double omega = omegaLUT.get(sqrt);
            Vector3D omegaV = new Vector3D(0, 0, omega);
            return -(mu * vx * sqrt + vitr.crossProduct(omegaV).project(Vector3D.getJ()).getRadius() * omega / Math.abs(omega)) / vx;
        }, 55, 2.4026, 2.2999, 0, 2, -3);
        return v.setXYZ(b[1], b[2], -robotVelocity.getRadius());
    }
}
