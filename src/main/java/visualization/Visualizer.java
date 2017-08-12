package visualization;

import java.util.ArrayList;

import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.LobsterGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;


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

	public Visualizer(){
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		_graph = new SingleGraph("Input Graph");
		_graph.addAttribute("ui.quality");
		_graph.addAttribute("ui.antialias");
		_graph.setAttribute("stylesheet", stylesheet);
	}

	public void add(DefaultDirectedWeightedGraph DAG) {

		for(Vertex vertex : DAG.vertexSet()){
			Node n =_graph.addNode(vertex.getName());
			if(DAG.inDegreeOf(vertex) == 0){
				n.addAttribute("ui.style", " size:40" +"px;");
			} else {
				n.addAttribute("ui.style", " size:25" +"px;");
			}
			n.addAttribute("ui.label", n.getId());
		}


		for(DefaultWeightedEdge edge : DAG.edgeSet()){
			String source = DAG.getEdgeSource(edge).getName();
			String target = DAG.getEdgeTarget(edge).getName();

			_graph.addEdge(source + target ,source , target, true);
		}


	}


	public void displayGraphs() {
		Viewer viewer = _graph.display();
		View view = viewer.getDefaultView();
		/*view.setViewCenter(2, 3, 4);*/
	}


}
