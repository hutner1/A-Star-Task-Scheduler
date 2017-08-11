package scheduler.dfsbranchandbound;

import java.util.ArrayList;
import java.util.HashMap;


import scheduler.basicmilestone.Vertex;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;

/**
 * This class generates an optimal solution using Depth First Search Branch & Bound algorithm
 *
 */
public class SolutionGenerator {

	// DIGRAPH
	DefaultDirectedWeightedGraph _digraph;
	// NO. OF PROCESSORS
	int _noOfProcessors; 
	
	// For storage of CURRENT OPTIMAL SOLUTION
	/**
	 * Processors and tasks executed on it
	 * for e.g.
	 * 
	 * |= Processor No ==|
	 * |  0  |  1  |  2  |
	 * |==== Task No.====|
	 * |  a  |  b  |  d  |
	 * |     |  c  |  e  | 
	 * |=====|=====|=====|
	 * 
	 */
	HashMap<Integer, ArrayList<Vertex>> _processorList;
	
	// CURRENT VERTEX INFO, VALUE FORMAT := int[startTime, finishTime, processorNo]
	/** 
	 * for e.g.
	 * Task[a] -> [start: 0, finish: 3, processor no: 2]
	 */
	HashMap<Vertex,int[]> _vertexScheduleInfo;
	
	// For storage of *OPTIMAL SOLUTION* 
	HashMap<Integer, ArrayList<Vertex>> _optimalProcessorListSolution;
	HashMap<Vertex,int[]> _optimalVertexScheduleInfo;
	
	
	// CURRENT OPTIMAL COST
	int _optimalCost;
	
	// For storage of *BOTTOM LVL* information
	HashMap<Vertex,Integer> _btmLevel;
	
	/**
	 * Takes in digraph information to generate optimum solution for using DFS branch & bound
	 * @param digraph
	 * @param noOfProcessors
	 */
	public SolutionGenerator(DefaultDirectedWeightedGraph digraph, int noOfProcessors){
		_digraph = digraph;
		_noOfProcessors = noOfProcessors;
		
		// initialise data structures
		_processorList = new HashMap<>();
		_vertexScheduleInfo = new HashMap<>();
		_btmLevel = new HashMap<>(); 
		for(int i = 0; i<noOfProcessors; i++){
			_processorList.put(i, new ArrayList<>());
		}
		
		// starting optimal cost is max value
		_optimalCost = Integer.MAX_VALUE;	
	} 
	
	/**
	 * Does Depth First Search Branch & Bound and returns the optimal solution
	 * which contains info on each vertices
	 * @return optimal schedule
	 */
	public HashMap<Vertex,int[]> generateSolution(){
		ArrayList<Vertex> rootVertices =  returnRootVertices();
		
		// populate bottom level hash map
		for(Vertex v : _digraph.vertexSet()){
			_btmLevel.put(v, getBottomLevel(v));
			//System.out.println(v.getName()+"'s bottom level :"+_btmLevel.get(v)); //debug
		}
		
		// start the dfs b&b for different root vertices
		for(Vertex v : rootVertices){
			// create a shallow copy of all vertices
			ArrayList<Vertex> initialVertices = new ArrayList<>();
			for (Vertex v2 : _digraph.vertexSet()){
				initialVertices.add(v2);
			}
			// start dfs b&b for optimal solution
			dfs(v, initialVertices);
		}
		return _optimalVertexScheduleInfo;
	}
	
	/**
	 * Get the output string for each vertex, to be written to output file
	 * @param vertex
	 * @return output string, to be written to output file
	 */
	public String outputString(Vertex vertex) {
		return ", Start=" + getOptimalStartTime(vertex) + ", Processor=" + getOptimalProcessorNo(vertex);
	}
	
	// DEBUGGER
	private void debugger(Vertex vertex){
		System.out.println("==============\n");
		System.out.println("OPTIMAL: "+ _optimalCost);
		for(int i=0; i<_noOfProcessors; i++){
	 		ArrayList<Vertex> processorSchedule = _processorList.get(i);
	 		System.out.println("Processor "+i+":");
	 		for(Vertex v: processorSchedule){
		 		System.out.println(v.getName() + " : " + getStartTime(v)  + " -> " + getFinishTime(v));	
	 		}
		}
		System.out.println("==============\n");
	}
	
