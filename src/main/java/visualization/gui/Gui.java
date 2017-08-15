package visualization.gui;



import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import visualization.Visualizer;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;

public class Gui {
	protected Visualizer _visualizer;
	public JFrame frame;
	protected JPanel _cards;
	protected JPanel _graphPage;

	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] agrs) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		 UIManager.setLookAndFeel(
		            UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Gui window = new Gui(null);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gui(Visualizer DAG) {
		_visualizer = DAG;
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		_cards = new JPanel(new CardLayout());
		
		frame.getContentPane().add(_cards, BorderLayout.CENTER);
		
		_graphPage = new GraphPage(_visualizer);
		_cards.add(_graphPage, "name_3915657358581");
		
	}
	
	public void updateGraphGui(){
		_graphPage.revalidate();
		_graphPage.repaint();
		
	}
	
}
