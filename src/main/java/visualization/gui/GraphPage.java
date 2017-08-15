package visualization.gui;


import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import visualization.Visualizer;

import java.awt.GridLayout;

public class GraphPage extends JPanel {
	View _view;
	
	/**
	 * Create the panel.
	 */
	public GraphPage(Visualizer graphVisualizer) {
		
		_view = graphVisualizer.displayGraph();
		
		this.add((Component)_view);
		
		setPreferredSize(new Dimension(600,450));
		setLayout(new GridLayout(1, 0, 0, 0));
	}

}
