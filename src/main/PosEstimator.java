package main;

public class PosEstimator {

    Matrix pos;

    public PosEstimator() {
        pos = new Matrix(3, 1);
    }

    public void updatePos(Matrix pos) {
        this.pos = pos;
    }

    public void updatePos(Matrix xPrime, double theta) {
        // theta is before this tick
        // vx and vy are in the robot centric
        Matrix rotation = new Matrix(new double[][]{{Math.cos(theta), -Math.sin(theta), 0}, {Math.sin(theta), Math.cos(theta), 0}, {0, 0, 1}});
        double dTheta = xPrime.getData()[2][0];
        Matrix exponential;
        if (dTheta < 1e-6) {
            exponential = new Matrix(new double[][]{{1 - dTheta * dTheta / 6.0, -dTheta / 2.0, 0}, {dTheta / 2.0, 1 - dTheta * dTheta / 6.0, 0}, {0, 0, 1}});
        } else {
            exponential = new Matrix(new double[][]{{Math.sin(dTheta) / dTheta, (Math.cos(dTheta) - 1) / dTheta, 0}, {(1 - Math.cos(dTheta)) / dTheta, Math.sin(dTheta) / dTheta, 0}, {0, 0, 1}});

        }

        Matrix translation = rotation.multiply(exponential);
        translation = translation.multiply(xPrime);
        pos = pos.add(translation);
    }


    public Matrix getPos() {
        return pos;
    }

    public static void main(String[] args) {
        PosEstimator posEstimator = new PosEstimator();
        Matrix xPrime;
        for (int i = 0; i < 100; i++) {
            xPrime = new Matrix(new double[][]{{0.1 * Math.cos(posEstimator.getPos().getData()[2][0])}, {0 * Math.sin(-posEstimator.getPos().getData()[2][0])}, {.5}});
            posEstimator.updatePos(xPrime, posEstimator.getPos().getData()[2][0]);
            System.out.println(posEstimator.getPos().T().get2DVector(0)[0]);
        }
    }
}
