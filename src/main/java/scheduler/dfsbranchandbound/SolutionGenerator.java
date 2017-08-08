package scheduler.dfsbranchandbound;

import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import scheduler.basicmilestone.Schedule;
import scheduler.basicmilestone.Vertex;

/**
 * This class generates an optimal solution using Depth First Search Branch & Bound algorithm
 *
 */
public class SolutionGenerator {

	DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> _digraph;
	int _noOfProcessors; 
	int _currentMinCost;
	
	// For storage of current optimal solution
	HashMap<Integer, ArrayList<Vertex>> _processorList;
	// int[startTime, finishTime, processorNo]
	HashMap<Vertex,int[]> _vertexScheduleInfo;
	
	// For storage of OPTIMAL solution
	HashMap<Integer, ArrayList<Vertex>> _optimalProcessorListSolution;
	HashMap<Vertex,int[]> _optimalVertexScheduleInfo;
	
	// Current OPTIMAL cost
	int _optimalCost;
	
	/**
	 * Takes in digraph information to generate optimum solution for
	 * @param digraph
	 * @param noOfProcessors
	 */
	public SolutionGenerator(DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> digraph, int noOfProcessors){
		_digraph = digraph;
		_noOfProcessors = noOfProcessors;
		_processorList = new HashMap<>();
		// create a list for tasks for each processor
		for(int i = 0; i<noOfProcessors; i++){
			_processorList.put(i, new ArrayList<>());
		}
		_vertexScheduleInfo = new HashMap<>();
		
		// starting optimal cost is max value
		_optimalCost = Integer.MAX_VALUE;
	} 
	
	/**
	 * Does Depth First Search Branch & Bound and returns the optimal solution
	 * @return optimal schedule
	 */
	public HashMap<Vertex,int[]> generateSolution(){
		ArrayList<Vertex> rootVertices =  returnRootVertices();
		
		// start the dfs for different root vertices
		for(Vertex v : rootVertices){
			// create a shallow copy of vertices
			ArrayList<Vertex> initialVertices = new ArrayList<>();
			for (Vertex v2 : _digraph.vertexSet()){
				initialVertices.add(v2);
			}
			dfs(v, initialVertices);
		}
		
		return _optimalVertexScheduleInfo;
	}
	
	/**
	 * Get the output string for each vertex //TODO
	 * @param vertex
	 * @return
	 */
	public String outputString(Vertex vertex) {
		return ", Start=" + getOptimalStartTime(vertex) + ", Processor=" + getOptimalProcessorNo(vertex);
	}
	
	private void debugger(Vertex vertex){
		System.out.println("Adding " + vertex.getName() + " - " + _optimalCost);
		for(int i=0; i<_noOfProcessors; i++){
	 		ArrayList<Vertex> processorSchedule = _processorList.get(i);
	 		System.out.println("Processor "+i);
	 		for(Vertex v: processorSchedule){
		 		System.out.println(v.getName() + " : " + getStartTime(v)  + " -> " + getFinishTime(v) + " = " + getProcessorNo(v) );
	 			
	 		}

		}
		System.out.println("==============\n");

	}
	
	/**
	 * Recursive DFS from a free vertex that is not in any processor
	 * @param vertex free vertex
	 * @param freeVertices free vertices
	 */
	private void dfs(Vertex vertex, ArrayList<Vertex> freeVertices){
		
		// list of vertices currently not on any processors (to be processed) //TODO I think I'm making a copy here
		ArrayList<Vertex> currentFreeVertices = new ArrayList<>(freeVertices); //TODO
		// remove vertex to process
		currentFreeVertices.remove(vertex);
		
		// do STATE SPACE SEARCH by assigning the VERTEX to each processor 
		// so that RESULTS can be checked for different processors
		for(int i=0; i<_noOfProcessors; i++){

			// BOUNDING
			// determine whether to abandon search based on the running total 
			boolean abandonSearch = assignVertexToProcessor(i, vertex);
			if(abandonSearch){
				revertChangesMade(vertex);
				System.out.println("Abandon Search");
				continue;
			}
			
			
			//TODO refactor
			/**
			 * Get the optimal solution for the branch when leaf of DFS search is reached
			 * This is done by comparing the last finish times of all the processors
			 */
			if(currentFreeVertices.size() == 0){
				int[] lastFinish = null; 
				for(int j=0; j<_noOfProcessors; j++){
					if(lastFinish == null){
						lastFinish = new int[]{j,getFinishTimeOfLastTask(j)};
						continue;
					}
					if(getFinishTimeOfLastTask(j) > lastFinish[1]){
						lastFinish = new int[]{j,getFinishTimeOfLastTask(j)};
					}
				}
				if(lastFinish[1] < _optimalCost){
					// store the current minimum cost
					_optimalCost = lastFinish[1];
					System.out.println("Leaf reached, optimal cost: " + _optimalCost);
					// store the current optimal solution
					_optimalProcessorListSolution = new HashMap<Integer,ArrayList<Vertex>>(_processorList); //TODO
					_optimalVertexScheduleInfo = new HashMap<Vertex, int[]>(_vertexScheduleInfo); //TODO
				}
			}
			
			// nodes that have parents already run and haven't been considered yet
			ArrayList<Vertex> nodesToConsider = new ArrayList<>(); //TODO
			for (Vertex v : _digraph.vertexSet()){
				boolean consider = true;
				for(Vertex v2 : getParents(v)){
					if(! _vertexScheduleInfo.containsKey(v2)){
						consider = false;
					}
				}
				if(consider && !_vertexScheduleInfo.containsKey(v)){
					nodesToConsider.add(v);
				}
			}
			
			
			
			//TODO need to look at all nodes whose dependencies are run
			/**
			 * Start doing DFS on children vertices
			 */
			for(Vertex v : nodesToConsider){
				dfs(v,currentFreeVertices);
			}
			
			System.out.println("Leaf reached! from vertex: " + vertex.getName() + "\n");

			// when all children have been convert, go back up the branch
			revertChangesMade(vertex);
			
		}
		
		
	}
	
	/**
	 * Checks the start time for the vertex on this processor by checking for finishing
	 * times of the dependencies
	 * @param processorNo
	 * @param vertex
	 */
	private boolean assignVertexToProcessor(int processorNo, Vertex vertex){
		// get the list of vertices associated with a processor
 		ArrayList<Vertex> processorSchedule = _processorList.get(processorNo);
 		// get the last finish time of a task on the current processor
 		int currentStartTimeForVertex = getFinishTimeOfLastTask(processorNo);
 		
		// check where the dependencies of the vertex to add to this current processor are at
 		for(DefaultWeightedEdge e : _digraph.incomingEdgesOf(vertex)){
 			// get a parent
 			Vertex parent = _digraph.getEdgeSource(e);
 			// check which processor was parent run on
 			for(int i=0; i<_noOfProcessors; i++){
 				// if the processor is not the current processor, get the possible start time after
 				// communication from the parent is done and if it is higher than the
 				// earliest possible start time on the current processor, replace it
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
 		// add vertex to the current processor
 		processorSchedule.add(vertex);
 		// add start time information for that vertex, finish time will be generated
 		addVertexScheduleInformation(vertex, currentStartTimeForVertex, processorNo);

		debugger(vertex);
		
 		// based on running total cost, BOUND if larger than optimal cost
		for(int j=0; j<_noOfProcessors; j++){
			int totalForBranch = getFinishTimeOfLastTask(j);
			if(totalForBranch > _optimalCost){
				System.out.println("Node : " + vertex.getName() + " " +totalForBranch);
				return true;
			}
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
