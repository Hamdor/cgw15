package computergraphics.datastructures;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import computergraphics.math.Vector3;

public abstract class ACurve {

	protected Map<Vector3, Double> controllPoints;

	public ACurve(Vector3[] controlPoints, double[] values) {
		if (controlPoints.length != values.length) {
			throw new InvalidParameterException("Number of control points != number of belonging values!");
		}
		
		this.controllPoints = new HashMap<Vector3, Double>();
		for (int i = 0; i < controlPoints.length; i++) {
			this.controllPoints.put(controlPoints[i], values[i]);
		}
	}

	public abstract double getFunctionValue(double x);

}
