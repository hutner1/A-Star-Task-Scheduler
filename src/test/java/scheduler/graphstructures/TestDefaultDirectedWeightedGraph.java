package scheduler.graphstructures;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests to make sure that the DefaultDirectedWeightedGraph class functions as expected 
 */
public class TestDefaultDirectedWeightedGraph {
	private DefaultDirectedWeightedGraph _testGraph;
	// root vertices
	private final Vertex _root1 = new Vertex("root1");
	private final Vertex _root2 = new Vertex("root2");
	// intermediate vertices
	private final Vertex _intermediate = new Vertex("inter1");
	// leaf vertices
	private final Vertex _leaf1 = new Vertex("leaf1");
	private final Vertex _leaf2 = new Vertex("leaf2");

	// edges
	DefaultWeightedEdge _e1;
	DefaultWeightedEdge _e2;
	DefaultWeightedEdge _e3;
	DefaultWeightedEdge _e4;
	
	// sets for checking againsts
	private ArrayList<Vertex> _testVertexSet = new ArrayList<>();
	private ArrayList<Vertex> _testRootVertexSet = new ArrayList<>();
	private ArrayList<DefaultWeightedEdge> _testEdgeSet = new ArrayList<>();
	
	
	@Before
	public void initialiseDefaultDirectedWeightedGraph() {
		_testGraph = new DefaultDirectedWeightedGraph();
		// add vertices
		_testGraph.addVertex(_root1);
		_testGraph.addVertex(_root2);
		_testGraph.addVertex(_intermediate);
		_testGraph.addVertex(_leaf1);
		_testGraph.addVertex(_leaf2);
		
		// populate a Vertex array list for comparison later on
		_testVertexSet.add(_root1);
		_testVertexSet.add(_root2);
		_testVertexSet.add(_intermediate);
		_testVertexSet.add(_leaf1);
		_testVertexSet.add(_leaf2);
		_testRootVertexSet.add(_root1);
		_testRootVertexSet.add(_root2);
		
		/*
		 *  This will ensure that the functions are tested on vertices with 
		 *  0,1,2 incoming or outgoing edges
		 */
		// 2 roots pointing to 1 vertex
		_e1 = _testGraph.addEdge(_root1, _intermediate, 1);
		_e2 = _testGraph.addEdge(_root2, _intermediate, 2);
		// the same vertex points to 2 leaves
		_e3 = _testGraph.addEdge(_intermediate, _leaf1, 6);
		_e4 = _testGraph.addEdge(_intermediate, _leaf2, 7);
		
		_testEdgeSet.add(_e1);
		_testEdgeSet.add(_e2);
		_testEdgeSet.add(_e3);
		_testEdgeSet.add(_e4);

	}
	
	/**
	 * Test that the returned sets are the same as the test sets
	 */
	@Test
	public void testReturnedSets(){
		assertTrue(_testGraph.vertexSet().containsAll(_testVertexSet));
		assertTrue(_testGraph.edgeSet().containsAll(_testEdgeSet));
		assertTrue(_testGraph.returnRootVertices().containsAll(_testRootVertexSet));
	}
	
	/**
	 * Test the root vertices' properties
	 */
	@Test
	public void testRootVertices(){
		assertEquals(_testGraph.inDegreeOf(_root1),0);
		assertEquals(_testGraph.incomingEdgesOf(_root1).size(),0);
		assertEquals(_testGraph.edgesOf(_root1).size(),1);
		assertTrue(_testGraph.edgesOf(_root1).contains(_e1));
		assertEquals(_testGraph.outgoingEdgesOf(_root1).size(), 1);
		assertTrue(_testGraph.outgoingEdgesOf(_root1).contains(_e1));
		assertTrue(_testGraph.getDirectChildren(_root1).contains(_intermediate));

		assertEquals(_testGraph.inDegreeOf(_root2),0);
		assertEquals(_testGraph.incomingEdgesOf(_root2).size(),0);
		assertEquals(_testGraph.edgesOf(_root2).size(),1);
		assertTrue(_testGraph.edgesOf(_root2).contains(_e2));
		assertEquals(_testGraph.outgoingEdgesOf(_root2).size(), 1);
		assertTrue(_testGraph.outgoingEdgesOf(_root2).contains(_e2));
		assertTrue(_testGraph.getDirectChildren(_root2).contains(_intermediate));
	}
	