	/**
	 * Recursive DFS from a free vertex that is not yet run on any processorS
	 * @param vertex free vertex
	 * @param freeVertices free vertices
	 */
	private void dfs(Vertex vertex, ArrayList<Vertex> freeVertices){
		// vertices NOT YET RUN
		ArrayList<Vertex> currentFreeVertices = new ArrayList<>(freeVertices); 
		
		// REMOVE VERTEX TO RUN
		currentFreeVertices.remove(vertex);
		
		// do *STATE SPACE SEARCH* by assigning the VERTEX to each processor 
		// so that RESULTS can be checked for different processors
		for(int i=0; i<_noOfProcessors; i++){
			// BOUNDING
			// go 1 back up the branch, if abandoning branch
			boolean abandonSearch = assignVertexToProcessor(i, vertex);
			if(abandonSearch){
				revertChangesMade(vertex);
				continue;
			}
			
			/**
			 * LEAF REACHED, CHECK IF SOLUTION is OPTIMAL
			 * This is done by comparing the last finish times of all the processors
			 */
			if(currentFreeVertices.size() == 0){
				// FIND LATEST FINISH TIME among the processors
				int[] processorAndFinishTime = new int[]{0,getFinishTimeOfLastTask(0)}; 
				for(int j=1; j<_noOfProcessors; j++){
					if(getFinishTimeOfLastTask(j) > processorAndFinishTime[1]){
						processorAndFinishTime = new int[]{j,getFinishTimeOfLastTask(j)};
					}
				}
				// IF LATEST FINISH TIME < CURRENT OPTIMAL, update optimal solution
				if(processorAndFinishTime[1] < _optimalCost){
					System.out.println("Optimal cost found: " + _optimalCost); //TODO debug
					// store the current minimum cost
					_optimalCost = processorAndFinishTime[1];
					// store the current optimal solution
					_optimalProcessorListSolution = new HashMap<Integer,ArrayList<Vertex>>(_processorList);
					_optimalVertexScheduleInfo = new HashMap<Vertex, int[]>(_vertexScheduleInfo);
				}
			}
			
			// list of NODES NOT RUN YET but with DEPENDENCIES RAN
			ArrayList<Vertex> nodesToConsider = new ArrayList<>();
			for (Vertex v : _digraph.vertexSet()){
				// check if dependencies ran
				boolean consider = true;
				for(Vertex v2 : getParents(v)){
					if(! _vertexScheduleInfo.containsKey(v2)){
						consider = false;
					}
				}
				// add to list to consider if dependencies ran
				if(consider && !_vertexScheduleInfo.containsKey(v)){
					nodesToConsider.add(v);
				}
			}
			
			/**
			 * Start doing recursive DFS on nodes to consider
			 */
			for(Vertex v : nodesToConsider){
				dfs(v,currentFreeVertices);
			}

			// when all children considered, go 1 back up the branch
			revertChangesMade(vertex);
		}
	}
	
	/**
	 * Checks the start time for a vertex on this processor by checking for finishing
	 * times of the dependencies
	 * @param processorNo
	 * @param vertex
	 * @return whether to BOUND this branch starting with the vertex
	 */
	private boolean assignVertexToProcessor(int processorNo, Vertex vertex){
 		// LATEST FINISH TIME of a task on the current processor
 		int currentStartTimeForVertex = getFinishTimeOfLastTask(processorNo);
		
		// list of TASKS RAN ON PROCESSOR
 		ArrayList<Vertex> processorSchedule = _processorList.get(processorNo);
 		
		// locate the dependencies to consider start time
 		for(DefaultWeightedEdge e : _digraph.incomingEdgesOf(vertex)){
 			// GET A PARENT (DEPENDENCY)
 			Vertex parent = _digraph.getEdgeSource(e);
 			// check which processor was parent run on
 			for(int i=0; i<_noOfProcessors; i++){
 				// get the possible start time after communication from parent and 
 				// reconsider earliest possible start
				  if (_processorList.get(i).contains(parent)){ 
				     if(i != processorNo){ 
				       int possibleStartTimeForVertex = getFinishTime(parent) + (int) _digraph.getEdgeWeight(e); 
				       if(possibleStartTimeForVertex > currentStartTimeForVertex){ 
				         currentStartTimeForVertex = possibleStartTimeForVertex; 
				       } 
				     } 
 				}
 	 		}	
 		}
 		
 		// add vertex as ran
 		processorSchedule.add(vertex);
 		// add start and processor info for vertex
 		addVertexScheduleInformation(vertex, currentStartTimeForVertex, processorNo);

		debugger(vertex); //TODO debug
		
 		// BOUND if running total larger than optimal cost
		for(int j=0; j<_noOfProcessors; j++){
			int totalForBranch = getFinishTimeOfLastTask(j);
			if(totalForBranch > _optimalCost){
				System.out.println("Node : " + vertex.getName() + " " +totalForBranch);
				return true;
			}
		}
		
		// BOUND if considering bottom lvl results in > optimal cost
		if(currentStartTimeForVertex+_btmLevel.get(vertex) > _optimalCost){
			return true;
		}
		
 		return false;
	}
	
