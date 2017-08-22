package scheduler.astar;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.Vertex;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import io.DataReader;

public class TestSolution {
		
		//Data structures to use
		DefaultDirectedWeightedGraph _graph = new DefaultDirectedWeightedGraph();
/*		
		@Test
		public void testSingleNodeSolution(){
			Vertex v = new Vertex("test");
			v.setWeight(10);
			_graph.addVertex(v);
			AStar alg = new AStar(_graph, 1, null,null);
			Solution sol = alg.execute();
			assertEquals(10, sol.getLastFinishTime());
		}
		
		@Test
		public void testOne(){
			DataReader dataReader = new DataReader(new File("test-examples/test1.dot"));
			while(dataReader.hasMoreGraphs()) {
			dataReader.readNextGraph();
			AStar alg = new AStar(dataReader.getGraph(), 1, null,null);
			Solution sol = alg.execute();
			assertEquals(640, sol.getLastFinishTime());
			}
		}*/
}
