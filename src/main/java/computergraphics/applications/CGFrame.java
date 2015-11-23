/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

package computergraphics.applications;

import computergraphics.datastructures.HalfEdgeTriangleMesh;
import computergraphics.datastructures.ITriangleMesh;
import computergraphics.datastructures.ObjIO;
import computergraphics.framework.AbstractCGFrame;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.ColorNode;
import computergraphics.scenegraph.ScaleNode;
import computergraphics.scenegraph.ShaderNode;
import computergraphics.scenegraph.ShaderNode.ShaderType;
import computergraphics.scenegraph.TriangleMeshNode;

/**
 * Application for the first exercise.
 * 
 * @author Philipp Jenke
 */
public class CGFrame extends AbstractCGFrame {

  /**
   * 
   */
  private static final long serialVersionUID = 4257130065274995543L;
  private ObjIO             objIO;
  private TriangleMeshNode triangleMeshNode;
  HalfEdgeTriangleMesh mesh;

  /**
   * Constructor.
   */
  public CGFrame(int timerInverval, ObjIO objIO) {
    super(timerInverval);
    this.objIO = objIO;

    // Shader node does the lighting computation
    ShaderNode shaderNode = new ShaderNode(ShaderType.PHONG);
    getRoot().addChild(shaderNode);

    ColorNode colorNode = new ColorNode(0.445,0.32,0.60);
    shaderNode.addChild(colorNode);

    ScaleNode scaleNode = new ScaleNode(new Vector3(2.0, 2.0, 2.0));
    colorNode.addChild(scaleNode);

    // Read Triangle Mesh!
    mesh = readTriangleMesh("meshes//cow.obj");
    triangleMeshNode = new TriangleMeshNode(mesh,1);
        
    scaleNode.addChild(triangleMeshNode);
  }

  private HalfEdgeTriangleMesh readTriangleMesh(String inFile) {
    HalfEdgeTriangleMesh mesh = new HalfEdgeTriangleMesh();
    // Read mesh
    objIO.einlesen(inFile, mesh);
    // Set half edge opposites
    mesh.computeOppositeHalfEdges();
    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();
    mesh.colorizeMesh();
    final int NohE = mesh.getNumberOfHalfEdges(); // Number of HalfEgdes
    System.out.println("Half Edges: " + NohE + "\nHalf Egdes / 3: " + NohE / 3);

    return mesh;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see computergrafik.framework.ComputergrafikFrame#timerTick()
   */
  @Override
  protected void timerTick() {

  }

  @Override
  public void keyPressed(int keyCode) {
    System.out.println("Key pressed: " + (char) keyCode);
    if(keyCode == 'S'||keyCode == 's'){
    	mesh.laplaceSmoothing();
    	mesh.colorizeMesh();
    	triangleMeshNode.update();
    }
    
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    // The timer ticks every 10 ms.
    ObjIO objectIO = new ObjIO();
    new CGFrame(10, objectIO);
  }
}
