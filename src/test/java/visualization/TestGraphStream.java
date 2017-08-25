package visualization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.junit.Before;
import org.junit.Test;

import io.OutputWriter;
import scheduler.astar.AStar;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.Vertex;
import visualization.graph.Visualizer;

public class TestGraphStream {
	
	private DefaultDirectedWeightedGraph _graph;
	private Visualizer _graphStream;
	
	@Before
	public void initialise() 
	{
		_graph = new DefaultDirectedWeightedGraph();
		Vertex A = new Vertex("A");
		Vertex B = new Vertex("B");
		Vertex C = new Vertex("C");
		A.setWeight(10);
		B.setWeight(20);
		C.setWeight(30);
		_graph.addVertex(A);
		_graph.addVertex(B);
		_graph.addVertex(C);
		_graph.addEdge(A, B, 10);
		_graph.addEdge(A, C, 5);
		_graph.addEdge(B, C, 10);
		_graphStream = new Visualizer();
		
	}
	
	@Test
	public void testAdd() {

		_graphStream.add(_graph);
		assertFalse(_graphStream.getGraph().equals(null));
		assertFalse(_graphStream.getDAG().equals(null));
		
		Graph graph = _graphStream.getGraph();
		
		assertEquals(graph.getNode("A").getId(), "A");
		assertEquals(graph.getNode("B").getId(), "B");
		assertEquals(graph.getNode("C").getId(), "C");
		assertEquals(graph.getEdge("AB").getId(), "AB");
		assertEquals(graph.getEdge("AC").getId(), "AC");
		assertEquals(graph.getEdge("BC").getId(), "BC");

	}
	
	@Test
	public void testGetColor() {
		
		assertEquals(_graphStream.getColor(1), "e74c3c");
	}
	

}
