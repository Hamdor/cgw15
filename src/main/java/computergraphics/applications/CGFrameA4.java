package computergraphics.applications;

import java.util.ArrayList;
import java.util.List;

import computergraphics.datastructures.HalfEdgeTriangleMesh;
import computergraphics.datastructures.Vertex;
import computergraphics.framework.AbstractCGFrame;
import computergraphics.math.LookupTable;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.ScaleNode;
import computergraphics.scenegraph.ShaderNode;
import computergraphics.scenegraph.TranslationNode;
import computergraphics.scenegraph.TriangleMeshNode;

public class CGFrameA4 extends AbstractCGFrame {
  /**
   * 
   */
  private static final long serialVersionUID = -1263569744708004459L;

  private HalfEdgeTriangleMesh mesh;

  private final double TAU              = 0.0; // Tau for surfrace calculation
  private final int    NUM_COLUMNS      = 15;  // Columns in lookup table
  private final int    NUM_TRIANGLES    = 5;   // Triangles in one column
  private final int    CORNERS          = 8;   // A cube has 8 corners :)
  private final int    RESOLUTION       = 200; // Maximum cubes for our range
  private final double VOLUME_MAX       = 2;   // Volume max
  private final double VOLUME_MIN       = -2;  // Volume mix
  private final double DISTANCE = (VOLUME_MAX * (VOLUME_MIN < 0 ? VOLUME_MAX : VOLUME_MIN) / RESOLUTION);
  

  /// Sphere function
  public static double getFunctionValueOfSphere(double x, double y, double z) {
    return ((x * x + y * y + z * z) - 1);
  }

  /// Dounut function
  public static double getFunctionValueOfDounut(double x, double y, double z) {
    double outer_rad = 1.0;
    double inner_rad = 0.5;
    return Math
        .pow((x * x + y * y + z * z + outer_rad * outer_rad
            - inner_rad * inner_rad), 2)
        - 4 * (outer_rad * outer_rad) * (x * x + y * y);
  }

  /**
   * Constructor
   *
   * @param timerInterval
   *          Timer in milliseconds
   */
  public CGFrameA4(int timerInterval) {
    super(timerInterval);

    double[][] CornerPositions = {
        { 0, 0, 0 }, // Corner 1
        { DISTANCE, 0, 0 }, // Corner 2
        { DISTANCE, DISTANCE, 0 }, // Corner 3
        { 0, DISTANCE, 0 }, // Corner 4
        { 0, 0, DISTANCE }, // Corner 5
        { DISTANCE, 0, DISTANCE }, // Corner 6
        { DISTANCE, DISTANCE, DISTANCE }, // Corner 7
        { 0, DISTANCE, DISTANCE } // Corner 8
    };

    mesh = new HalfEdgeTriangleMesh();
    ShaderNode shaderNode = new ShaderNode();
    ScaleNode scaleNode = new ScaleNode(new Vector3(1, 1, 1));
    TranslationNode transNode = new TranslationNode(new Vector3(0.0, 0.0, 0.0));

    getRoot().addChild(shaderNode);
    shaderNode.addChild(scaleNode);
    scaleNode.addChild(transNode);
    
    // Loop over our cubes
    for (double x = VOLUME_MIN; x < VOLUME_MAX; x += DISTANCE) {
      for (double y = VOLUME_MIN; y < VOLUME_MAX; y += DISTANCE) {
        for (double z = VOLUME_MIN; z < VOLUME_MAX; z += DISTANCE) {
          ArrayList<Vector3> vertices = new ArrayList<Vector3>();
          ArrayList<Double>  values = new ArrayList<Double>();
          // Loop over every corner of a cube
          for (int i = 0; i < CORNERS; ++i) {
            // Get relative corner position
            vertices.add(new Vector3(
                x + CornerPositions[i][0],
                y + CornerPositions[i][1],
                z + CornerPositions[i][2])
            );
            // Calculate the implizit function and add its values
            values.add(getFunctionValueOfDounut(vertices.get(i).get(0), vertices.get(i).get(1), vertices.get(i).get(2)));
          }
          // Create the acutaly triangle from the value
          createTriangles(vertices, values);
        }
      }
    }

    // Set half edge opposites
    //mesh.computeOppositeHalfEdges(); // TODO: Endloser loop O_o ...
    //mesh.computeTriangleNormals();
    //mesh.computeVertexNormals();
    //mesh.colorizeMesh();
    final int NohE = mesh.getNumberOfHalfEdges(); // Number of HalfEgdes
    System.out.println("Half Edges: " + NohE + "\nHalf Egdes / 3: " + NohE / 3);

    TriangleMeshNode triangleMeshNode = new TriangleMeshNode(mesh, 1);
    scaleNode.addChild(triangleMeshNode);
  }

  public void createTriangles(List<Vector3> points, List<Double> values) {
    // Calculate the index to access thr lookup table
    int caseIndex = 0;
    for (int i = 0; i < values.size(); ++i) {
      caseIndex += (values.get(i) > TAU) ? (0x01 << i) : 0;
    }
    int offset = caseIndex * NUM_COLUMNS;
    for (int i = 0; i < NUM_TRIANGLES; i++) {
      // Add triangle if lookup table give a valid value
      if (LookupTable.CUBE_FACES[offset] != -1) {
        // Calculate the triangle points (a,b,c)
        int a = mesh.addVertex(
            calcVertex(LookupTable.CUBE_FACES[offset + 0], points, values));
        int b = mesh.addVertex(
            calcVertex(LookupTable.CUBE_FACES[offset + 1], points, values));
        int c = mesh.addVertex(
            calcVertex(LookupTable.CUBE_FACES[offset + 2], points, values));
        // Add complete triangle to the triangle mesh
        mesh.addTriangle(a, b, c);
        // Adjust offset to match next entrie in table
        offset += 3;
      }
    }
  }

  /// Interpolate a vertex of a triagnle for a edge of the cube
  private Vertex calcVertex(int edgeIndex, List<Vector3> points, List<Double> values) {
    final int[] edgeEntry = LookupTable.CUBE_EDGES[edgeIndex];
    // Retrieve correspondent points / corners and their values
    final Vector3 vec1 = points.get(edgeEntry[0]);
    final Vector3 vec2 = points.get(edgeEntry[1]);
    final double val1 = values.get(edgeEntry[0]);
    final double val2 = values.get(edgeEntry[1]);
    // Create linear interpolated vertex
    return new Vertex(interpolateLinear(vec1, vec2, val1, val2));
  }

  /// Linar interpolate
  private Vector3 interpolateLinear(Vector3 vec1, Vector3 vec2, double val1, double val2) {
    final double alpha = (TAU - val1) / (val2 - val1); // Calculate alpha
    // Now we can calculuate x, y, z
    final double pointX = ((1.0 - alpha) * vec1.get(0)) + (alpha * vec2.get(0));
    final double pointY = ((1.0 - alpha) * vec1.get(1)) + (alpha * vec2.get(1));
    final double pointZ = ((1.0 - alpha) * vec1.get(2)) + (alpha * vec2.get(2));
    return new Vector3(pointX, pointY, pointZ);
  }

  @Override
  protected void timerTick() {
    // nop
  }

  public static void main(String[] args) {
    new CGFrameA4(1000);
  }
}
