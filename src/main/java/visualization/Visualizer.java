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
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import scheduler.astar.ProcessInfo;
import scheduler.astar.Processor;
import scheduler.astar.Solution;
import scheduler.basicmilestone.Vertex;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;


public class Visualizer {

	//Set the style for the graph using CSS
	private String _stylesheet = 
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
	private HashMap<Integer, Processor> _processorWithSolution = null;


	public Visualizer(){
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		_graph = new SingleGraph("Input Graph");
		_graph.addAttribute("ui.quality");
		_graph.addAttribute("ui.antialias");
		_graph.setAttribute("stylesheet", _stylesheet);

	}


	/**
	 * This adds the nodes and edges from the directed weight graph into the graph stream
	 * data structure
	 * 
	 * @param DAG the directed weighted graph input
	 * 
	 */
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

	/**
	 * 
	 * This method displays the graph onto the screen, and also add action listeners
	 * to mouse action (not yet implemented)
	 * 
	 */
	public void displayGraph() {
		_viewer = _graph.display();
		/*		HierarchicalLayout hl = new HierarchicalLayout();
		_viewer.enableAutoLayout(hl);*/
		/*_viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);*/
		/*View view = _viewer.getDefaultView();*/

		/*ViewerPipe fromViewer = _viewer.newViewerPipe();
		fromViewer.addViewerListener(this);
		fromViewer.addSink(_graph);*/
		/*view.setViewCenter(2, 3, 4);*/

		/*ProxyPipe fromViewer = _viewer.newViewerPipe();
        fromViewer.addSink(_graph);*/

		/*Viewer viewer = new Viewer(_graph,Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		View viewPanel = viewer.addDefaultView(false);*/
		/*viewPanel.addMouseListener(new MouseListener l);*/
		/*		viewPanel.addMouseListener(new MouseListener() {
		    public void mouseClicked(MouseEvent e) {
		    	System.out.println("camera: " + e.getSource().getClass().getName());
		        Point3 gu = view.getCamera().transformPxToGu(e.getX(), e.getY());
		        Node node = graph.addNode(e.getWhen());
		        node.setAttribute("xyz", gu.x, gu.y, 0);

		    }

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});*/


		/*ViewerPipe fromViewer = viewer.newViewerPipe();*/
		/*fromViewer.addViewerListener(n);*/
		/*fromViewer.addSink(_graph);
		fromViewer.pump();*/


		/*		ViewerPipe fromViewer = _viewer.newViewerPipe();
        fromViewer.addViewerListener(new ViewerListener(){

			@Override
			public void viewClosed(String viewName) {
				// TODO Auto-generated method stub

			}

			@Override
			public void buttonPushed(String id) {
				// TODO Auto-generated method stub
				Node n  = _graph.getNode(id);
                String nodeName = n.toString();
                System.out.println(nodeName);
			}

			@Override
			public void buttonReleased(String id) {
				// TODO Auto-generated method stub

			}

        });
        fromViewer.addSink(_graph);
        fromViewer.pump();*/
		/*        while(true){

                fromViewer.pump();

        }*/


		View view = _viewer.getDefaultView();



		// We connect back the viewer to the graph, 
		// the graph becomes a sink for the viewer. 
		// We also install us as a viewer listener to 
		// intercept the graphic events. 
		ViewerPipe fromViewer = _viewer.newViewerPipe();
		NodeClickListener clisten = new NodeClickListener(fromViewer, view, _graph); 
		fromViewer.addViewerListener((ViewerListener) clisten); 

	}

	/**
	 * Updates the status/ visual of the graph based on the current best schedule.
	 * The color of nodes changes accordingly to the processor that it is assigned to
	 * 
	 * @param currentBestSol the current best solution schedule from the A* algorithm
	 */
	public void UpdateGraph(Solution currentBestSol) {

		//Get the hash map of the processes
		_processorWithSolution = currentBestSol.getProcess();

		//Set all nodes to black to reset previous visualization
		for(Vertex vertex : _DAG.vertexSet()){
			_graph.getNode(vertex.getName()).setAttribute("ui.style", "fill-color:#"+ "000000" +";");
		}

		for(int i = 1; i < _processorWithSolution.keySet().size() + 1; i++){
			List<ProcessInfo> processes = _processorWithSolution.get(i).getProcesses();
			for(ProcessInfo processInfo : processes){
				String colorCode = getColor(i);

				_graph.getNode(processInfo.getVertex().getName()).setAttribute("ui.style", "fill-color:#"+ colorCode +";");
			}
		}

	}


	/**
	 * Get the color code
	 * 
	 * @param index the index used to retrieve the color code from the array
	 * @return String the color code
	 */
	public String getColor(int index){
		String[] colors = {	"ffffff", "e74c3c", "FFC300", "1d8348", "8e44ad", "2874a6", 
				"e67e22", "5d6d7e", "45b39d", "aed6f1", "f4f6f7", "cc5c92", "f0a0a0"};

		return colors[index];
	}

}
