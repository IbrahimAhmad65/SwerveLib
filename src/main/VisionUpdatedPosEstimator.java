package main;

public class VisionUpdatedPosEstimator {

    private Kalman kalman;
    private PosEstimator posEstimator;
    public VisionUpdatedPosEstimator(Matrix x, Matrix P, Matrix F, Matrix Q, Matrix H, Matrix R) {
        kalman = new Kalman(x, P, F, Q, H, R);
        posEstimator = new PosEstimator();
    }

    public void updatePos(Matrix xPrime, double theta, Matrix z) {
        // unfinished dont know how kalman filter works
        posEstimator.updatePos(xPrime, theta);
        kalman.setX(posEstimator.getPos());
        kalman.update(z);
        posEstimator.updatePos(kalman.getX());
    }


}
