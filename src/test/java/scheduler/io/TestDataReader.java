package scheduler.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.DataReader;
import io.InputParser;
import io.InputParserException;

/**
 * I am curerntly working it, don't touch it please - David
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
			writer.println("	B	 [Weight=20];");
			writer.println("	C	 [Weight=30];");
			writer.println("	A -> B	 [Weight=10];");
			writer.println("	A -> C	 [Weight=5];");
			writer.println("	B -> C	 [Weight=10];");
			writer.println("}");

			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		_dataReader = new DataReader(_file);

	}

	@Test
	public void testGetGraphName() {


	}

	@Test
	public void testGetVerticesAndEdgesRead() {


	}

	@Test
	public void testGetVerticesAndEdgesInfo() {


	}

	@Test
	public void testGetGraph() {


	}

	@Test
	public void testGetMapping() {


	}

	@After
	public void deinitialise() {
		_file.delete();
	}

}
