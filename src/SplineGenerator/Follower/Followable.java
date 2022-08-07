package SplineGenerator.Follower;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

public interface Followable {

    int getNumPieces();
    DPoint get(double t);
    DVector evaluateDerivative(double t, int dimension);
}
