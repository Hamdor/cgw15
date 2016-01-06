/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import computergraphics.datastructures.IntersectionResult;
import computergraphics.datastructures.Ray3D;
import computergraphics.math.MathHelpers;
import computergraphics.math.Vector3;

/**
 * Geometry of a simple sphere.
 * 
 * @author Philipp Jenke
 */
public class SphereNode extends Node {

	/**
	 * Sphere radius.
	 */
	private double radius;

	/**
	 * Resolution (in one dimension) of the mesh.
	 */
	private int resolution;
	
	/**
	 * the center of this sphere
	 */
	private Vector3 center;

  private Vector3 color;

	/**
	 * Constructor.
	 */
	public SphereNode(double radius, int resolution, Vector3 center, Vector3 color) {
		this.radius = radius;
		this.resolution = resolution;
		this.center = center;
		this.color = color;
	}

	@Override
	public void drawGl(GL2 gl) {
		gl.glTranslated(center.get(0), center.get(1), center.get(2));
		GLU glu = new GLU();
		GLUquadric earth = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
		glu.gluQuadricNormals(earth, GLU.GLU_SMOOTH);
		glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);
		final int slices = resolution;
		final int stacks = resolution;
		glu.gluSphere(earth, radius, slices, stacks);
	}

	@Override
	public IntersectionResult findIntersection(Ray3D ray) {
		//Sphere equation:		0 = ||(x - center)||² 				- radius²
		//parameterized ray:			ray.pos + lambda * ray.dir
		Vector3 rayPos = ray.getPoint();
		Vector3 rayDir = ray.getDirection();
		//insert to equation:	0 = ||(	ray.pos + lambda * ray.dir - center)||² 	- radius²
		//solve pq:
		// 	  	p = (2 * ray.pos * ray.dir 			- 2 * center * ray.dir) 		/ (ray.dir²)
		double 	p = (2 * rayPos.multiply(rayDir) 	- 2 * center.multiply(rayDir)) 	/ (rayDir.multiply(rayDir));
		// 	   	q = (ray.pos² 					- 2 * ray.pos * center 			+ center² 					- radius²) 			/ (ray.dir²)
		double 	q = (rayPos.multiply(rayPos) 	- 2 * rayPos.multiply(center) 	+ center.multiply(center) 	- radius * radius) 	/ (rayDir.multiply(rayDir));
		// 		lambda = -(p / 2) +- sqrt((p² / 4) - q)
		double mphalf = -(p / 2);
		double root = Math.sqrt((p * p / 4) - q);

		final double lambda1 = mphalf + root;
		final double lambda2 = mphalf - root;
		
		return calcIntersection(ray, lambda1, lambda2);
	}
	
	private IntersectionResult calcIntersection(Ray3D ray, double lambda1, double lambda2){
	  IntersectionResult result = null;
	  //find closer solution 
	  double lambda = Math.min(lambda1, lambda2);
	  //check for valid solution
	  if (lambda > MathHelpers.EPSILON) {
	    //	    intersection = ray.pos          +       ray.dir         *     lambda 
	    Vector3 intersection = ray.getPoint().add(ray.getDirection().multiply(lambda));
	    //calculate normal by subtracting the center from the point and normalize it.
	    Vector3 normalAtIntersection = intersection.subtract(center).getNormalized();
	    result = new IntersectionResult(intersection, normalAtIntersection, this);
	  }
	  
	  return result;
	}
	
	@Override
	public Vector3 getColor() {
	  return color;
	}
}
