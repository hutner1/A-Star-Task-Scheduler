package scheduler.io;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import io.OutputWriter;
import scheduler.astar.Solution;
import scheduler.graphstructures.Vertex;


/**
 * Tests to make sure that the OutputWriter class functions as expected 
 * I am currently working it, don't touch it please - David
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
	
	
	@Before
	public void initialise() 
	{
		_outputWriter =  new OutputWriter(_outputFilename);
		_testFile2 = new File("test2");
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
		
	}
	
	@After
	public void deinitialise() {
		
		File file = new File(_outputFilename);
		file.delete();
		_testFile2.delete();
	}
	
	
	
	
	
}
