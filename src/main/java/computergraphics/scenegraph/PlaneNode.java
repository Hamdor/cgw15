package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.math.MathHelpers;
import computergraphics.math.Vector3;
import computergraphics.datastructures.IntersectionResult;
import computergraphics.datastructures.Ray3D;

public class PlaneNode extends Node {
  /**
   * Span of the plane.
   */
  private Vector3      span;

  /**
   * Normal on the plane.
   */
  private Vector3      normal;

  /**
   * Color of the plane.
   */
  private Vector3      color;

  /**
   * Constructor.
   * 
   * @param span
   * @param normal
   * @param color
   */
  public PlaneNode(Vector3 span, Vector3 normal, Vector3 color) {
    this.span = span;
    this.normal = normal;
    this.color = color;
  }

  @Override
  public IntersectionResult findIntersection(Ray3D ray) {
    double lambda = (normal.multiply(span) - normal.multiply(ray.getPoint()))
        / normal.multiply(ray.getDirection());
    if (lambda > MathHelpers.EPSILON) {
      Vector3 intersectionPoint = ray.getPoint().add(ray.getDirection().multiply(lambda));
      return new IntersectionResult(intersectionPoint, normal, this);
    }
    return null;
  }

  @Override
  public void drawGl(GL2 gl) {
    // Remember current state of the render system
    gl.glPushMatrix();
//
//    gl.glBegin(GL2.GL_QUADS);
//    gl.glColor3d(color.get(0), color.get(1), color.get(2));
//    gl.glNormal3d(normal.get(0), normal.get(1), normal.get(2));
//
//    gl.glVertex3d(span.get(0), span.get(1), span.get(2));
//    gl.glVertex3d(span.get(0) + v1.get(0), span.get(1) + v1.get(1),
//        span.get(2) + v1.get(2));
//    gl.glVertex3d(span.get(0) + v2.get(0), span.get(1) + v2.get(1),
//        span.get(2) + v2.get(2));
//    gl.glVertex3d(v2.subtract(v1).add(span).get(0),
//        v2.subtract(v1).add(span).get(1), v2.subtract(v1).add(span).get(2));

    gl.glEnd();

    // Restore original state
    gl.glPopMatrix();
  }

  public Vector3 getPoint() {
    return span;
  }

  public Vector3 getNormal() {
    return normal;
  }
  @Override
  public Vector3 getColor() {
    return color;
  }
}
