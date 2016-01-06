package computergraphics.scenegraph;

import java.util.Set;

import com.jogamp.opengl.GL2;

import computergraphics.datastructures.ACurve;
import computergraphics.math.Vector3;

public class CurveNode extends Node {

	/**
	 * The represented Curve.
	 */
	private ACurve curve;

	/**
	 * has the Triangle Mesh been added as a gl List?
	 */
	private boolean listUpToDate;

	/**
	 * Number of the gl List.
	 */
	private int listNumber;

	/**
	 * The resolution of the Curve.
	 */
	private int resolution;

	public CurveNode(ACurve curve, int listNumber, int resolution) {
		this.curve = curve;
		this.resolution = resolution;
		this.listNumber = listNumber;
		this.listUpToDate = false;
	}

	public void update() {
		listUpToDate = false;
	}

	private void drawCurve(GL2 gl) {

		// add all points.
		for (int i = 0; i < curve.numberOfControlPoints(); i++) {
			//TranslationNode point = new TranslationNode(curve.getControllPoint(i));
			addChild(new SphereNode(0.05, 20, curve.getControllPoint(i), new Vector3(0,1,0)));
			//addChild(point);
		}
		// draw function
		gl.glBegin(GL2.GL_LINES);
		for (int i = 0; i < resolution; i++) {
			Vector3 start = curve.getFunctionValue((1.0 / resolution) * i);
			gl.glVertex3d(start.get(0), start.get(1), start.get(2));
			Vector3 end = curve.getFunctionValue((1.0 / resolution) * (i + 1));
			gl.glVertex3d(end.get(0), end.get(1), end.get(2));
		}
		gl.glEnd();

		// draw Tangent
		double tangent = 0.7;
//		TranslationNode point = new TranslationNode(curve.getFunctionValue(tangent));
		ColorNode color = new ColorNode(255./255.,140./255.,0./255.);
//		point.addChild(new SphereNode(0.1, 20));
//		color.addChild(point);
//		addChild(color);
		color.addChild(new SphereNode(0.1, 20, curve.getFunctionValue(tangent), new Vector3(0,1,0)));
    Vector3 tang = curve.getTangent(tangent, resolution).getNormalized();
    Vector3 start = curve.getFunctionValue(tangent);
    gl.glBegin(GL2.GL_LINES);
    gl.glVertex3d(start.get(0) - tang.get(0), start.get(1) - tang.get(1), start.get(2) - tang.get(2));
    gl.glVertex3d(start.get(0) + tang.get(0), start.get(1) + tang.get(1), start.get(2) + tang.get(2));
    gl.glEnd();

		listUpToDate = true;
	}

	@Override
	public void drawGl(GL2 gl) {
		if (!listUpToDate) {
			gl.glNewList(listNumber, GL2.GL_COMPILE);
			drawCurve(gl);
			gl.glEndList();
		}
		gl.glCallList(listNumber);

		// Draw all children
		for (int childIndex = 0; childIndex < getNumberOfChildren(); childIndex++) {
			getChildNode(childIndex).drawGl(gl);
		}
	}

}
