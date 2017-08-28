package visualization.gui;


import java.awt.GridLayout;

import javax.swing.JPanel;

import org.graphstream.ui.swingViewer.ViewPanel;

import visualization.graph.Visualizer;
/**
 * Generates a panel to contain the graph produced by the Visualizer class.
 */
@SuppressWarnings("serial")
public class GraphPage extends JPanel {
	ViewPanel _view;

	/**
	 * Create the panel with graphstream inside.
	 */
	public GraphPage(Visualizer graphVisualizer) {

		_view = graphVisualizer.displayGraph();

		this.add(_view);

		setLayout(new GridLayout(1, 0, 0, 0));
	}

}
