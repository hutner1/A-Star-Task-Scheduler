package scheduler.astar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.junit.Test;

import io.DataReader;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.Vertex;

/**
 * Tests to make sure that the Solution class functions as expected 
 */
public class TestSolution {

	//Data structures to use
	DefaultDirectedWeightedGraph _graph = new DefaultDirectedWeightedGraph();

	/**
	 * Test for getting the A* solution for single node graph
	 */
	@Test
	public void testSingleNodeSolution(){
		Vertex v = new Vertex("test");
		v.setWeight(10);
		_graph.addVertex(v);
		AStar alg = new AStar(_graph, 1, null,null,null);
		Solution sol = alg.execute();
		assertEquals(sol.getLastFinishTime(),10);
	}

	/**
	 * Test for getting the A* solution for a fork-join graph
	 */
	@Test
	public void testOne(){
		DataReader dataReader = new DataReader(new File("test-examples/test1.dot"));
		while(dataReader.hasMoreGraphs()) {
			dataReader.readNextGraph();
			AStar alg = new AStar(dataReader.getGraph(), 1, null,null,null);
			Solution sol = alg.execute();
			assertEquals(sol.getLastFinishTime(),640);
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
			AStar alg = new AStar(dataReader.getGraph(), 2, null,null,null);
			Solution sol = alg.execute();
			assertEquals(sol.getLastFinishTime(),59);
		}
	}
	/**
	 * Test for getting the A* solution for a stencil graph
	 */
	@Test
	public void testStencil(){
		DataReader dataReader = new DataReader(new File("test-examples/200.dot"));
		while(dataReader.hasMoreGraphs()) {
			dataReader.readNextGraph();
			AStar alg = new AStar(dataReader.getGraph(), 2, null,null,null);
			Solution sol = alg.execute();
			assertEquals(sol.getLastFinishTime(),1380);
		}
	}
	/**
	 * Test for solution equality when adding vertices to a solution
	 */
	@Test
	public void testEqual(){
		
		Vertex v = new Vertex("ASD");
		v.setWeight(10);
		
		Vertex v2 = new Vertex("hello");
		v2.setWeight(15);
		
		_graph.addVertex(v);
		_graph.addVertex(v2);
		
		ArrayList<Vertex> scheduledProcesses = new ArrayList<Vertex>();
		ArrayList<Vertex> schedulableProcesses = new ArrayList<Vertex>();
		ArrayList<Vertex> nonschedulableProcesses = new ArrayList<Vertex>();
		
		schedulableProcesses.add(v);
		schedulableProcesses.add(v2);
		
		Solution solA = new Solution(200, 2, _graph, scheduledProcesses, schedulableProcesses, nonschedulableProcesses,false);
		Solution solB = new Solution(200, 2, _graph, scheduledProcesses, schedulableProcesses, nonschedulableProcesses,false);
		
		solA.addProcess(v, 1);
		solB.addProcess(v, 2);
		
		Set<Solution> set = new CopyOnWriteArraySet<Solution>();
		
		set.add(solA);		
		assertTrue(set.contains(solB));
		
		set.add(solB);
		assertTrue(set.size() == 1);
		
		assertTrue(solA.equals(solB));
		
		solB.addProcess(v2, 2);
		solA.addProcess(v, 2);
		
		assertFalse(solA.equals(solB));
	}

}
