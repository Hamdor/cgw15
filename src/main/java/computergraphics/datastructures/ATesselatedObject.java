package computergraphics.datastructures;

import java.util.ArrayList;
import java.util.List;

import computergraphics.math.LookupTable;
import computergraphics.math.Vector3;

public abstract class ATesselatedObject {

	private final double 	TAU = 0.0; // Tau for surfrace calculation
	private final int 	NUM_COLUMNS = 15; // Columns in lookup table
	private final int 	NUM_TRIANGLES = 5; // Triangles in one column
	private final int 	CORNERS = 8; // A cube has 8 corners :)
	private final int 	RESOLUTION = 200; // Maximum cubes for our range
	private final double 	VOLUME_MAX = 2; // Volume max
	private final double 	VOLUME_MIN = -2; // Volume mix
	private final double 	DISTANCE = (VOLUME_MAX * (VOLUME_MIN < 0 ? VOLUME_MAX : VOLUME_MIN) / RESOLUTION);

	private final double[][] CornerPositions = { 
			{ 0, 0, 0 }, // Corner 1
			{ DISTANCE, 0, 0 }, // Corner 2
			{ DISTANCE, DISTANCE, 0 }, // Corner 3
			{ 0, DISTANCE, 0 }, // Corner 4
			{ 0, 0, DISTANCE }, // Corner 5
			{ DISTANCE, 0, DISTANCE }, // Corner 6
			{ DISTANCE, DISTANCE, DISTANCE }, // Corner 7
			{ 0, DISTANCE, DISTANCE } // Corner 8
	};
	
	private HalfEdgeTriangleMesh mesh;

	/**
	 * Constructor for the Tessellation base.
	 */
	public ATesselatedObject() {

		mesh = new HalfEdgeTriangleMesh();
	}

	/**
	 * Fill the mesh with triangles.
	 */
	protected void fillMesh() {
		// Loop over our cubes
	    for (double x = VOLUME_MIN; x < VOLUME_MAX; x += DISTANCE) {
	      for (double y = VOLUME_MIN; y < VOLUME_MAX; y += DISTANCE) {
	        for (double z = VOLUME_MIN; z < VOLUME_MAX; z += DISTANCE) {
	          ArrayList<Vector3> vertices = new ArrayList<Vector3>();
	          ArrayList<Double>  values = new ArrayList<Double>();
	          // Loop over every corner of a cube
	          for (int i = 0; i < CORNERS; ++i) {
	            // Get relative corner position
	            vertices.add(new Vector3(
	                x + CornerPositions[i][0],
	                y + CornerPositions[i][1],
	                z + CornerPositions[i][2])
	            );
	            // Calculate the implizit function and add its values
	            values.add(getFunctionValue(vertices.get(i).get(0), vertices.get(i).get(1), vertices.get(i).get(2)));
	          }
	          // Create the acutaly triangle from the value
	          createTriangles(vertices, values);
	        }
	      }
	    }
	    //calculate missing mesh data.
		//mesh.update();
	}

	/**
	 * Create the Triangle according to marching Scuares algorithm.
	 * @param points	The corner points of the cube.
	 * @param values	The function value at each corner point.
	 */
	private void createTriangles(List<Vector3> points, List<Double> values) {
		// Calculate the index to access thr lookup table
		int caseIndex = 0;
		for (int i = 0; i < values.size(); ++i) {
			caseIndex += (values.get(i) > TAU) ? (0x01 << i) : 0;
		}
		int offset = caseIndex * NUM_COLUMNS;
		for (int i = 0; i < NUM_TRIANGLES; i++) {
			// Add triangle if lookup table give a valid value
			if (LookupTable.CUBE_FACES[offset] != -1) {
				// Calculate the triangle points (a,b,c)
				int a = mesh.addVertex(calcVertex(LookupTable.CUBE_FACES[offset + 0], points, values));
				int b = mesh.addVertex(calcVertex(LookupTable.CUBE_FACES[offset + 1], points, values));
				int c = mesh.addVertex(calcVertex(LookupTable.CUBE_FACES[offset + 2], points, values));
				// Add complete triangle to the triangle mesh
				mesh.addTriangle(a, b, c);
				// Adjust offset to match next entrie in table
				offset += 3;
			}
		}
	}

	/**
	 * Interpolate a vertex of a triangle for an edge of the cube.
	 * @param edgeIndex	the index for the edge of the cube.
	 * @param points	The list of all points in cube.
	 * @param values	The list of all values belonging to each point.
	 * @return			The interpolated vertex.
	 */
	private Vertex calcVertex(int edgeIndex, List<Vector3> points, List<Double> values) {
		final int[] edgeEntry = LookupTable.CUBE_EDGES[edgeIndex];
		// Retrieve correspondent points / corners and their values
		final Vector3 vec1 = points.get(edgeEntry[0]);
		final Vector3 vec2 = points.get(edgeEntry[1]);
		final double val1 = values.get(edgeEntry[0]);
		final double val2 = values.get(edgeEntry[1]);
		// Create linear interpolated vertex
		return new Vertex(interpolateLinear(vec1, vec2, val1, val2));
	}

	/**
	 * Linear Interpolation for the given vectors according to values.
	 * @param vec1	The position of the first vector.
	 * @param vec2	The position of the second vector.
	 * @param val1	The value of the first postion.
	 * @param val2	The value of the second position.
	 * @return 		The new position of the Vector after interpolation.
	 */
	private Vector3 interpolateLinear(Vector3 vec1, Vector3 vec2, double val1, double val2) {
		final double alpha = (TAU - val1) / (val2 - val1); // Calculate alpha
		// Now we can calculuate x, y, z
		final double pointX = ((1.0 - alpha) * vec1.get(0)) + (alpha * vec2.get(0));
		final double pointY = ((1.0 - alpha) * vec1.get(1)) + (alpha * vec2.get(1));
		final double pointZ = ((1.0 - alpha) * vec1.get(2)) + (alpha * vec2.get(2));
		return new Vector3(pointX, pointY, pointZ);
	}

	/**
	 * Calculates the function value for the Tesselated Object at the given position. 
	 * @param x		X-position.
	 * @param y		Y-position.
	 * @param z		Z-position.
	 * @return		The calculated function value.
	 */
	protected abstract double getFunctionValue(double x, double y, double z);

	public HalfEdgeTriangleMesh getMesh() {
		return mesh;
	}
}
