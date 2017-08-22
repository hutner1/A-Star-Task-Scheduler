package scheduler.astar;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import io.DataReader;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;

public class TestAStarParallelised {

	//Data structures to use
	DefaultDirectedWeightedGraph _graph = new DefaultDirectedWeightedGraph();
	
	@Test
	public void testConstructor(){
		AStar alg = new AStarParallelised(_graph, 2, 3, null);
		assertEquals(alg._numberOfProcessors, 2);
		assertEquals(_graph.edgeSet().size(),0);
	}
	
	public void testOne(){
		DataReader dataReader = new DataReader(new File("test-examples/test1.dot"));
		while(dataReader.hasMoreGraphs()) {
		dataReader.readNextGraph();
		}
	}
}





