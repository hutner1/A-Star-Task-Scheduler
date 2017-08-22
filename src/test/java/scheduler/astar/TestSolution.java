package scheduler.astar;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.Vertex;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestSolution {
		
		//Data structures to use
		DefaultDirectedWeightedGraph _graph = new DefaultDirectedWeightedGraph();
		
		@Test
		public void testSingleNodeSolution(){
			Vertex v = new Vertex("test");
			v.setWeight(10);
			_graph.addVertex(v);
			AStar alg = new AStar(_graph, 1, null);
			Solution sol = alg.execute();
			assertEquals(10, sol.getLastFinishTime());
		}
}
