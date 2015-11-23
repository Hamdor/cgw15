package computergraphics.datastructures;

import java.util.ArrayList;
import java.util.List;

import computergraphics.math.Vector3;

public class HalfEdgeTriangleMesh implements ITriangleMesh {

	private final boolean debug = false;

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

	private final double ALPHA = 0.3;

	/**
	 * Constructor
	 */
	public HalfEdgeTriangleMesh() {
		// initialise Lists.
		halfEdges = new ArrayList<HalfEdge>();
		triangleFacets = new ArrayList<TriangleFacet>();
		vertices = new ArrayList<Vertex>();
	}

	@Override
	public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
		if (debug) {
			System.out.println("add Triangle for vertices: " + vertexIndex1 + " (" + vertices.get(vertexIndex1) + ") "
					+ vertexIndex2 + " (" + vertices.get(vertexIndex2) + ") " + vertexIndex3 + " ("
					+ vertices.get(vertexIndex3) + ") ");
		}
		final int[] idx = { vertexIndex1, vertexIndex2, vertexIndex3 };
		addTriangle(idx);
	}

	private void addTriangle(final int[] startIds) {
		TriangleFacet facet = new TriangleFacet();
		HalfEdge[] edges = new HalfEdge[3];
		for (int i = 0; i < edges.length; ++i) {
			// Init all 3 half edges
			edges[i] = new HalfEdge();
		}
		for (int i = 0; i < edges.length; ++i) {
			edges[i].setFacet(facet);
			// Connect half edges
			edges[i].setNext(i < edges.length - 1 ? edges[i + 1] : edges[0]);
			Vertex vertex = vertices.get(startIds[i]);
			edges[i].setStartVertex(vertex);
			vertex.setHalfEgde(edges[i]);
			halfEdges.add(edges[i]); // Add to list
		}
		// set all properties for facet
		facet.setHalfEdge(edges[0]);
		facet.setNormal(computeFacetNormal(facet));
		// save facet
		triangleFacets.add(facet);
	}

	@Override
	public int addVertex(Vertex v) {
		if (debug) {
			System.out.println("add Vertex: " + v);
		}
		vertices.add(v);
		return vertices.size() - 1;
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
		if (debug) {
			System.out.println("-----clear all lists!-----");
		}
		halfEdges.clear();
		triangleFacets.clear();
		vertices.clear();
	}

	@Override
	public void computeTriangleNormals() {
		if (debug) {
			System.out.println("Compute Triangle Normals...");
		}
		for (TriangleFacet facet : triangleFacets) {
			facet.setNormal(computeFacetNormal(facet));
		}
	}

	public void computeVertexNormals() {
		for (HalfEdge first : halfEdges) {
			HalfEdge cur = first;
			Vector3 sum = cur.getFacet().getNormal();
			do {
				cur = cur.getOpposite().getNext();
				sum.add(cur.getFacet().getNormal());
			} while (cur != first);
			first.getStartVertex().setNormal(sum.getNormalized());
		}
	}

	/**
	 * Calculate the normal for a facet.
	 * 
	 * @param facet
	 *            the facet the normal should be calculated for.
	 * @return the calculated normal.
	 */
	private Vector3 computeFacetNormal(TriangleFacet facet) {
		Vector3 p0 = facet.getHalfEdge().getStartVertex().getPosition();
		Vector3 p1 = facet.getHalfEdge().getNext().getStartVertex().getPosition();
		Vector3 p2 = facet.getHalfEdge().getNext().getNext().getStartVertex().getPosition();
		Vector3 u = p1.subtract(p0);
		Vector3 v = p2.subtract(p0);

		return u.cross(v).getNormalized();
	}

	/**
	 * searches for all opposite HalfEdges
	 */
	public void computeOppositeHalfEdges() {
		if (debug) {
			System.out.println("Compute opposite half edges...");
		}
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
			if (!halfEdge.equals(opposite) && opposite.getStartVertex().equals(destination)) {
				if (opposite.getNext().getStartVertex().equals(halfEdge.getStartVertex())) {
					return opposite;
				}
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

	public int getNumberOfHalfEdges() {
		return halfEdges.size();
	}

	/**
	 * Apply LaplaceSmoothing to HalfEdgeTriangleMesh Vertices.
	 */
	public void laplaceSmoothing() {

		ArrayList<Vector3> mainEmphasis = new ArrayList<Vector3>();

		//calculate main emphasis for every vertex
		for (Vertex p : vertices) {
			mainEmphasis.add(calculateMainEmphasis(p));
		}
		
		//apply changes to every Vertex.
		for (int i = 0; i < vertices.size(); i++) {
			//calculate new position
			Vector3 position = vertices.get(i).getPosition().multiply(ALPHA); /* alpha * p_i ... */
			position = position.add(mainEmphasis.get(i).multiply(1-ALPHA)); /* ... + (1-alpha) * c_i */
			
			//set other values
			Vector3 normal = vertices.get(i).getNormal();
			Vector3 color = vertices.get(i).getColor();
			
			//create new Vertex.
			Vertex update = new Vertex(position, normal, color);
			vertices.set(i, update);
		}
		
		computeTriangleNormals();
		computeVertexNormals();
	}

	/**
	 * Calculates the main emphasis using all neighbours.
	 * @param p the vertex to calculate the main emphasis for
	 * @return the main emphasis
	 */
	private Vector3 calculateMainEmphasis(Vertex p) {
		ArrayList<Vertex> neighbours = getNeighbours(p);
		Vector3 sum = new Vector3();
		for (Vertex n : neighbours) {
			sum = sum.add(n.getPosition());
		}
		return sum.multiply((1.0/neighbours.size()));
	}

	/**
	 * Determine Neighbours for the given vertex.
	 * @param vertex the vertex to get the Neighbour for
	 * @return the determined neighbours.
	 */
	private ArrayList<Vertex> getNeighbours(Vertex vertex) {
		ArrayList<Vertex> neighbours = new ArrayList<Vertex>();
		HalfEdge first = vertex.getHalfEdge();
		HalfEdge next = first;
		
		do{
			neighbours.add(next.getOpposite().getStartVertex());
			next = next.getOpposite().getNext();
		}while (next != first);
		
		return neighbours;
	}

}
