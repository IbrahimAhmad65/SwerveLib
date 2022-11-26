package SplineGenerator.Follower;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

// Required for any object that a spline follower to follow
public interface Followable {
    //Returns the number of segments
    int getNumPieces();

    // Returns the position on a spline after given the t value
    DPoint get(double t);

    // Returns the dimensionth gradient given the t value
    DVector evaluateDerivative(double t, int dimension);
}