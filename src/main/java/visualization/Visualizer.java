package visualization;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.LobsterGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.stream.SinkAdapter;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import scheduler.astar.ProcessInfo;
import scheduler.astar.Processor;
import scheduler.astar.Solution;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;
import scheduler.graphstructures.Vertex;

/**
 * This class visualize the scheduling process with GraphStream
 * 
 */
public class Visualizer {

	//Set the style for the graph using CSS
	private String _stylesheet = 
			"graph {" +
					"fill-color: rgb(250,250,250);" +
					"}" + 

			"edge {" +
			"size:3px;" +
			"arrow-shape: arrow; " +
			"arrow-size: 12px, 6px;" +
			"}" +

    		"node{" +
    		"size:20px;"+
    		"text-size:16px;"+
    		"size:30px;"+
    		"text-color:rgb(255,255,255);"+
    		"stroke-mode:plain;"+
    		"stroke-width:2px;"+
    		"stroke-color:#FFF8;"+
    		"}";
	private Graph _graph;
	private Viewer _viewer;
	private DefaultDirectedWeightedGraph _DAG;
	private NodeClickListener _nodeClickListener = new NodeClickListener();

	
	public Visualizer(){
		
		//Creates the graph and initializes its attributes
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		_graph = new SingleGraph("Input Graph");
		_graph.addAttribute("ui.quality");
		_graph.addAttribute("ui.antialias");
		_graph.setAttribute("stylesheet", _stylesheet);
		_graph.addAttribute("ui.title", "Scheduling Visualization");
	}


	/**
	 * This adds the nodes and edges from the directed weight graph into the graph stream
	 * data structure
	 * 
	 * @param DAG The directed weighted graph input
	 * 
	 */
	public void add(DefaultDirectedWeightedGraph DAG) {

		_DAG = DAG;
		
		//Add all nodes of the DAG to the graph
		for(Vertex vertex : DAG.vertexSet()){
			Node n =_graph.addNode(vertex.getName());
			
			//Set the size of the nodes to be bigger if they are source node or a leaf
			if(DAG.inDegreeOf(vertex) == 0){
				n.addAttribute("ui.style", " size:40px;");
				
				//Allocate the position of the source node on the graph
				n.setAttribute("y", 300);
				n.setAttribute("x", 0);

			}else if(DAG.outgoingEdgesOf(vertex).size() < 1) {
				n.addAttribute("ui.style", " size:40px;");

			} else {
				n.addAttribute("ui.style", " size:25px;");
			}
			
			//Labels the node with their name
			n.addAttribute("ui.label", n.getId());
		}


		//Add all edges of the DAG to the graph
		for(DefaultWeightedEdge edge : DAG.edgeSet()){
			String source = DAG.getEdgeSource(edge).getName();
			String target = DAG.getEdgeTarget(edge).getName();

			_graph.addEdge(source + target ,source , target, true);
		}


	}

	/**
	 * 
	 * This method displays the graph onto the screen, and also add action listeners
	 * to node click action 
	 * 
	 */
	public ViewPanel displayGraph() {


		//Displays the graph
		_viewer = new Viewer(_graph,
                Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

		ViewPanel view = _viewer.addDefaultView(false);
		_viewer.enableAutoLayout();

		//Connect back the viewer to the graph, the graph becomes a sink for the viewer. 
		ViewerPipe fromViewer = _viewer.newViewerPipe();
		
		//Create and add an viewer listener to intercept node click events

		_nodeClickListener = new NodeClickListener(fromViewer, view, _graph); 
		fromViewer.addViewerListener((ViewerListener) _nodeClickListener); 

		return view;
	}
	
	/**
	 * Updates the status/ visual of the graph based on the current best schedule.
	 * The color of nodes changes accordingly to the processor that it is assigned to
	 * 
	 * @param currentBestSol The current best solution schedule from the A* algorithm
	 */
	public void UpdateGraph(Solution currentBestSol) {

		//Get the hash map of the processes
		HashMap<Integer, Processor> processorWithSolution = currentBestSol.getProcess();
		HashMap<String, List<Object>> scheduledVertices = new HashMap<String, List<Object>>();
		

		//Set all nodes to black to reset previous visualization
		for(Vertex vertex : _DAG.vertexSet()){
			_graph.getNode(vertex.getName()).setAttribute("ui.style", "fill-color:#"+ "000000" +";");
		}

		//Set the color for each node/task in the current schedule
		for(int i = 1; i < processorWithSolution.keySet().size() + 1; i++){
			List<ProcessInfo> processes = processorWithSolution.get(i).getProcesses();
			for(ProcessInfo processInfo : processes){
				String colorCode = getColor(i);
				String vertexName = processInfo.getVertex().getName();
				_graph.getNode(vertexName).setAttribute("ui.style", "fill-color:#"+ colorCode +";");
				
				List<Object> schedule = new ArrayList<Object>();
				schedule.add(i);
				schedule.add(processInfo);
				scheduledVertices.put(vertexName, schedule);
				
			}
		}
		_nodeClickListener.setCurrentSolution(scheduledVertices);

	}


	/**
	 * Get the color code
	 * 
	 * @param index the index used to retrieve the color code from the array
	 * @return String The color code
	 */
	public String getColor(int index){
		String[] colors = {	"ffffff", "e74c3c", "FFC300", "1d8348", "8e44ad", "2874a6", 
				"e67e22", "5d6d7e", "45b39d", "aed6f1", "d9fc67", "cc5c92", "f0a0a0"};

		return colors[index];
	}

}
