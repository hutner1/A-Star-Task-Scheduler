package visualization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.LobsterGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import scheduler.astar.ProcessInfo;
import scheduler.astar.Processor;
import scheduler.astar.Solution;
import scheduler.basicmilestone.Vertex;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;


public class Visualizer {

	private String stylesheet = 
			"graph {" +
			"fill-color: rgb(225,225,225);" +
			"}" + 
			
			"edge {" +
			"size:3px;" +
			"arrow-shape: arrow; " +
    		"arrow-size: 12px, 6px;" +
			/*shape:cubic-curve;*/
			/*shape:blob;*/
			/*fill-color: rgb(127,0,55);*/
			"}" +
    		
    		"node{" +
    		"text-size:16px;"+
    		"text-color:rgb(255,255,255);"+
    		"stroke-mode:plain;"+
    		"stroke-width:2px;"+
    		"stroke-color:#FFF8;"+
    		"}";
	private Graph _graph;
	private Viewer _viewer;
	private DefaultDirectedWeightedGraph _DAG;

	public Visualizer(){
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		_graph = new SingleGraph("Input Graph");
		_graph.addAttribute("ui.quality");
		_graph.addAttribute("ui.antialias");
		_graph.setAttribute("stylesheet", stylesheet);
	}

	public void add(DefaultDirectedWeightedGraph DAG) {

		_DAG = DAG;
		for(Vertex vertex : DAG.vertexSet()){
			Node n =_graph.addNode(vertex.getName());
			if(DAG.inDegreeOf(vertex) == 0){
				n.addAttribute("ui.style", " size:40px;");
				n.setAttribute("y", 300);
				n.setAttribute("x", 0);
				
			}else if(DAG.outgoingEdgesOf(vertex).size() < 1) {
				n.addAttribute("ui.style", " size:40px;");
				
			} else {
				n.addAttribute("ui.style", " size:25px;");
			}
			n.addAttribute("ui.label", n.getId());
		}


		for(DefaultWeightedEdge edge : DAG.edgeSet()){
			String source = DAG.getEdgeSource(edge).getName();
			String target = DAG.getEdgeTarget(edge).getName();

			_graph.addEdge(source + target ,source , target, true);
		}


	}


	public void displayGraph() {
		_viewer = _graph.display();
		View view = _viewer.getDefaultView();
		/*view.setViewCenter(2, 3, 4);*/
	}

	public void UpdateGraph(Solution currentBestSol) {
		
		//Get the hash map of the processes
		HashMap<Integer, Processor> processorWithSolution =currentBestSol.getProcess();
		
		//Set all nodes to black to reset previous visualization
		for(Vertex vertex : _DAG.vertexSet()){
			_graph.getNode(vertex.getName()).setAttribute("ui.style", "fill-color:#"+ "000000" +";");
		}
		
		for(int i = 1; i < processorWithSolution.keySet().size() + 1; i++){
			List<ProcessInfo> processes = processorWithSolution.get(i).getProcesses();
			for(ProcessInfo processInfo : processes){
				String colorCode = getColor(i);
				
				_graph.getNode(processInfo.getVertex().getName()).setAttribute("ui.style", "fill-color:#"+ colorCode +";");
			}
		}
		
	}
	
	public String getColor(int index){
		String[] colors = {	"ffffff", "e74c3c", "FFC300", "1d8348", "8e44ad", "2874a6", 
				"e67e22", "5d6d7e", "45b39d", "aed6f1", "f4f6f7", "cc5c92", "f0a0a0"};

		return colors[index];
	}

}
