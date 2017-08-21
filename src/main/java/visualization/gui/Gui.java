package visualization.gui;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import visualization.Visualizer;
import visualization.gantt.Gantt;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.TextArea;

public class Gui {
	protected Visualizer _visualizer;
	public JFrame frame;
	protected JPanel _cards;
	protected JPanel _graphPage;
	private JPanel panel;
	private JButton _active;
	private JButton _notActive;
	private JTextArea infoArea;
	private JTextField txtrTask;

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
		//Initializing the outer container/frame for the gui.
		frame = new JFrame();
		frame.setTitle("Imagine Breaker - Task Scheduler");
		frame.getContentPane().setBackground(new Color(239,239,239));
		frame.setBounds(50, 50, 890, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		//Initializing the panel with a card layout to store graphs. 
		_cards = new JPanel(new CardLayout());
		_cards.setBorder(new LineBorder(new Color(180,235,250), 5, true));
		_cards.setBounds(15, 15, 700, 540);
		_cards.setPreferredSize(new Dimension(700,540));
		CardLayout cardLayout = (CardLayout) _cards.getLayout();
		frame.getContentPane().add(_cards);
		
		//Initializing the graph from GraphStream than adding it to the cards panel.
		_graphPage = new GraphPage(_visualizer);
		_graphPage.setPreferredSize(new Dimension(700,540));
		
		_cards.add(_graphPage, "Graph");
		
		//Initializing the Gantt chart than adding it to the cards panel.
		Gantt gantt = new Gantt(null);
		JPanel ganttPanel = gantt.createDemoPanel();
		ganttPanel.setPreferredSize(new Dimension(1200, 800));

		_cards.add(ganttPanel,"Gantt");
		
		//Initializing the button related to the gantt chart for usage.
		JButton ganttButton = new CustomButton("Gantt Chart");
		
		//Initializing the buttons
		JButton graphButton = new CustomButton("Tree Graph");
		
		_active = graphButton;
		graphButton.setBackground(new Color(255, 135, 135));
		
		graphButton.setBounds(734, 15, 140, 50);
		frame.getContentPane().add(graphButton);
		
		//Adding an action listener for the button related to the tree graph.
		graphButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(_cards, "Graph");
				_active = graphButton;
				_notActive = ganttButton;
				changeActive();
			}
		});
		
		
		//Giving looks to the gantt chart button
		ganttButton.setBounds(734, 76, 140, 50);
		frame.getContentPane().add(ganttButton);

		//Adding an action listener for the button related to the gantt chart.
		ganttButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(_cards, "Gantt");
				_active = ganttButton;
				_notActive = graphButton;
				changeActive();
			}
		});

		
		//Creating a button for exit.
		JButton btnClose = new CustomButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				
			}
		});

		btnClose.setBounds(734, 505, 140, 50);
		frame.getContentPane().add(btnClose);
		
		//TODO
		infoArea = new JTextArea();
		infoArea.setText("Press the node to see the info");
		infoArea.setEditable(false);
		infoArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
		infoArea.setBounds(734, 248, 140, 246);
		
		infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setMargin(new Insets(5,5,5,5));
		
        frame.getContentPane().add(infoArea);
		
		CustomButton statButton = new CustomButton("Gantt Chart");
		statButton.setText("Statistics");
		statButton.setBounds(734, 137, 140, 50);
		frame.getContentPane().add(statButton);
		
		txtrTask = new JTextField();
		txtrTask.setHorizontalAlignment(SwingConstants.CENTER);
		txtrTask.setFont(new Font("SansSerif", Font.BOLD, 20));
		txtrTask.setText("Task");
		txtrTask.setForeground(new Color(255, 255, 255));
		txtrTask.setBackground(new Color(0, 153, 51));
		txtrTask.setEditable(false);
		txtrTask.setBounds(734, 198, 140, 50);
		txtrTask.setMargin(new Insets(5,5,5,5));
		
		frame.getContentPane().add(txtrTask);

	}
	
	/**
	 * Change the color of the active button on the GUI
	 */
	private void changeActive(){
		_active.setBackground(new Color(255, 135, 135));
		_notActive.setBackground(new Color(255, 59, 63));

	}
	
	/**
	 * 
	 */
	public void showInfoOnTextArea(String task, int pNo, Object startTime, Object endTime){
		txtrTask.setText("Task "+task);
		infoArea.setText("Processor Number: "+pNo+"\n\nStart Time: " + startTime + "\n\nEnd Time: "+endTime);
		
	}
	/**
	 * 
	 */
	public void noInfoToShow(String task){
		txtrTask.setText("Task "+task);
		infoArea.setText("This task is not scheduled yet");
	}
}


