package scheduler.astar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;
import scheduler.graphstructures.Vertex;
import visualization.Visualizer;

import visualization.gantt.Gantt;

import visualization.gui.Gui;


/**
 * AStar creates optimal solution with A*
 */
public class AStar {
	protected DefaultDirectedWeightedGraph _graph;
	protected int _numberOfProcessors;
	protected PriorityQueue<Solution> _solutionSpace;
	protected Set<Solution> _closedSolutions;
	protected Visualizer _visualizer;
	protected Gantt _gantt;
	private int _counter=0;
	
	public AStar(DefaultDirectedWeightedGraph graph, int numberOfProcessors, Visualizer graphVisualizer, Gantt gantt) {

		_graph = graph;
		_numberOfProcessors = numberOfProcessors;
		_solutionSpace = new PriorityQueue<Solution>();
		_closedSolutions = new CopyOnWriteArraySet<Solution>(); //threadsafe set
		_visualizer = graphVisualizer;

		_gantt = gantt;

	}

	/**
	 * Execute A* algorithm
	 * @return
	 */
	public Solution execute() {

		//Create initial solution and add to priority queue
		//Pop solution and create children solutions for that, readd children to queue
		//Pop most efficient child and add create children, readd
		//Repeat until child is a complete graph, that is the optimal schedule
			HashMap<Vertex, Integer> btmLevel = new HashMap<Vertex, Integer>();
			List<Vertex> schedulable = new ArrayList<Vertex>(); // dependencies all met
			List<Vertex> nonschedulable = new ArrayList<Vertex>(); // dependencies not met
			// fill lists of schedulables
			
			// Upper bound of run time if all tasks are run in order
			int upperBound = 0;
			
			for (Vertex v : _graph.vertexSet()) {
				if (_graph.inDegreeOf(v) == 0) { //get source nodes
					schedulable.add(v);
				} else {
					nonschedulable.add(v);
				}
				upperBound += v.getWeight();
				btmLevel.put(v, getBottomLevel(v));
			}
			
			
			
			// Create empty solution and then commence the looping
			Solution emptySolution = new Solution(upperBound, _numberOfProcessors, _graph, new ArrayList<Vertex>(), schedulable, nonschedulable);
			emptySolution.setBtmLevels(btmLevel);
			_solutionSpace.add(emptySolution);
			
			
			// BEST priority solution
			Solution bestCurrentSolution = _solutionSpace.poll();
			
			// if not complete, consider the children in generating the solution and poll again
			while (!bestCurrentSolution.isCompleteSchedule()) {
				
				while (_closedSolutions.contains(bestCurrentSolution)) {
					bestCurrentSolution = _solutionSpace.poll();
				}
				
				for (Solution s : bestCurrentSolution.createChildren()) {
					if (!_solutionSpace.contains(s)) {
						_solutionSpace.add(s);
						if (s.maxCostFunction() == bestCurrentSolution.maxCostFunction()) {
							break;
						}
					}
				}
				_closedSolutions.add(bestCurrentSolution);
				bestCurrentSolution = _solutionSpace.poll();
				//TODO System.out.println(bestCurrentSolution.maxCostFunction());
				//TODO System.out.println("Solution space size : " + _solutionSpace.size());
				
				if (_gantt != null) {
  					if (_gantt.hasLaunched()) {
  						_gantt.updateSolution(bestCurrentSolution);
  					} else {
  						_gantt.setSolution(bestCurrentSolution);
  						_gantt.launch();
  					}
  				}
				
				if(_visualizer != null){  
			              if(_counter == 15){  
			                  _counter = 0;  
			                  _visualizer.UpdateGraph(bestCurrentSolution);  
			                  
			                } else {  
			                  _counter++;  
			                }  
			          
			              } 
	
			}
			
			if(_visualizer != null){
				_visualizer.UpdateGraph(bestCurrentSolution);
			}
			return bestCurrentSolution;


	}
	
	/**
	 * Get children vertices
	 * @param vertex
	 * @return children vertices
	 */
	private ArrayList<Vertex> getChildren(Vertex vertex){
		ArrayList<Vertex> children = new ArrayList<>();
		for(DefaultWeightedEdge e : _graph.outgoingEdgesOf(vertex)){
			children.add(_graph.getEdgeTarget(e));
		}
		return children;
	}
	
	/**
	 * Returns the bottom level of a vertex
	 * @param vertex
	 * @return
	 */
	private int getBottomLevel(Vertex vertex){
		ArrayList<Vertex> children = getChildren(vertex);
		int myWeight = vertex.getWeight();
		if(children.isEmpty()){
			return myWeight;
		} else {
			int btmLvl = 0;
			for (Vertex v : children){
				int lvl = getBottomLevel(v);
				if(lvl > btmLvl){
					btmLvl = lvl;
				}
			}
			return myWeight + btmLvl;
		}
	}
	

}
