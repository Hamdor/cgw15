package computergraphics.applications;

import computergraphics.datastructures.MonomCurve;
import computergraphics.math.Vector3;

public class Test {

	public static void main(String[] args) {
		Vector3[] controlPoints = new Vector3[3];
		for (int i = 0; i < controlPoints.length; i++) {
			controlPoints[i] = new Vector3(i, i * 2, 0);
		}

		MonomCurve curve = new MonomCurve(controlPoints);
		
		System.out.println(curve.getFunctionValue(0).equals(controlPoints[0]));
		System.out.println(curve.getFunctionValue(1).equals(controlPoints[2]));
	}

}
