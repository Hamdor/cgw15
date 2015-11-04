package computergraphics.datastructures;

import java.util.ArrayList;
import java.util.List;

import computergraphics.math.Vector3;

public class HalfEdgeTriangleMesh implements ITriangleMesh {

	/**
	 * Contains a List of all half edges in this triangle mesh.
	 */
	private List<HalfEdge> halfEdges;

	/**
	 * Contains a List of all triangle facets in this triangle mesh.
	 */
	private List<TriangleFacet> triangleFacets;

	/**
	 * Contains a List of all vertices in this triangle mesh.
	 */
	private List<Vertex> vertices;

	/**
	 * Filename of the belonging texture
	 */
	private String textureFilename = "";

	public HalfEdgeTriangleMesh() {

		// initialise Lists.
		halfEdges = new ArrayList<HalfEdge>();
		triangleFacets = new ArrayList<TriangleFacet>();
		vertices = new ArrayList<Vertex>();

	}

	@Override
	public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
		// create Objects
		TriangleFacet facet = new TriangleFacet();
		HalfEdge edge1 = new HalfEdge();
		HalfEdge edge2 = new HalfEdge();
		HalfEdge edge3 = new HalfEdge();

		// set all properties
		edge1.setFacet(facet);
		edge1.setNext(edge2);
		edge1.setStartVertex(vertices.get(vertexIndex1));
		vertices.get(vertexIndex1).setHalfEgde(edge1);

		edge2.setFacet(facet);
		edge2.setNext(edge3);
		edge2.setStartVertex(vertices.get(vertexIndex2));
		vertices.get(vertexIndex2).setHalfEgde(edge2);

		edge3.setFacet(facet);
		edge3.setNext(edge1);
		edge3.setStartVertex(vertices.get(vertexIndex3));
		vertices.get(vertexIndex3).setHalfEgde(edge3);

		facet.setHalfEdge(edge1);
		facet.setNormal(computeFacetNormal(facet));

		computeOppositeHalfEdges();

	}

	@Override
	public int addVertex(Vertex v) {
		if (!vertices.contains(v)) {
			if (vertices.add(v)) {
				return vertices.indexOf(v);
			}
		}
		return -1;
	}

	@Override
	public int getNumberOfTriangles() {
		return triangleFacets.size();
	}

	@Override
	public int getNumberOfVertices() {
		return vertices.size();
	}

	@Override
	public Vertex getVertex(int index) {
		return vertices.get(index);
	}

	@Override
	public TriangleFacet getFacet(int facetIndex) {
		return triangleFacets.get(facetIndex);
	}

	@Override
	public void clear() {

		halfEdges.clear();
		triangleFacets.clear();
		vertices.clear();
	}

	@Override
	public void computeTriangleNormals() {

		for (TriangleFacet facet : triangleFacets) {
			facet.setNormal(computeFacetNormal(facet));
		}
	}

	/**
	 * Calculate the normal for the facet.
	 * 
	 * @param facet
	 *            the facet the normal should be calculated for.
	 * @return the calculated normal.
	 */
	private Vector3 computeFacetNormal(TriangleFacet facet) {

		Vector3 p0 = facet.getHalfEdge().getStartVertex().getPosition();
		Vector3 p1 = facet.getHalfEdge().getNext().getStartVertex().getPosition();
		Vector3 p2 = facet.getHalfEdge().getNext().getNext().getStartVertex().getPosition();

		return new Vector3(p0.cross(p1).add(p1.cross(p2)).add(p2.cross(p0)));
	}

	public void computeOppositeHalfEdges() {

		for (HalfEdge halfEdge : halfEdges) {
			halfEdge.setOpposite(computeOppositeHalfEdge(halfEdge));
		}
	}

	/**
	 * computes opposite halfEdge for the given half edge.
	 * 
	 * @param halfEdge
	 *            the halfEdge to compute the opposite for.
	 * @return the opposite HalfEdge to the given one.
	 */
	private HalfEdge computeOppositeHalfEdge(HalfEdge halfEdge) {

		Vertex destination = halfEdge.getNext().getStartVertex();

		for (HalfEdge opposite : halfEdges) {
			if (opposite.getStartVertex().equals(destination)) {
				return opposite;
			}
		}

		return null;
	}

	@Override
	public void setTextureFilename(String filename) {
		textureFilename = filename;
	}

	@Override
	public String getTextureFilename() {
		return textureFilename;
	}

}
