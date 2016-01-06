/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

package computergraphics.applications;

import java.awt.Image;

import computergraphics.framework.AbstractCGFrame;
import computergraphics.framework.Camera;
import computergraphics.framework.ImageViewer;
import computergraphics.framework.Raytracer;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.PlaneNode;
import computergraphics.scenegraph.ShaderNode;
import computergraphics.scenegraph.ShaderNode.ShaderType;
import computergraphics.scenegraph.SphereNode;

/**
 * Application for the first exercise.
 * 
 * @author Philipp Jenke
 */
public class CGFrame extends AbstractCGFrame {
  /**
   * Width of result image
   */
  static final int RAYTRACE_WIDTH  = 1920;
  /**
   * Height of result image
   */
  static final int RAYTRACE_HEIGHT = 1080;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4257130065274995543L;

	/**
	 * Constructor.
	 */
	public CGFrame(int timerInverval) {
		super(timerInverval);
		// Shader node does the lighting computation
		ShaderNode shaderNode = new ShaderNode(ShaderType.PHONG);
		getRoot().addChild(shaderNode);
		// Setup a plane
    PlaneNode plane = new PlaneNode(new Vector3(0.0, -1.0, -1.0), new Vector3(-3.0, 2.0, 0.0), new Vector3(0.0, 0.0, 3.0), new Vector3(1.0, 1.0, 1.0));
    shaderNode.addChild(plane);
    // Setup some spheres
    shaderNode.addChild(new SphereNode(0.55, 20, new Vector3(0.5, 0.0, 0.0)));
    shaderNode.addChild(new SphereNode(0.35, 20, new Vector3(-0.5, 0.5, 1.0)));
    shaderNode.addChild(new SphereNode(0.15, 20, new Vector3(0.85, 0.0, 1.0)));
    // Setup a new camera for ray tracer
    Camera camera = new Camera();
    camera.setEye(new Vector3(4.0, 1.0, 8.0));
    // Do the actual raytracing
    Raytracer rt = new Raytracer(camera, getRoot());
    Image render = rt.render(RAYTRACE_WIDTH, RAYTRACE_HEIGHT);
    new ImageViewer(render);
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see computergrafik.framework.ComputergrafikFrame#timerTick()
	 */
	@Override
	protected void timerTick() {
	  // nop
	}

	@Override
	public void keyPressed(int keyCode) {
	  // nop
	}

	/**
	 * Program entry point.
	 */
	public static void main(String[] args) {
		// The timer ticks every 1000 ms.
		new CGFrame(1000);
	}
}
