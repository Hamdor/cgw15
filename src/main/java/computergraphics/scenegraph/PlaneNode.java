package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.datastructures.IntersectionResult;
import computergraphics.datastructures.Ray3D;
import computergraphics.math.Vector3;

//represents a plane in HNF
public class PlaneNode extends Node {

	/**
	 * normal to the plane
	 */
	private Vector3 normal;
	
	/**
	 * point on the plane
	 */
	private Vector3 point;
	
	public PlaneNode(Vector3 normal, Vector3 point) {
		this.normal = normal.getNormalized();
		this.point = point;
	}
	
	private void drawPlane(GL2 gl){
		final int D = 100; // clipping the plane at diameter 100
		//create one random point in the plane that is not equal to existing point.
		Vector3 p1;
		do {
			p1 = getRandPointInPlane();
		} while(point.equals(p1));
		
		//calculate tangent and bi-tangent
		Vector3 t = point.subtract(p1).getNormalized(); // get a normalized tangent.
		Vector3 b = t.cross(normal); // the bi-tangent is the cross produkt of the tangent and the normal
		
		//calculate 4 corner points
		Vector3 [] v = new Vector3[4];
		//v1 = point - t*D - b*D;
		v[0] = point.subtract(t.multiply(D)).subtract(b.multiply(D));
		//v2 = point + t*D - b*D;
		v[1] = point.add(t.multiply(D)).subtract(b.multiply(D));
		//v3 = point + t*D + b*D;
		v[2] = point.add(t.multiply(D)).add(b.multiply(D));
		//v4 = point - t*D + b*D;
		v[3] = point.subtract(t.multiply(D)).add(b.multiply(D));
	
		//draw the resulting quad.
		gl.glBegin(gl.GL_QUADS);
		
		gl.glNormal3d(normal.get(0), normal.get(1), normal.get(2));
		for (int i = 0; i < 4; i++){
			gl.glVertex3d(v[i].get(0), v[i].get(1), v[i].get(2));
		}
		
		gl.glEnd();
	}
	
	private Vector3 getRandPointInPlane() {
		//TODO not functional!
		
		// get two random values vor x and y and calculate z from them.
		final double x = Math.random()*100;
		final double y = Math.random()*100;
		// n*[x-p] = 0
		// (n1 * x1 + n2 * x2 + n3 * x3) - n*p = 0
		// n3 * x3 = n*p - n1 * x1 - n2 * x2
		// x3 = (n * p - n1 * x1 - n2 * x2) / n3
		final double z = (normal.multiply(point) - normal.get(0) * x - normal.get(1) * y) / normal.get(2);
		
		return new Vector3(x,y,z);
	}

	@Override
	public void drawGl(GL2 gl) {
		drawPlane(gl);
	}
	
public IntersectionResult findIntersection(Ray3D ray) {
		
		//plane in HNF:			0 =    n *  x						    - n * p
		//parameterized ray:				ray.pos + lambda * ray.dir
		Vector3 rayPos = ray.getPoint();
		Vector3 rayDir = ray.getDirection();
		//insert to equation:	0 = ||(n * (ray.pos + lambda * ray.dir) - n * p
		// 			 lambda = (n * p			 		- n * ray.pos) 				/ (n * ray.dir)
		final double lambda = (normal.multiply(point) 	- normal.multiply(rayPos))	/ (normal.multiply(rayDir));
		
		return calcIntersection(ray, lambda);
	}
	
	private IntersectionResult calcIntersection(Ray3D ray, final double lambda){
		IntersectionResult result;
		
		if (lambda > 0){
			//create intersection result!
			result = new IntersectionResult();
			result.object = this;
			//	   point = ray.pos 			+ 		ray.dir 		*	  lambda 
			result.point = ray.getPoint().add(ray.getDirection().multiply(lambda));
			//set normal to plane normal
			result.normal = this.normal;
		} else {
			// there is no solution
			result = null;
		}
		
		return result;
	}

}
