package visualization;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.event.MouseInputListener; 

import org.graphstream.graph.Edge; 
import org.graphstream.graph.Graph; 
import org.graphstream.graph.Node;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe; 

/**
 * Listener to handle click of nodes. 
 * The custom graph stream library uses a tight loop to pump mouse click events 
 * to the graph stream lib, but we take a signal based approach to this by using hte native java  
 * mouse listener, and on each event trigger the pump to the graph stream library 
 * 
 */ 
public class NodeClickListener implements ViewerListener , MouseInputListener{  
	private ViewerPipe _viewerPipe = null; 
	private View _view = null; 
	private Graph _graph = null; 

	/**
	 * Constructor 
	 * @param _viewerPipe - Viewer Pipe of the graph UI 
	 * @param _view - View of the current graph in swing 
	 * @param g - graph object for the current graph in use 
	 */ 
	public NodeClickListener(ViewerPipe viewerPipe, View view, Graph graph) { 
		this._viewerPipe = viewerPipe; 
		this._view = view; 
		this._graph = graph; 
		// Keep piping back while grph is out to hook mouse clicks 
		this._view.addMouseListener(this); 

	} 


	/**
	 * Close the view when graph is no longer needed and detach all listeners 
	 * @param id - inherited by interface, not used
	 */ 
	public void viewClosed(String id) { 
		_view.removeMouseListener(this); 

	} 
	
	/**
	 * Displays the node/task information once clicked
	 * @param id - id string of the node
	 */ 
	public void buttonPushed(String id) { 

		System.out.println("Button pushed on node "+id); 
		Node n = _graph.getNode(id); 

	} 



	@Override 
	/**
	 * Mouse release event to pump on release 
	 */ 
	public void mouseReleased(MouseEvent e) { 
		
		_viewerPipe.pump(); 

	} 


	/**
	 * Inherited function unused 
	 */ 
	public void buttonReleased(String id) { 
		// TODO Auto-generated method stub 
		
	} 



	/**
	 * Inherited function unused 
	 */ 
	@Override 
	public void mouseClicked(MouseEvent e) { 
		// TODO Auto-generated method stub 

	} 

	/**
	 * Inherited function unused 
	 */ 
	@Override 
	public void mouseEntered(MouseEvent e) { 
		// TODO Auto-generated method stub 

	} 

	/**
	 * Inherited function unused 
	 */ 
	@Override 
	public void mouseExited(MouseEvent e) { 
		// TODO Auto-generated method stub 

	} 

	/**
	 * Inherited function unused 
	 */ 
	@Override 
	public void mousePressed(MouseEvent e) { 
		// TODO Auto-generated method stub 
		//_viewerPipe.pump(); 
		//System.out.println("Pump it!"); 
	} 


	/**
	 * Inherited function unused 
	 */ 
	@Override 
	public void mouseDragged(MouseEvent arg0) { 
		// TODO Auto-generated method stub 

	} 

	/**
	 * Inherited function unused 
	 */ 
	@Override 
	public void mouseMoved(MouseEvent arg0) { 
		// TODO Auto-generated method stub 

	} 



}
