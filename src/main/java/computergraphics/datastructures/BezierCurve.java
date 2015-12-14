package computergraphics.datastructures;

import computergraphics.math.MathHelpers;
import computergraphics.math.Vector3;

public class BezierCurve extends ACurve {

	public BezierCurve(Vector3[] controlPoints) {
		super(controlPoints);
	}

	@Override
	public Vector3 getFunctionValue(double t) {
		Vector3 p = new Vector3(0,0,0);
		int n = controllPoints.size();
		for (int i = 0; i < n; i++) {
			//b(t) = (n over i) * t^i * (1-t)^(n-i)
			double b = MathHelpers.over(n, i) * Math.pow(t, i) * Math.pow(1-t, n-i);
			p.add(controllPoints.get(i).multiply(b));
		}
		return p;
	}
	
	@Override
	public Vector3 getTangent(double t) {
		// TODO Auto-generated method stub
		return new Vector3(0,0,0);
	}


}
