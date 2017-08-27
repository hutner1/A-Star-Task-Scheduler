package scheduler.astar;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import io.DataReader;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;

/**
 * Tests to make sure that the AStarParallelised class functions as expected 
 */
public class TestAStarParallelised {

	//Data structures to use
	DefaultDirectedWeightedGraph _graph = new DefaultDirectedWeightedGraph();

	/**
	 * Test to check initialising the AStarParallelised class
	 */
	@Test
	public void testConstructor(){
		AStar alg = new AStarParallelised(_graph, 2, 3, null,null,null);
		assertEquals(alg._numberOfProcessors, 2);
		assertEquals(_graph.edgeSet().size(),0);
	}
	
	/**
	 * Test for getting the AStarParallelised optimal result for a fork-join graph
	 */
	@Test
	public void testOne(){
		DataReader dataReader = new DataReader(new File("test-examples/test1.dot"));
		while(dataReader.hasMoreGraphs()) {
			dataReader.readNextGraph();
			AStar as = new AStarParallelised(dataReader.getGraph(), 2, 2,null,null,null);
			Solution sol = as.execute();
			assertEquals(sol.getLastFinishTime(),360);
		}
	}
	
	/**
	 * Test for getting the A* solution for a difficult fork-join graph (demonstrating fixed order pruning)
	 */
	@Test
	public void testTwo(){
		DataReader dataReader = new DataReader(new File("test-examples/2.dot"));
		while(dataReader.hasMoreGraphs()) {
			dataReader.readNextGraph();
			AStar as = new AStarParallelised(dataReader.getGraph(), 2, 8,null,null,null);
			Solution sol = as.execute();
			assertEquals(sol.getLastFinishTime(),59);
		}
	}
	/**
	 * Test for getting the AStarParallelised optimal result solution for a stencil graph
	 */
	@Test
	public void testStencil(){
		DataReader dataReader = new DataReader(new File("test-examples/200.dot"));
		while(dataReader.hasMoreGraphs()) {
			dataReader.readNextGraph();
			AStar as = new AStarParallelised(dataReader.getGraph(), 2, 8,null,null,null);
			Solution sol = as.execute();
			assertEquals(sol.getLastFinishTime(),1380);
		}
	}
}





