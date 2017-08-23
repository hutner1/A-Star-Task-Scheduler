package scheduler.astar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.PriorityBlockingQueue;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;
import scheduler.graphstructures.Vertex;
import visualization.Visualizer;

import visualization.gantt.Gantt;

import visualization.gui.Gui;
import visualization.gui.StatisticTable;


/**
 * AStar creates optimal solution with A*
 */
public class AStar {
	protected DefaultDirectedWeightedGraph _graph;
	protected int _numberOfProcessors;

	/**
	 * OPEN solutions
	 */
	protected PriorityBlockingQueue<Solution> _solutionSpace;

	/**
	 * CLOSED solutions
	 */
	protected Set<Solution> _closedSolutions;

	protected Visualizer _visualizer;

	protected StatisticTable _stats;

	//Upper bound obtained from list scheduling
	protected int _upperBound;
	protected Gantt _gantt;
	protected static int _counter=0;
	protected static int _solCreated=0;
	protected static int _solPopped=0;
	protected static int _solPruned=0;

	/**
	 * AStar's constructor
	 * @param graph task digraph
	 * @param numberOfProcessors number of processors to do task scheduling on
	 * @param graphVisualizer the visualizer
	 */
	public AStar(DefaultDirectedWeightedGraph graph, int numberOfProcessors, Visualizer graphVisualizer, Gantt gantt, StatisticTable stats) {
		_graph = graph;
		_numberOfProcessors = numberOfProcessors;
		_solutionSpace = new PriorityBlockingQueue<Solution>(); //data structure does not permit null elements
		_closedSolutions = new CopyOnWriteArraySet<Solution>(); //threadsafe set
		_visualizer = graphVisualizer;
		_gantt = gantt;
		_stats = stats;
	}


	/**
	 * Execute A* algorithm
	 * @return optimal solution found sequentially
	 */
	public Solution execute() {
		initialiseSolutionSpace();
		return findOptimalSolution();
	}

	/**
	 * Inintialise the solution space and bottom level information
	 */
	protected void initialiseSolutionSpace(){
		//Create initial solution and add to priority queue
		//Pop solution and create children solutions for that, read children to queue
		//Pop most efficient child and add create children, read
		//Repeat until child is a complete graph, that is the optimal schedule
		HashMap<Vertex, Integer> btmLevel = new HashMap<Vertex, Integer>();
		List<Vertex> schedulable = new ArrayList<Vertex>(); // dependencies all met
		List<Vertex> nonschedulable = new ArrayList<Vertex>(); // dependencies not met
		// fill lists of schedulables

		for (Vertex v : _graph.vertexSet()) {
			if (_graph.inDegreeOf(v) == 0) { //get source nodes
				schedulable.add(v);
			} else {
				nonschedulable.add(v);
			}
			btmLevel.put(v, getBottomLevel(v));
		}

		ListScheduler listScheduler = new ListScheduler(_graph, _numberOfProcessors);
		_upperBound = listScheduler.getResult();
		System.out.println("Upper bound:" + _upperBound);

		// Create empty solution and then commence the looping
		Solution emptySolution = new Solution(_upperBound, _numberOfProcessors, _graph, new ArrayList<Vertex>(), schedulable, nonschedulable);
		emptySolution.setBtmLevels(btmLevel);
		_solutionSpace.add(emptySolution);	
	}


	/**
	 * Finds the optimal solution using A* algorithm
	 * @return optimal solution
	 */
	protected Solution findOptimalSolution(){
		// BEST priority solution
		Solution bestCurrentSolution = _solutionSpace.poll();
		_solPopped ++;

		// For PARALLELISATION, just in case that at the start, the first thread 
		// did not populate the solution space fast enough for the subsequent threads
		// TODO what if solution space too small like 2 tasks - YaoJian will understand
		while(bestCurrentSolution == null){
			bestCurrentSolution = _solutionSpace.poll();
			/*_solPopped ++;*/
		}

		// if not complete, consider the children in generating the solution and poll again
		while (!bestCurrentSolution.isCompleteSchedule()) {
			//System.out.println("C: "+_closedSolutions.size());

			while ((_closedSolutions.contains(bestCurrentSolution)) || (bestCurrentSolution == null)) {
				bestCurrentSolution = _solutionSpace.poll();
				if (bestCurrentSolution != null) {
					_solPopped ++;
					_solPruned ++;
				}

			}

			//System.out.println("SS: "+_solutionSpace.size());

			Queue<Solution> childSolutions = bestCurrentSolution.createChildren();

			Solution s;
			boolean fullyExpanded = true;

			while ((s = childSolutions.poll()) != null) {
				int childCost = s.maxCostFunction();
				_solCreated ++;
				System.out.println(childCost);
				if (!_solutionSpace.contains(s) && !_closedSolutions.contains(s)) {
					if (childCost > _upperBound){
						// DO NOTHING AS IT WILL NOT BE CONSIDERED
						_solPruned ++;
					} else {
						_solutionSpace.add(s); // TODO move to after if statement?
						if (childCost == bestCurrentSolution.maxCostFunction()) {
							if (!childSolutions.isEmpty()) {
								fullyExpanded = false;
								bestCurrentSolution.setExpansionStatus(true);
								_solutionSpace.add(bestCurrentSolution);
								break;
							} else {
								bestCurrentSolution.setExpansionStatus(false);
							}
						}
					}
				}

			}
			if(fullyExpanded){
				_closedSolutions.add(bestCurrentSolution);
			}

			while(bestCurrentSolution == null){
				bestCurrentSolution = _solutionSpace.poll();
			}

			//TODO System.out.println(bestCurrentSolution.maxCostFunction());
			//TODO System.out.println("Solution space size : " + _solutionSpace.size());

			if (_gantt != null) {
				if (_gantt.hasLaunched()) {
					if(_counter == 10){  
						_gantt.updateSolution(bestCurrentSolution);
					}
				} else {
					_gantt.setSolution(bestCurrentSolution);
				}
			}

			if(_visualizer != null){  
				if(_counter == 10){  
					_counter = 0;  
					_visualizer.UpdateGraph(bestCurrentSolution);  

				} else {  
					_counter++;  
				}  

			} 

			if(_stats != null){  
				if(_counter == 10){  
					_stats.updateStats(_solCreated, _solPopped, _solPruned, -1, bestCurrentSolution.maxCostFunction());
				}
			} 

		}
		if(_visualizer != null){
			_visualizer.UpdateGraph(bestCurrentSolution);
			_gantt.updateSolution(bestCurrentSolution);
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

	/**
	 * TODO
	 * @return
	 */
	public int getSolCreated(){
		return _solCreated;
	}

	/**
	 * TODO
	 * @return
	 */	
	public int getSolPruned(){
		return _solPruned;
	}

	/**
	 * TODO
	 * @return
	 */	
	public int getSolPopped(){
		return _solPopped;
	}

}
