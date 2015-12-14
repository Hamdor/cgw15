/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

package computergraphics.applications;

import computergraphics.datastructures.TesselatedDonut;
import computergraphics.datastructures.ATesselatedObject;
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
  private TriangleMeshNode triangleMeshNode;
  private ATesselatedObject object;

  /**
   * Constructor.
   */
  public CGFrame(int timerInverval) {
    super(timerInverval);

    // Shader node does the lighting computation
    ShaderNode shaderNode = new ShaderNode(ShaderType.PHONG);
    getRoot().addChild(shaderNode);

    ColorNode colorNode = new ColorNode(0.445,0.32,0.60);
    shaderNode.addChild(colorNode);

    ScaleNode scaleNode = new ScaleNode(new Vector3(1.0, 1.0, 1.0));
    colorNode.addChild(scaleNode);


    object = new TesselatedDonut(0.5,1.0);
    
    triangleMeshNode = new TriangleMeshNode(object.getMesh(),1);
        
    scaleNode.addChild(triangleMeshNode);
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
    	object.getMesh().laplaceSmoothing();
    	triangleMeshNode.update();
    }
    
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    // The timer ticks every 10 ms.
    new CGFrame(10);
  }
}
