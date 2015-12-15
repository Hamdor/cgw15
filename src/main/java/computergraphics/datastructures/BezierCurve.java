package computergraphics.datastructures;

import computergraphics.math.MathHelpers;
import computergraphics.math.Vector3;

public class BezierCurve extends ACurve {

	public BezierCurve(Vector3[] controlPoints) {
		super(controlPoints);
	}

  @Override
  public Vector3 getFunctionValue(double t) {
    Vector3 sum = new Vector3(0, 0, 0);
    // Calculate polygon point for BezierCurve step by step
    // slides: 36
    for (int i = 0; i < numberOfControlPoints(); ++i) {
      final Vector3 ctrlI = getControllPoint(i);     // Controll-Point (i)
      final int     N     = numberOfControlPoints(); // Number of control points (n)
      // a = (t ^ i)
      double a = Math.pow(t, i);
      // b = ( 1 - t ) ^ (n - i)
      double b = Math.pow((1 - t), (N - 1 - i));
      // c =  n! / i! - (n - i)!
      double c = (MathHelpers.factorial(N - 1) / (MathHelpers.factorial(i) * (MathHelpers.factorial(N - 1 - i))));
      // combine a, b, c ==> a * b * c ==>  (n! / i! - (n - i)!) * (t ^ i) * ( 1 - t ) ^ (n - i)
      Vector3 tmpProduct = ctrlI.multiply(c).multiply(b).multiply(a);
      // add the value for this iteration
      sum = sum.add(tmpProduct);
    }
    return sum;
  }
	
	@Override
	public Vector3 getTangent(double t) {
		// TODO Auto-generated method stub
		return new Vector3(0,0,0);
	}
}
