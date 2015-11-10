package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.datastructures.HalfEdge;
import computergraphics.datastructures.ITriangleMesh;
import computergraphics.datastructures.TriangleFacet;
import computergraphics.datastructures.Vertex;
import computergraphics.math.Vector3;

public class TriangleMeshNode extends Node {

	/**
	 * the Triangle Mesh to draw
	 */
	private final ITriangleMesh triangleMesh;

	/**
	 * has the Triangle Mesh been added as a gl List?
	 */
	private boolean listExists;

	/**
	 * Number of the gl List.
	 */
	private int listNumber;

	/**
	 * Constructor:
	 * @param triangleMesh 	the TriangleMesh to be drawn
	 * @param listNumber 	the List number for the new gl list.
	 */
	public TriangleMeshNode(ITriangleMesh triangleMesh, int listNumber) {
		this.triangleMesh = triangleMesh;
		this.listNumber = listNumber;
		listExists = false;
	}

	private void drawMesh(GL2 gl) {
		//draw Triangles!
		gl.glBegin(GL2.GL_TRIANGLES);
		for (int i = 0; i < triangleMesh.getNumberOfTriangles(); i++) {

			// get Triangle
			TriangleFacet currentFacet = triangleMesh.getFacet(i);

			// get first HalfEdge
			HalfEdge currentHalfEdge = currentFacet.getHalfEdge();

			for (int j = 0; j < 3; j++) {
				// get Vertex!
				Vertex currentVertex = currentHalfEdge.getStartVertex();

				// get positions!
				Vector3 positionVertex = currentVertex.getPosition();
				Vector3 positionNormal = currentVertex.getNormal();

				// draw it!
				gl.glNormal3d(positionNormal.get(0), positionNormal.get(1), positionNormal.get(2));
				gl.glVertex3d(positionVertex.get(0), positionVertex.get(1), positionVertex.get(2));

				// get next HalfEdge!
				currentHalfEdge = currentHalfEdge.getNext();
			}
		}
		// finished Triangles!
		gl.glEnd();
		listExists = true;
	}

	@Override
	public void drawGl(GL2 gl) {
		if (!listExists) {
			gl.glNewList(listNumber, GL2.GL_COMPILE);
			drawMesh(gl);
			gl.glEndList();
		}
		gl.glCallList(listNumber);
	}

}
