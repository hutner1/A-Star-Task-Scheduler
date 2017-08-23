package scheduler.io;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import io.OutputWriter;
import scheduler.astar.AStar;
import scheduler.astar.Solution;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.Vertex;


/**
 * Tests to make sure that the OutputWriter class functions as expected 
 * 
 */
public class TestOutputWriter {

	String _outputFilename = "test";
	File _testFile2;
	private OutputWriter _outputWriter;
	private String _digraphName;
	private ArrayList<String> _verticesAndEdgesInfo;
	private ArrayList<String> _verticesAndEdgesRead;
	private Solution _solution;
	private HashMap<String, Vertex> _vertexMapping;
	
	/**
	 * Initialize the files and parameters that are needed for the tests
	 */
	@Before
	public void initialise() 
	{
		_outputWriter =  new OutputWriter(_outputFilename);
		_testFile2 = new File("test2.dot");
		try {
			_testFile2.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try{ 
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_testFile2,true)));
			writer.println("testtesttesttesttesttest");
			writer.println("testtesttesttest");
			writer.println("testtest");
			writer.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		_verticesAndEdgesInfo = new ArrayList<String>();
		_verticesAndEdgesRead = new ArrayList<String>();
		_vertexMapping = new HashMap<String, Vertex>();
		
		_digraphName = "testGraph";
		DefaultDirectedWeightedGraph _graph = new DefaultDirectedWeightedGraph();
		Vertex A = new Vertex("A");
		Vertex B = new Vertex("B");
		A.setWeight(10);
		B.setWeight(20);
		_graph.addVertex(A);
		_graph.addVertex(B);
		_graph.addEdge(A, B, 10);
		AStar alg = new AStar(_graph, 1, null,null,null);
		_solution = alg.execute();
		_verticesAndEdgesInfo.add("	A	 [Weight=10];");
		_verticesAndEdgesInfo.add("	B	 [Weight=20];");
		_verticesAndEdgesInfo.add(" 	A -> B	 [Weight=10];");
		_verticesAndEdgesRead.add("A");
		_verticesAndEdgesRead.add("B");
		_verticesAndEdgesRead.add(">");
		_vertexMapping.put("A", A);
		_vertexMapping.put("B", B);
	}
	
	/**
	 * Test for writing the solution to the output file
	 */
	@Test
	public void testCreateScheduleAStar() {
		_outputWriter.createScheduleAStar(_digraphName, _verticesAndEdgesInfo, _verticesAndEdgesRead, _solution, _vertexMapping);
		
		String firstLine = "";
		String secondLine = "";
		String thirdLine = "";
		String fourthLine = "";
		String fifthLine = "";
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(_outputFilename + ".dot"));
			firstLine = in.readLine();
			secondLine = in.readLine();
			thirdLine = in.readLine();
			fourthLine = in.readLine();
			fifthLine = in.readLine();
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertEquals(firstLine, "digraph \"testGraph\" {");
		assertEquals(secondLine, "	A	 [Weight=10, Start=0, Processor=1];");
		assertEquals(thirdLine, "	B	 [Weight=20, Start=10, Processor=1];");
		assertEquals(fourthLine, " 	A -> B	 [Weight=10];");
		assertEquals(fifthLine, "}");
		
	}
	
	/**
	 * Test to check the string is written to file correctly
	 */
	@Test
	public void testRecord() {
		
		File testFile3 = new File("testFile3.dot");
		String line = "";
		try {
			testFile3.createNewFile();
			_outputWriter.record(testFile3, "testFile3");
			BufferedReader in = new BufferedReader(new FileReader(testFile3));
			line = in.readLine();
			in.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertEquals(line,"testFile3");
		testFile3.delete();
	}
	
	/**
	 * Test to check the file is cleared correct
	 */
	@Test
	public void testClearFile() {
		
		assertTrue(_testFile2.length() > 0);
		_outputWriter.clearFile(_testFile2);
		assertTrue(_testFile2.exists());
		assertTrue(_testFile2.length() == 0);
	}
	
	
	@After
	public void deinitialise() {
		
		File file = new File(_outputFilename + ".dot");
		file.delete();
		_testFile2.delete();
	}
	
}
