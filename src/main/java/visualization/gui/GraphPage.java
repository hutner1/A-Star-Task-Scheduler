package visualization.gui;


import javax.swing.JPanel;

import org.graphstream.ui.swingViewer.ViewPanel;
import visualization.Visualizer;

import java.awt.GridLayout;

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