	/**
	 * Test the leaf vertices' properties
	 */
	@Test
	public void testLeafVertices(){
		assertEquals(_testGraph.inDegreeOf(_leaf1),1);
		assertEquals(_testGraph.incomingEdgesOf(_leaf1).size(),1);
		assertTrue(_testGraph.incomingEdgesOf(_leaf1).contains(_e3));
		assertEquals(_testGraph.edgesOf(_leaf1).size(),1);
		assertTrue(_testGraph.edgesOf(_leaf1).contains(_e3));
		assertEquals(_testGraph.outgoingEdgesOf(_leaf1).size(), 0);
		assertTrue(_testGraph.getDirectParents(_leaf1).contains(_intermediate));


		assertEquals(_testGraph.inDegreeOf(_leaf2),1);
		assertEquals(_testGraph.incomingEdgesOf(_leaf2).size(),1);
		assertTrue(_testGraph.incomingEdgesOf(_leaf2).contains(_e4));
		assertEquals(_testGraph.edgesOf(_leaf2).size(),1);
		assertTrue(_testGraph.edgesOf(_leaf2).contains(_e4));
		assertEquals(_testGraph.outgoingEdgesOf(_leaf2).size(), 0);
		assertTrue(_testGraph.getDirectParents(_leaf2).contains(_intermediate));
	}	
	
	/**
	 * Test the intermediate vertices' properties
	 */
	@Test
	public void testIntermediateVertices(){
		assertEquals(_testGraph.inDegreeOf(_intermediate),2);
		assertEquals(_testGraph.incomingEdgesOf(_intermediate).size(),2);
		assertTrue(_testGraph.incomingEdgesOf(_intermediate).contains(_e1));
		assertTrue(_testGraph.incomingEdgesOf(_intermediate).contains(_e2));
		assertEquals(_testGraph.edgesOf(_intermediate).size(),4);
		assertTrue(_testGraph.edgesOf(_intermediate).contains(_e1));
		assertTrue(_testGraph.edgesOf(_intermediate).contains(_e2));
		assertTrue(_testGraph.edgesOf(_intermediate).contains(_e3));
		assertTrue(_testGraph.edgesOf(_intermediate).contains(_e4));
		assertEquals(_testGraph.outgoingEdgesOf(_intermediate).size(), 2);
		assertTrue(_testGraph.outgoingEdgesOf(_intermediate).contains(_e3));
		assertTrue(_testGraph.outgoingEdgesOf(_intermediate).contains(_e4));
		assertTrue(_testGraph.getDirectParents(_intermediate).contains(_root1));
		assertTrue(_testGraph.getDirectParents(_intermediate).contains(_root2));
		assertTrue(_testGraph.getDirectChildren(_intermediate).contains(_leaf1));
		assertTrue(_testGraph.getDirectChildren(_intermediate).contains(_leaf2));
	}

	/**
	 * Test the edges' properties
	 */
	@Test
	public void testEdgesCreated(){
		// test edge properties
		assertEquals(_testGraph.getEdgeSource(_e1),_root1);
		assertEquals(_testGraph.getEdgeTarget(_e1),_intermediate);
		assertEquals(_testGraph.getEdgeWeight(_e1),1);
		
		assertEquals(_testGraph.getEdgeSource(_e2),_root2);
		assertEquals(_testGraph.getEdgeTarget(_e2),_intermediate);
		assertEquals(_testGraph.getEdgeWeight(_e2),2);
		
		assertEquals(_testGraph.getEdgeSource(_e3),_intermediate);
		assertEquals(_testGraph.getEdgeTarget(_e3),_leaf1);
		assertEquals(_testGraph.getEdgeWeight(_e3),6);
		
		assertEquals(_testGraph.getEdgeSource(_e4),_intermediate);
		assertEquals(_testGraph.getEdgeTarget(_e4),_leaf2);
		assertEquals(_testGraph.getEdgeWeight(_e4),7);
	}

}
