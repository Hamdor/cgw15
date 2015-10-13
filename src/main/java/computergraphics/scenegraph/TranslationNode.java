package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.math.Vector3;

/**
 * Scene graph node which translates all its child nodes.
 * 
 * @author Timo Haeckel
 */
public class TranslationNode extends Node {

	/**
	 * translation in x-, y- and z-direction.
	 */
	private Vector3 translate = new Vector3(0, 0, 0);

	/**
	 * Constructor.
	 */
	public TranslationNode(Vector3 translate) {
		this.getTranslate().copy(translate);
	}

	@Override
	public void drawGl(GL2 gl) {
		// Remember current state of the render system
		gl.glPushMatrix();

		// Apply translation
		gl.glTranslatef((float) getTranslate().get(0), (float) getTranslate().get(1), (float) getTranslate().get(2));

		// Draw all children
		for (int childIndex = 0; childIndex < getNumberOfChildren(); childIndex++) {
			getChildNode(childIndex).drawGl(gl);
		}

		// Restore original state
		gl.glPopMatrix();

	}

	public Vector3 getTranslate() {
		return translate;
	}

	public void setTranslate(Vector3 translate) {
		this.translate = translate;
	}

}