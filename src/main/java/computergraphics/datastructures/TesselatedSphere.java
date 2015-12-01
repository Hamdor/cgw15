package computergraphics.datastructures;

public class TesselatedSphere extends TesselatedObject {

	private final double radius;
	
	public TesselatedSphere(double radius) {
		this.radius = radius;
		fillMesh();
	}
	
	@Override
	protected double getFunctionValue(double x, double y, double z) {
		return ((x * x + y * y + z * z) - radius*radius);
	}

}
