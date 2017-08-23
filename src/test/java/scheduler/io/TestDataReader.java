package scheduler.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.DataReader;
import io.InputParser;
import io.InputParserException;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;
import scheduler.graphstructures.Vertex;

/**
 * Tests to make sure that the TestDataReader class functions as expected 
 */
public class TestDataReader {

	File _file;
	DataReader _dataReader;

	@Before 
	public void initialise() 
	{
		_file = new File("testInput.dot"); 

		try {
			_file.createNewFile();
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_file,true)));
			writer.println("digraph \"testGraph\" {");
			writer.println("	A	 [Weight=10];");
			writer.println("		B	 [Weight=20];");
			writer.println("	C	 [Weight=30];");
			writer.println("	A -> B		 [Weight=10];");
			writer.println("	A -> C	 				[Weight=5];");
			writer.println("	B -> C	 [Weight=10];			");
			writer.println("}");

			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		_dataReader = new DataReader(_file);
		_dataReader.readNextGraph();

	}

	
	/**
	 * Test to check that the graph name is read/stored correctly
	 */
	@Test
	public void testGetGraphName() {

		assertEquals("testGraph",_dataReader.getGraphName());
	}

	
	/**
	 * Test classification of input (vertex/edge)
	 */
	@Test
	public void testGetVerticesAndEdgesRead() {

		ArrayList<String> verticesAndEdgesRead = _dataReader.getVerticesAndEdgesRead();
		assertEquals(verticesAndEdgesRead.get(0), "A");
		assertEquals(verticesAndEdgesRead.get(1), "B");
		assertEquals(verticesAndEdgesRead.get(2), "C");
		assertEquals(verticesAndEdgesRead.get(3), ">");
		assertEquals(verticesAndEdgesRead.get(4), ">");
		assertEquals(verticesAndEdgesRead.get(5), ">");
	}

	
	/**
	 * Test that vertices and edges from the file are read/stored correctly
	 */
	@Test
	public void testGetVerticesAndEdgesInfo() {

		ArrayList<String> verticesAndEdgesInfo = _dataReader.getVerticesAndEdgesInfo();
		assertEquals(verticesAndEdgesInfo.get(0), "	A	 [Weight=10];");
		assertEquals(verticesAndEdgesInfo.get(1), "	B	 [Weight=20];");
		assertEquals(verticesAndEdgesInfo.get(2), "	C	 [Weight=30];");
		assertEquals(verticesAndEdgesInfo.get(3), "	A -> B	 [Weight=10];");
		assertEquals(verticesAndEdgesInfo.get(4), "	A -> C	 [Weight=5];");
		assertEquals(verticesAndEdgesInfo.get(5), "	B -> C	 [Weight=10];");

	}

	
	/**
	 * Test the digraph constructed by the data reader from the input
	 */
	@Test
	public void testGetGraph() {

		DefaultDirectedWeightedGraph digraph = _dataReader.getGraph();
		ArrayList<Vertex> vertices = digraph.vertexSet();
		ArrayList<DefaultWeightedEdge> edges = digraph.edgeSet();



		Vertex A = vertices.get(0);
		Vertex B = vertices.get(1);
		Vertex C = vertices.get(2);
		DefaultWeightedEdge AB = edges.get(0);
		DefaultWeightedEdge AC = edges.get(1);
		DefaultWeightedEdge BC = edges.get(2);

		assertEquals(A.getName(),"A");
		assertEquals(A.getWeight(), 10);
		assertEquals(B.getName(),"B");
		assertEquals(B.getWeight(),20);
		assertEquals(C.getName(),"C");
		assertEquals(C.getWeight(),30);
		assertEquals(AB.getSource().getName(),"A");
		assertEquals(AB.getDest().getName(),"B");
		assertEquals(AB.getWeight(), 10);
		assertEquals(AC.getSource().getName(),"A");
		assertEquals(AC.getDest().getName(),"C");
		assertEquals(AC.getWeight(), 5);
		assertEquals(BC.getSource().getName(),"B");
		assertEquals(BC.getDest().getName(),"C");
		assertEquals(BC.getWeight(), 10);

	}

	
	/**
	 * Test the vertex mapping constructed by the data reader from the input
	 */
	@Test
	public void testGetMapping() {

		HashMap<String, Vertex> vertexMapping = _dataReader.getMapping();
		Vertex A = vertexMapping.get("A");
		Vertex B = vertexMapping.get("B");
		Vertex C = vertexMapping.get("C");

		assertEquals(A.getName(),"A");
		assertEquals(A.getWeight(), 10);
		assertEquals(B.getName(),"B");
		assertEquals(B.getWeight(),20);
		assertEquals(C.getName(),"C");
		assertEquals(C.getWeight(),30);

	}

	
	/**
	 * Test to check that the resetData() method resets all data in the reader
	 */
	@Test
	public void resetData() {
		
		ArrayList<String> verticesAndEdgesRead = _dataReader.getVerticesAndEdgesRead();
		ArrayList<String> verticesAndEdgesInfo = _dataReader.getVerticesAndEdgesInfo();
		DefaultDirectedWeightedGraph digraph = _dataReader.getGraph();
		HashMap<String, Vertex> vertexMapping = _dataReader.getMapping();
		
		assertFalse(verticesAndEdgesRead.isEmpty());
		assertFalse(verticesAndEdgesInfo.isEmpty());
		assertFalse(digraph.vertexSet().isEmpty());
		assertFalse(digraph.edgeSet().isEmpty());
		assertFalse(vertexMapping.isEmpty());
		
		_dataReader.resetData();
		
		verticesAndEdgesRead = _dataReader.getVerticesAndEdgesRead();
		verticesAndEdgesInfo = _dataReader.getVerticesAndEdgesInfo();
		digraph = _dataReader.getGraph();
		vertexMapping = _dataReader.getMapping();
		
		assertTrue(verticesAndEdgesRead.isEmpty());
		assertTrue(verticesAndEdgesInfo.isEmpty());
		assertTrue(digraph.vertexSet().isEmpty());
		assertTrue(digraph.edgeSet().isEmpty());
		assertTrue(vertexMapping.isEmpty());
		
		initialise();
	}


	@After
	public void deinitialise() {
		_file.delete();
	}

}
