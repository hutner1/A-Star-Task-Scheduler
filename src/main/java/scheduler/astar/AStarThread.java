package scheduler.astar;

import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import scheduler.basicmilestone.Vertex;

/**
 * AStar thread class that will be added to allow solution search in parallel
 */
public class AStarThread extends AStar{
	int _threadNo; //Number identifier for thread
	protected PriorityQueue<Solution> _nonschedulable; //TODO: Link with AStar
	protected Set<Solution> _schedulable; //TODO: Link with AStar
	private Solution bestSol = null;
	
	public AStarThread(int i, DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph, PriorityQueue<Solution> nonschedulable, Set<Solution> schedulable, int numberOfProcessors) {
		super(graph, numberOfProcessors);
		this._threadNo = i;
		this._nonschedulable = nonschedulable;
		this._schedulable = schedulable;
	}
	
	public void run() {
		bestSol = super.execute();
	}
	
	public Solution execute() {
		return bestSol;
	}
}
