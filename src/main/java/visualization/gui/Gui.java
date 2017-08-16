package visualization.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import visualization.Visualizer;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class Gui {
	protected Visualizer _visualizer;
	public JFrame frame;
	protected JPanel _cards;
	protected JPanel _graphPage;
	private JPanel panel;

	/**
	 *This part is there for just testing reason.
	 *This is needed to use WindowsBuilder which is a Swing Builder.
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
	public Gui(Visualizer visualizer) {
		_visualizer = visualizer;
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 850, 580);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBounds(30, 15, 600, 500);
		frame.getContentPane().add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		_cards = new JPanel(new CardLayout());
		panel.add(_cards);
		_cards.setPreferredSize(new Dimension(600,500));

		_graphPage = new GraphPage(_visualizer);
		_graphPage.setPreferredSize(new Dimension(600,530));
		_cards.add(_graphPage, "Graph");
		JPanel gantt = new JPanel();
		
		_cards.add(gantt,"Gantt");
		
		CardLayout cardLayout = (CardLayout) _cards.getLayout();
		JButton btnNewButton = new JButton("Tree Graph");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(_cards, "Graph");
			}
		});
		btnNewButton.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		btnNewButton.setBounds(671, 24, 133, 45);
		frame.getContentPane().add(btnNewButton);

		JButton btnGanttChart = new JButton("Gantt Chart");
		btnGanttChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(_cards, "Gantt");
			}
		});
		btnGanttChart.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		btnGanttChart.setBounds(671, 82, 133, 45);
		frame.getContentPane().add(btnGanttChart);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		btnClose.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		btnClose.setBounds(671, 470, 133, 45);
		frame.getContentPane().add(btnClose);

	}

	public void updateGraphGui(){
		_graphPage.revalidate();
		_graphPage.repaint();

	}
}


