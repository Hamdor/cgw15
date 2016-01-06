package computergraphics.datastructures;

import computergraphics.math.Vector3;
import computergraphics.scenegraph.Node;

/**
 * Representation of the intersection result.
 */
public class IntersectionResult {

  public IntersectionResult() {
  }
  
  public IntersectionResult (Vector3 point, Vector3 normal, Node object) {
    this.point = new Vector3(point);
    this.normal = new Vector3(normal);
    this.object = object;
  }
  
  /**
   * The intersection happens at this point.
   */
  public Vector3 point;

  /**
   * Normal at the given point.
   */
  public Vector3 normal;

  /**
   * Intersected object
   */
  public Node object;
}
