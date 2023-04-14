package main;

public class Kalman {
private Matrix x;
    private Matrix P;
    private Matrix F;
    private Matrix Q;
    private Matrix H;
    private Matrix R;

    public Kalman(Matrix x, Matrix P, Matrix F, Matrix Q, Matrix H, Matrix R) {
        // x is the state vector
        // P is the covariance matrix
        // F is the state transition matrix
        // Q is the process noise covariance matrix
        // H is the measurement matrix
        // R is the measurement noise covariance matrix
        this.x = x;
        this.P = P;
        this.F = F;
        this.Q = Q;
        this.H = H;
        this.R = R;
    }

    public void update(Matrix z) {
        // z is the measurement vector
        Matrix xPrime = F.multiply(x);
        // xPrime is the predicted state vector
        Matrix PPrime = F.multiply(P).multiply(F.T()).add(Q);
        // PPrime is the predicted covariance matrix
        Matrix K = PPrime.multiply(H.T()).multiply((H.multiply(PPrime).multiply(H.T()).add(R)).inverse());
        // K is the Kalman gain
        x = xPrime.add(K.multiply(z.subtract(H.multiply(xPrime))));
        // x is the updated state vector
        P = PPrime.subtract(K.multiply(H).multiply(PPrime));
        // P is the updated covariance matrix
    }

    public Matrix getX() {
        return x;
    }

    public Matrix getP() {
        return P;
    }

    public Matrix getF() {
        return F;
    }

    public Matrix getQ() {
        return Q;
    }

    public Matrix getH() {
        return H;
    }

    public Matrix getR() {
        return R;
    }

    public void setX(Matrix x) {
        this.x = x;
    }

    public void setP(Matrix P) {
        this.P = P;
    }

    public void setF(Matrix F) {
        this.F = F;
    }

    public void setQ(Matrix Q) {
        this.Q = Q;
    }

    public void setH(Matrix H) {
        this.H = H;
    }

    public void setR(Matrix R) {
        this.R = R;
    }

    public static void main(String[] args) {
        Matrix x = new Matrix(new double[][]{{0}, {0}, {0}});
        Matrix P = new Matrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        Matrix F = new Matrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        Matrix Q = new Matrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        Matrix H = new Matrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        Matrix R = new Matrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        Kalman kalman = new Kalman(x, P, F, Q, H, R);
        Matrix z;
        for (int i = 0; i < 100; i++) {
            z = new Matrix(new double[][]{{Math.cos(kalman.getX().getData()[2][0])}, {Math.sin(kalman.getX().getData()[2][0])}, {0}});
            kalman.update(z);
            System.out.println(kalman.getX().T().get2DVector(0)[0]);
        }
    }
}
