package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.datastructures.HalfEdge;
import computergraphics.datastructures.ITriangleMesh;
import computergraphics.datastructures.TriangleFacet;
import computergraphics.datastructures.Vertex;
import computergraphics.math.Vector3;

public class TriangleMeshNode extends Node{

	private final ITriangleMesh triangleMesh;
	
	public TriangleMeshNode(ITriangleMesh triangleMesh) {
		this.triangleMesh = triangleMesh;
	}
	
	@Override
	public void drawGl(GL2 gl) {
		// TODO Auto-generated method stub
		
		for (int i = 0; i < triangleMesh.getNumberOfTriangles(); i++) {
			//draw Triangles!
			
			//get Triangle
			TriangleFacet currentFacet = triangleMesh.getFacet(i);
			//get first HalfEdge
			HalfEdge currentHalfEdge = currentFacet.getHalfEdge();
			
			for (int j = 0; j < 3; j++) {
				//get Vertex!
				Vertex currentVertex = currentHalfEdge.getStartVertex();

				//get positions!
				Vector3 positionVertex = currentVertex.getPosition();
				Vector3 positionNormal = currentVertex.getNormal();
				
				//draw it!
				gl.glNormal3d(positionNormal.get(0), positionNormal.get(1), positionNormal.get(2));
				gl.glVertex3d(positionVertex.get(0), positionVertex.get(1), positionVertex.get(2));
				
				//get next HalfEdge!
				currentHalfEdge = currentHalfEdge.getNext();
			}
		}
		
	}

}
