package computergraphics.datastructures;

public class TesselatedDonut extends ATesselatedObject {

	private final double innerRadius;
	private final double outerRadius;

	public TesselatedDonut(double innerRadius, double outerRadius) {
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		fillMesh();
	}
	
	@Override
	protected double getFunctionValue(double x, double y, double z) {
	    return Math
	        .pow((x * x + y * y + z * z + outerRadius * outerRadius
	            - innerRadius * innerRadius), 2)
	        - 4 * (outerRadius * outerRadius) * (x * x + y * y);
	}

}
