package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.math.Vector3;

/**
 * Scene graph node which rotates all its child nodes.
 * 
 * @author Timo Haeckel
 */
public class RotationNode extends Node {

	/**
	 * translation in x-, y- and z-direction.
	 */
	private Vector3 rotate = new Vector3(0, 0, 0);

	/**
	 * rotation angle in degree
	 */
	private float angle;

	/**
	 * Constructor.
	 */
	public RotationNode(float angle, Vector3 rotate) {
		this.getRotate().copy(rotate);
		this.setAngle(angle);
	}

	@Override
	public void drawGl(GL2 gl) {
		// Remember current state of the render system
		gl.glPushMatrix();

		// Apply rotation
		gl.glRotatef(getAngle(), (float) getRotate().get(0), (float) getRotate().get(1), (float) getRotate().get(2));

		// Draw all children
		for (int childIndex = 0; childIndex < getNumberOfChildren(); childIndex++) {
			getChildNode(childIndex).drawGl(gl);
		}

		// Restore original state
		gl.glPopMatrix();

	}

	public Vector3 getRotate() {
		return rotate;
	}

	public void setRotate(Vector3 rotate) {
		this.rotate = rotate;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

}
