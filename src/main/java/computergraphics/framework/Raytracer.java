package computergraphics.framework;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import computergraphics.datastructures.IntersectionResult;
import computergraphics.datastructures.Ray3D;
import computergraphics.framework.Camera;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.Node;

/**
 * Creates a raytraced image of the current scene.
 */
public class Raytracer {

  /**
   * Reference to the current camera.
   */
  private final Camera camera;

  /**
   * Reference to the root node of the scenegraph.
   */
  private final Node rootNode;

  /**
   * Constructor.
   * 
   * @param camera
   *          Scene camera.
   * @param rootNode
   *          Root node of the scenegraph.
   */
  public Raytracer(Camera camera, Node rootNode) {
    this.camera = camera;
    this.rootNode = rootNode;
  }

  /**
   * Creates a raytraced image for the current view with the provided
   * resolution. The opening angle in x-direction is grabbed from the camera,
   * the opening angle in y-direction is computed accordingly.
   * 
   * @param resolutionX
   *          X-Resolution of the created image.
   * 
   * @param resolutionX
   *          Y-Resolution of the created image.
   */
  public Image render(int resolutionX, int resolutionY) {
    BufferedImage image = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_RGB);

    Vector3 viewDirection = camera.getRef().subtract(camera.getEye()).getNormalized();
    Vector3 xDirection = viewDirection.cross(camera.getUp()).getNormalized();
    Vector3 yDirection = viewDirection.cross(xDirection).getNormalized();
    double openingAngleYScale = Math.sin(camera.getOpeningAngle() * Math.PI / 180.0);
    double openingAngleXScale = openingAngleYScale * (double) resolutionX / (double) resolutionY;

    for (int i = 0; i < resolutionX; i++) {
      double alpha = (double) i / (double) (resolutionX + 1) - 0.5;
      for (int j = 0; j < resolutionY; j++) {
        double beta = (double) j / (double) (resolutionY + 1) - 0.5;
        Vector3 rayDirection = viewDirection.add(xDirection.multiply(alpha * openingAngleXScale))
            .add(yDirection.multiply(beta * openingAngleYScale)).getNormalized();
        Ray3D ray = new Ray3D(camera.getEye(), rayDirection);

        Vector3 color = trace(ray, 0);

        // Adjust color boundaries
        for (int index = 0; index < 3; index++) {
          color.set(index, Math.max(0, Math.min(1, color.get(index))));
        }

        image.setRGB(i, j,
            new Color((int) (255 * color.get(0)), (int) (255 * color.get(1)), (int) (255 * color.get(2))).getRGB());
      }
    }

    return image;
  }

  /**
   * Compute a color from tracing the ray into the scene.
   * 
   * @param ray
   *          Ray which needs to be traced.
   * @param recursion
   *          Current recursion depth. Initial recursion depth of the rays
   *          through the image plane is 0. This parameter is used to abort the
   *          recursion.
   * 
   * @return Color in RGB. All values are in [0,1];
   */
  private Vector3 trace(Ray3D ray, int recursion) {
    Vector3 color = new Vector3(0, 0, 0);

    // get all nodes of the scenegraph
    ArrayList<Node> nodes = rootNode.getAllNodesBelow();
    ArrayList<IntersectionResult> results = new ArrayList<IntersectionResult>();

    // cross the ray with all Objects.
    for (Node node : nodes) {
      results.add(node.findIntersection(ray));
    }

    // calculate the nearest intersection
    IntersectionResult nearest = getNearestIntersection(results, ray);

    if (nearest != null) {
      // evaluate if the object is in shade.
      boolean nodeInShade = checkIfNodeInShade(nearest);

      if (!nodeInShade) {
        // calculate color with phong lighting model
      }
    }

    return color;

  }

  private boolean checkIfNodeInShade(IntersectionResult nearest) {
    // TODO Auto-generated method stub
    return false;
  }

  private IntersectionResult getNearestIntersection(ArrayList<IntersectionResult> intercections, Ray3D ray) {
    IntersectionResult nearest = null;
    
    for (IntersectionResult intersectionResult : intercections) {
      if (nearest == null){
        //nearest hasn't been set yet
        nearest = intersectionResult;
    
      } else if (intersectionResult.point.subtract(ray.getPoint()).getNorm() < nearest.point.subtract(ray.getPoint()).getNorm()) {
        //Intersection result closer than nearest. So change nearest.
        nearest = intersectionResult;
      }
    }
    
    return nearest;
  }

}