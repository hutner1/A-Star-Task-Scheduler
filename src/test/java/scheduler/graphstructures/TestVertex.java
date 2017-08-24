package scheduler.graphstructures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests to make sure that the Vertex class functions as expected 
 */
public class TestVertex {
	private final int _weight = 100;
	private final String _name = "test";
	private final int _bottomLvl = 50;
	private Vertex _testVertex;
	
	@Before
	public void initialiseVertex(){
		_testVertex = new Vertex(_name);
		_testVertex.setWeight(_weight);
		_testVertex.setBottomLevel(_bottomLvl);
	}
	

	@Test
	public void testGetters(){
		assertEquals(_testVertex.getName(),_name);	
		assertEquals(_testVertex.getWeight(),_weight);
		assertEquals(_testVertex.getBottomLevel(),_bottomLvl);
	}

	@Test
	public void testVisited() {
		// needs to be false initially
		assertFalse(_testVertex.isVisited());
		
		_testVertex.setVisited();
		
		assertTrue(_testVertex.isVisited());
	}
	
	@Test
	public void testComparator() {
		Vertex vertexA = new Vertex("0");
		Vertex vertexB = new Vertex("1");
		Vertex vertexC = new Vertex("2");
		
		List<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(vertexC);
		vertices.add(vertexA);
		vertices.add(vertexB);
		
		Collections.sort(vertices);
		
		assertTrue(vertices.indexOf(vertexA) == 0);
		assertTrue(vertices.indexOf(vertexB) == 1);
		assertTrue(vertices.indexOf(vertexC) == 2);
	}
}
