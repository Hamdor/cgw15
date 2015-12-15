/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

package computergraphics.applications;

import computergraphics.datastructures.TesselatedDonut;
import computergraphics.datastructures.ACurve;
import computergraphics.datastructures.ATesselatedObject;
import computergraphics.datastructures.BezierCurve;
import computergraphics.datastructures.MonomCurve;
import computergraphics.framework.AbstractCGFrame;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.ColorNode;
import computergraphics.scenegraph.CurveNode;
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

	/**
	 * Constructor.
	 */
	public CGFrame(int timerInverval) {
		super(timerInverval);

		// Shader node does the lighting computation
		ShaderNode shaderNode = new ShaderNode(ShaderType.PHONG);
		getRoot().addChild(shaderNode);

		ScaleNode scaleNode = new ScaleNode(new Vector3(0.5, 0.5, 0.5));
		shaderNode.addChild(scaleNode);
		
		ColorNode colorNode = new ColorNode(0,0,0);
		scaleNode.addChild(colorNode);

		CurveNode curve = new CurveNode(getCurve(1),2,20);
		colorNode.addChild(curve);

	}

	private ACurve getCurve(int i) {
		Vector3[] controlPoints = new Vector3[3];
		switch (i) {
		case 0:
			controlPoints [0] = new Vector3(0,0,0);
			controlPoints [1] = new Vector3(1,1,1);
			controlPoints [2] = new Vector3(0,1,0);
			break;
		case 1:
			controlPoints [0] = new Vector3(1.5,1.2,3.1);
			controlPoints [1] = new Vector3(1.2,2,4.4);
			controlPoints [2] = new Vector3(5,2,3);
			break;
		default:
			for (int index = 0; index < 3;index++){
				controlPoints[index] = new Vector3(Math.random()*i,Math.random()*i,Math.random()*i);
			}
			break;
		}

		return new MonomCurve(controlPoints);
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

	}

	/**
	 * Program entry point.
	 */
	public static void main(String[] args) {
		// The timer ticks every 10 ms.
		new CGFrame(10);
	}
}
