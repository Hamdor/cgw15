package computergraphics.datastructures;

import java.util.ArrayList;

import computergraphics.math.Vector3;

public abstract class ACurve {

	protected ArrayList<Vector3> controllPoints;

	public ACurve(Vector3[] controlPoints) {
		
		this.controllPoints = new ArrayList<Vector3>();
		for (int i = 0; i < controlPoints.length; i++) {
			this.controllPoints.add(controlPoints[i]);
		}
	}
	
	public Vector3 getControllPoint(int index) {
		return controllPoints.get(index);
	}
	
	public int numberOfControlPoints(){
		return controllPoints.size();
	}

	public abstract Vector3 getFunctionValue(double t);
	
	public abstract Vector3 getTangent(double t, double res);

}
