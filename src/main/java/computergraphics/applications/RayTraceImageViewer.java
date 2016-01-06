package computergraphics.applications;

import java.awt.Image;

import computergraphics.framework.Camera;
import computergraphics.framework.ImageViewer;
import computergraphics.framework.Raytracer;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.GroupNode;
import computergraphics.scenegraph.Node;
import computergraphics.scenegraph.PlaneNode;
import computergraphics.scenegraph.SphereNode;

public class RayTraceImageViewer {
  
  /**
   * Width of result image
   */
  static final int RAYTRACE_WIDTH  = 1920;
  /**
   * Height of result image
   */
  static final int RAYTRACE_HEIGHT = 1080;
  
  public RayTraceImageViewer() {

    //build scenegraph
    Node root = new GroupNode();
    
    //configure and add plane
    Vector3 span = new Vector3(0.0, -1.0, -1.0);
    Vector3 v1 = new Vector3(-3.0, 2.0, 0.0);
    Vector3 v2 = new Vector3(0.0, 0.0, 3.0);
    Vector3 color = new Vector3(0.8, 0.2, 0);
    PlaneNode plane = new PlaneNode(span, v1, v2, color);
    root.addChild(plane);
    
    //configure and add sphere1.
    double radius = 0.55;
    int resolution = 20;
    Vector3 center = new Vector3(0.5, 0.0, 0.0);
    SphereNode sphere1 = new SphereNode(radius, resolution, center);
    root.addChild(sphere1);
    
    //configure and add sphere2.
    Vector3 center2 = new Vector3(-0.5, 0.5, 1.0);
    double radius2 = 0.35;
    SphereNode sphere2 = new SphereNode(radius2, resolution, center2);
    root.addChild(sphere2);
    
    //configure and add sphere3.
    Vector3 center3 = new Vector3(0.85, 0.0, 1.0);
    double radius3 = 0.15;
    SphereNode sphere3 = new SphereNode(radius3, resolution, center3);
    root.addChild(sphere3);
    
    //create Camera
    Camera camera = new Camera();
    camera.setEye(new Vector3(4.0, 1.0, 8.0));
    
    //do raytracing
    Raytracer rt = new Raytracer(camera, root);
    
    //show image
    Image render = rt.render(RAYTRACE_WIDTH, RAYTRACE_HEIGHT);
    new ImageViewer(render);
  }
  
  public static void main(String[] args) {
    new RayTraceImageViewer();
  }
  
}
