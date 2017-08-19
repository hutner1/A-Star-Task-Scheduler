package visualization.gui;

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

import visualization.Visualizer;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.border.LineBorder;

public class Gui {
	protected Visualizer _visualizer;
	public JFrame frame;
	protected JPanel _cards;
	protected JPanel _graphPage;
	private JPanel panel;
	private JButton _active;
	private JButton _notActive;


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
		frame.setTitle("Imagine Breaker - Task Scheduler");
		frame.getContentPane().setBackground(new Color(239,239,239));
		frame.setBounds(100, 100, 850, 580);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBounds(30, 15, 600, 500);
		
		frame.getContentPane().add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		_cards = new JPanel(new CardLayout());
		_cards.setBorder(new LineBorder(new Color(180,235,250), 5, true));
		panel.add(_cards);
		_cards.setPreferredSize(new Dimension(600,500));

		_graphPage = new GraphPage(_visualizer);
		_graphPage.setPreferredSize(new Dimension(600,530));
		_cards.add(_graphPage, "Graph");
		JPanel gantt = new JPanel();
		
		_cards.add(gantt,"Gantt");
		
		CardLayout cardLayout = (CardLayout) _cards.getLayout();
		
		
		JButton graphButton = new CustomButton("Tree Graph");
		JButton ganttButton = new CustomButton("Gantt Chart");
		_active = graphButton;
		graphButton.setBackground(new Color(255, 135, 135));
		
		graphButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(_cards, "Graph");
				_active = graphButton;
				_notActive = ganttButton;
				changeActive();
			}
		});
		
		
		
		graphButton.setBounds(671, 15, 140, 50);
		frame.getContentPane().add(graphButton);

		
		ganttButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(_cards, "Gantt");
				_active = ganttButton;
				_notActive = graphButton;
				changeActive();
			}
		});

		
		ganttButton.setBounds(671, 82, 140, 50);
		frame.getContentPane().add(ganttButton);

		JButton btnClose = new CustomButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				
			}
		});

		btnClose.setBounds(671, 465, 140, 50);
		frame.getContentPane().add(btnClose);

	}

	public void updateGraphGui(){
		_graphPage.revalidate();
		_graphPage.repaint();

	}
	
	private void changeActive(){
		_active.setBackground(new Color(255, 135, 135));
		_notActive.setBackground(new Color(255, 59, 63));

	}
}


