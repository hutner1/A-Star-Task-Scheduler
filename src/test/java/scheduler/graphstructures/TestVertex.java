package scheduler.graphstructures;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Basic tests to make sure that the Vertex class functions as expected 
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
	

}
