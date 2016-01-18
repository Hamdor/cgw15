package computergraphics.applications;

import static org.junit.Assert.*;

import org.junit.Test;

import computergraphics.datastructures.Ray3D;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.PlaneNode;
import computergraphics.scenegraph.SphereNode;
import computergraphics.datastructures.IntersectionResult;

public class RaytraceTest {

  @Test
  public void testSphere() {
    SphereNode sp = new SphereNode(5, 20, new Vector3(0,0,0), new Vector3(0,0,0));
    assertTrue(sp.findIntersection(new Ray3D(new Vector3(4.0, 1.0, 8.0), new Vector3(-8,-2,-16).getNormalized())) != null);
    assertTrue(sp.findIntersection(new Ray3D(new Vector3(4.0, 1.0, 8.0), new Vector3(15,0,0).getNormalized())) == null);

  }
  
  @Test
  public void testPlane() {
    Vector3 span = new Vector3(0.0, -1.0, -1.0);
    Vector3 normal = new Vector3(6.0, 9.0, 0.0).getNormalized();
    Vector3 color = new Vector3(0.8, 0.2, 0);
    PlaneNode plane = new PlaneNode(span, normal ,color, 0);
    
    assertTrue(plane.findIntersection(new Ray3D(new Vector3(4.0, 1.0, 8.0), new Vector3(0.0, -2.0, -2.0))) != null);
    assertTrue(plane.findIntersection(new Ray3D(new Vector3(4.0, 1.0, 8.0), new Vector3(0.0, 2.0, 2.0))) == null);
  }

}
