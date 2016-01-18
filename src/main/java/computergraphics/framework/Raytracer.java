package computergraphics.framework;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import computergraphics.datastructures.IntersectionResult;
import computergraphics.datastructures.Ray3D;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.LightSource;
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

  private ArrayList<LightSource> lights;

  /**
   * LightSource
   */
  LightSource defaultLight = new LightSource(new Vector3(1.0, 3.0, 6.0), new Vector3(1, 0, 0));

  /**
   * Constructor.
   * 
   * @param camera
   *          Scene camera.
   * @param rootNode
   *          Root node of the scenegraph.
   */
  public Raytracer(Camera camera, Node rootNode, LightSource... lightSources) {
    this.camera = camera;
    this.rootNode = rootNode;
    lights = new ArrayList<LightSource>();
    if (lightSources.length > 0) {
      for (LightSource lightSource : lightSources) {
        lights.add(lightSource);
      }
    } else {
      lights.add(defaultLight);
    }

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
    Vector3 ret = new Vector3(0, 0, 0);// new Vector3(Math.random(),
                                         // Math.random(), Math.random());

    // get all nodes of the scenegraph
    ArrayList<Node> nodes = rootNode.getAllNodesBelow();

    // cross the ray with all Objects.
    ArrayList<IntersectionResult> results = findIntersections(ray, nodes);

    // calculate the nearest intersection
    IntersectionResult nearest = getNearestIntersection(results, ray);

    if (nearest != null) {
      Vector3 color = new Vector3(0,0,0);
      for (LightSource lightSource : lights) {
        // evaluate if the object is in shade.
        boolean nodeInShade = checkIfNodeInShade(nearest, nodes, lightSource);

        if (!nodeInShade) {
          // calculate color with phong lighting model
          color = color.add(calculatePhongLighting(nearest, ray, lightSource));
        }
      }

      // start recursion if depth < 5
      if (recursion < 5) {
        double reflectionFactor = nearest.object.getReflectionFactor(); //TODO calculate recursion multiplier
        // create new Ray r = v − 2(v*n) * n
        double scalar = ray.getDirection().multiply(nearest.normal);
        Vector3 r = ray.getDirection().subtract(nearest.normal.multiply(scalar * 2));
        Ray3D recursionBeam = new Ray3D(nearest.point, r);
        Vector3 reflexion = trace(recursionBeam, recursion+1);
        
        ret = color.multiply(1 - reflectionFactor).add(reflexion.multiply(reflectionFactor));
      }
    }
    return ret;
  }

  private Vector3 calculatePhongLighting(IntersectionResult intersection, Ray3D ray, LightSource light) {
    Vector3 color = new Vector3(0, 0, 0);
    // calculate constant vectors
    final Vector3 N = intersection.normal.getNormalized(); // Surfacenormal
    final Vector3 Vs = ray.getDirection().getNormalized(); // ray direction
    // final Vector3 E =
    // camera.getEye().subtract(intersection.point).getNormalized(); //vector
    // from intercection to eyepoint
    final Vector3 L = light.getPosition().subtract(intersection.point).getNormalized(); // vector
                                                                                        // from
                                                                                        // intercection
                                                                                        // to
                                                                                        // the
                                                                                        // lightsource
    final int m = 20;

    // calculate color
    // calculate diffuse portion: (N⋅L)⋅<Objektfarbe>, falls N⋅L > 0, else black
    Vector3 diffuse = new Vector3(0, 0, 0);
    final double NL = N.multiply(L);
    if (NL > 0) {
      diffuse = intersection.object.getColor().multiply(NL);
    }

    // calculate speculare portion: (R⋅(-Vs )) ⋅ (1,1,1) mit R = L-2(L⋅N)⋅N,
    // falls R⋅(-Vs ) > 0, else black
    Vector3 speculare = new Vector3(0, 0, 0);
    // colorspec = (R⋅(-VS))m⋅ (1,1,1) mit R = L-2(L⋅N)⋅N, falls R⋅(-VS) > 0,
    final Vector3 R = (L.subtract((N.multiply(L.multiply(N)).multiply(2.0)))).getNormalized();// L.subtract(N.multiply(N.multiply(L)*2));
    final double RVs = R.multiply(Vs.multiply(1));
    if (RVs > 0) {
      speculare = (new Vector3(1, 1, 1)).multiply(Math.pow(RVs, m));
    }

    return color.add(speculare).add(diffuse);
  }

  private ArrayList<IntersectionResult> findIntersections(Ray3D ray, ArrayList<Node> nodes) {
    ArrayList<IntersectionResult> results = new ArrayList<IntersectionResult>();
    for (Node node : nodes) {
      IntersectionResult iresult = node.findIntersection(ray);
      if (iresult != null)
        results.add(iresult);
    }
    return results;
  }

  private boolean checkIfNodeInShade(IntersectionResult nearest, ArrayList<Node> nodes, LightSource light) {
    Ray3D ray = new Ray3D(nearest.point, light.getPosition());
    return !findIntersections(ray, nodes).isEmpty();
  }

  private IntersectionResult getNearestIntersection(ArrayList<IntersectionResult> intercections, Ray3D ray) {
    IntersectionResult nearest = null;

    for (IntersectionResult intersectionResult : intercections) {
      if (nearest == null) {
        // nearest hasn't been set yet
        nearest = intersectionResult;

      } else if (intersectionResult != null && intersectionResult.point.subtract(ray.getPoint())
          .getNorm() < nearest.point.subtract(ray.getPoint()).getNorm()) {
        // Intersection result closer than nearest. So change nearest.
        nearest = intersectionResult;
      }
    }

    return nearest;
  }

}