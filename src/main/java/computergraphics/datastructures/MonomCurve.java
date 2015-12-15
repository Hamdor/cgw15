package computergraphics.datastructures;

import computergraphics.math.Vector3;

public class MonomCurve extends ACurve {

	public MonomCurve(Vector3[] controlPoints) {
		super(controlPoints);
	}

	@Override
	public Vector3 getFunctionValue(double t) {

		Vector3 c[] = new Vector3[3];
		
		//Calculate 
		//c0 = p0
		c[0] = calculateC0();
		//c1 = 4*p1 - 3*p0 -p2
		c[1] = calculateC1();
		//c2 = p2 - p0 - c1;
		c[2] = calculateC2();
		
		//p(t) = c0 + c1 * t + c2 * t² 
		return c[0].add(c[1].multiply(t)).add(c[2].multiply(t * t));
	}

	private Vector3 calculateC2() {
		//c2 = p2 - p0 - c1;
		return getControllPoint(2).subtract(getControllPoint(0)).subtract(calculateC1());
	}

	private Vector3 calculateC1() {
		//c1 = 4*p1 - 3*p0 -p2
		return getControllPoint(1).multiply(4).subtract(getControllPoint(0).multiply(3)).subtract(getControllPoint(2));
	}

	private Vector3 calculateC0() {
		//c0 = p0
		return getControllPoint(0);
	}


	@Override
	public Vector3 getTangent(double t, double res) {
		//Tangent at function with 3rd degree
		//p'(t) = c1 + c2 * 2 * t
		return calculateC1().add(calculateC2().multiply(2*t));
	}

}
