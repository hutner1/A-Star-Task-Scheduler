package scheduler.astar;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import io.DataReader;

/**
 * Tests to make sure that the ListScheduler class functions as expected 
 */
public class TestListScheduler {

	//Data structures to use
	DefaultDirectedWeightedGraph _graph = new DefaultDirectedWeightedGraph();

	/**
	 * Test for getting the topological sort result for a fork-join graph
	 */
	@Test
	public void testOne(){
		DataReader dataReader = new DataReader(new File("test-examples/test1.dot"));
		while(dataReader.hasMoreGraphs()) {
			dataReader.readNextGraph();
			ListScheduler ls = new ListScheduler(dataReader.getGraph(),1);
			assertEquals(640,ls.getResult());
		}
	}

}