	/**
	 * Revert changes done by considering the current vertex while doing DFS
	 * @param vertex vertex that made changes to the schedules and information map
	 */
	private void revertChangesMade(Vertex vertex){
		// remove the vertex from the processor list and any information regarding in
		_processorList.get(getProcessorNo(vertex)).remove(vertex);
		_vertexScheduleInfo.remove(vertex);
	}
	
	/**
	 * Add vertex schedule information
	 * @param vertex
	 * @param startTime
	 * @param processorNo
	 */
	private void addVertexScheduleInformation(Vertex vertex, int startTime, int processorNo){
		_vertexScheduleInfo.put(vertex, new int[]{startTime, startTime + vertex.getWeight(), processorNo});
	}
	
	
	/**
	 * Get parents vertices
	 * @param vertex
	 * @return parents vertices
	 */
	private ArrayList<Vertex> getParents(Vertex vertex){
		ArrayList<Vertex> parents = new ArrayList<>();
		for(DefaultWeightedEdge e : _digraph.incomingEdgesOf(vertex)){
			parents.add(_digraph.getEdgeSource(e));
		}
		return parents;
	}

	/**
	 * Get children vertices
	 * @param vertex
	 * @return children vertices
	 */
	private ArrayList<Vertex> getChildren(Vertex vertex){
		ArrayList<Vertex> children = new ArrayList<>();
		for(DefaultWeightedEdge e : _digraph.outgoingEdgesOf(vertex)){
			children.add(_digraph.getEdgeTarget(e));
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
	 * Returns start time of a vertex on a processor
	 * @param vertex
	 * @return
	 */
	private int getStartTime(Vertex vertex){
		return _vertexScheduleInfo.get(vertex)[0];
	}
	
	/**
	 * Returns optimal start time of a vertex on a processor
	 * @param vertex
	 * @return
	 */
	private int getOptimalStartTime(Vertex vertex){
		return _optimalVertexScheduleInfo.get(vertex)[0];
	}
	
	/**
	 * Returns finish time of a vertex on a processor
	 * @param vertex
	 * @return
	 */
	private int getFinishTime(Vertex vertex){
		return _vertexScheduleInfo.get(vertex)[1];
	}
	
	/**
	 * Returns processor no of a vertex,
	 * for uses like removing a vertex from a processor's list,
	 * by knowing which processor it was on
	 * @param vertex
	 * @return
	 */
	private int getProcessorNo(Vertex vertex){
		return _vertexScheduleInfo.get(vertex)[2];
	}
	
	/**
	 * Returns optimal processor no of a vertex,
	 * for uses like removing a vertex from a processor's list,
	 * by knowing which processor it was on
	 * @param vertex
	 * @return
	 */
	private int getOptimalProcessorNo(Vertex vertex){
		return _optimalVertexScheduleInfo.get(vertex)[2];
	}
	
	
	/**
	 * Returns last finish time of a vertex on a processor
	 * @param processorNo
	 * @return
	 */
	private int getFinishTimeOfLastTask(int processorNo){
		// get the list of vertices associated with a processor
 		ArrayList<Vertex> processorSchedule = _processorList.get(processorNo);
 		// 0 if no tasks on it
 		if(processorSchedule.size() == 0){
 			return 0;
 		}
		return _vertexScheduleInfo.get(processorSchedule.get(processorSchedule.size()-1))[1];
	}
	
	/**
	 * Returns root nodes of the digraph
	 * @return root nodes of the digraph
	 */
	private ArrayList<Vertex> returnRootVertices(){
		ArrayList<Vertex> rootVertices = new ArrayList<>();
		for (Vertex v: _digraph.vertexSet()) {
			if (_digraph.inDegreeOf(v)==0) {
				rootVertices.add(v);
			}
		}
		return rootVertices;
	}
	
}
