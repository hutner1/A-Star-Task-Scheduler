package Visualization;

import java.util.ArrayList;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.view.Viewer;


import scheduler.basicmilestone.Vertex;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;

public class Visualizer {
	
	ArrayList<Graph> _graphs;
	
	public Visualizer(){
		_graphs = new ArrayList<Graph>();
	}
	
	public void add(DefaultDirectedWeightedGraph DAG) {
		
		Graph graph = new SingleGraph("Input Graph");
		
		for(Vertex vertex : DAG.vertexSet()){
			graph.addNode(vertex.getName());
		}
		

		for(DefaultWeightedEdge edge : DAG.edgeSet()){
			String source = DAG.getEdgeSource(edge).getName();
			String target = DAG.getEdgeTarget(edge).getName();
			
			graph.addEdge(target + source ,target , source);
		}
		
		
	 _graphs.add(graph);
	}
	
	
	public void displayGraphs() {
		for(Graph graph: _graphs){
			graph.addAttribute("ui.stylesheet", "node { text-mode: normal;}"
					+ "edge {shape: line;fill-color: #222;arrow-size: 3px, 2px;}");
			Viewer viewer = graph.display();
			HierarchicalLayout hl = new HierarchicalLayout();
			viewer.enableAutoLayout(hl);
		}
	}
	

}
