package scheduler.graphstructures;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestDefaultWeightedEdge {
	private DefaultWeightedEdge _testEdge;
	private final int _weight = 100;
	private final Vertex sourceVertex = new Vertex("source");
	private final Vertex destVertex = new Vertex("dest");
	
	@Before
	public void initialiseDefaultWeightedEdge(){
		_testEdge = new DefaultWeightedEdge(sourceVertex, destVertex, _weight);
	}
	
	@Test
	public void testGetters() {
		assertEquals(_testEdge.getSource(),sourceVertex);
		assertEquals(_testEdge.getDest(),destVertex);
		assertEquals(_testEdge.getWeight(), _weight);
	}
	
	@Test
	public void testContains(){
		assertTrue(_testEdge.contains(sourceVertex));
		assertTrue(_testEdge.contains(destVertex));
		assertFalse(_testEdge.contains(new Vertex("anotherVertex")));

	}

}
