package visualization.gui;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import visualization.Visualizer;
import visualization.gantt.Gantt;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Gui {
	protected Visualizer _visualizer;
	public JFrame _frame;
	protected JPanel _cards;
	protected JPanel _graphPage;
	private JButton _active;

	private JButton _statButton;
	private JButton[] _notActive = new JButton[2];
	private JTextArea _infoArea;
	private JTextField _infoAreaTitle;
	private Gantt _gantt;
	private JPanel _stats;
	private int _numProcessor;

	/**
	 * Constructor for the GUI with some parameters.
	 */
	public Gui(Visualizer visualizer,Gantt gantt,StatisticTable stats,int numProcessor) {
		_visualizer = visualizer;
		_gantt = gantt;
		_stats = stats;
		_numProcessor = numProcessor;
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//Initializing the outer container/frame for the GUI.
		_frame = new JFrame();
		_frame.setTitle("Imagine Breaker - Task Scheduler");
		_frame.setBounds(50, 50, 990, 600);
		_frame.getContentPane().setLayout(null);
		_frame.setResizable(false);

		//Adding the background image
		try {
			_frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("resources\\back5.jpg")))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
		_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		//Adding actionWindowListener to shut down all program when the GUI is closed
		_frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run(){
						System.out.println("Shutting down all threads");	
					}
				});
				System.exit(0);
			}
		});

		//Initializing the panel with a card layout to store graphs and statistics. 
		_cards = new JPanel(new CardLayout());
		_cards.setBorder(new LineBorder(new Color(13, 90, 150),2));
		_cards.setBounds(15, 15, 800, 540);
		CardLayout cardLayout = (CardLayout) _cards.getLayout();

		_frame.getContentPane().add(_cards);

		//Initializing the graph from GraphStream than adding it to the cards panel.
		//Panel to wrap both tree graph and the legend.
		JPanel wrapPanel = new JPanel();
		wrapPanel.setLayout(null);
		wrapPanel.setBounds(0, 0, 680, 540);

		_cards.add(wrapPanel, "Graph");

		//To wrap graphstream and set the size.
		JPanel wrapPanel2 = new JPanel();
		wrapPanel2.setBounds(0, -4, 680, 540);

		wrapPanel.add(wrapPanel2);

		//Creating the panel with the tree graph inside it.
		_graphPage = new GraphPage(_visualizer);
		_graphPage.setPreferredSize(new Dimension(680,540));

		wrapPanel2.add(_graphPage);

		//Creating legend panel
		JPanel legend = new Legend(_numProcessor);
		legend.setBounds(680, 0, 120, 540);

		wrapPanel.add(legend);

		//Initializing the Gantt chart than adding it to the cards panel.
		JPanel ganttPanel = _gantt.createDemoPanel();
		ganttPanel.setPreferredSize(new Dimension(1200, 800));
		_cards.add(ganttPanel,"Gantt");

		//Adding stats table into the card layout.
		_stats.setPreferredSize(new Dimension(1200,1200));
		_cards.add(_stats,"Stats");

		//Initializing the button related to the gantt chart for usage.
		JButton ganttButton = new CustomButton("Gantt Chart");
		ganttButton.setBounds(834, 76, 140, 50);
		_frame.getContentPane().add(ganttButton);

		//Initializing the buttons
		JButton graphButton = new CustomButton("Tree Graph");
		_active = graphButton;
		graphButton.setBackground(new Color(6, 47, 79));
		graphButton.setBounds(834, 15, 140, 50);
		_frame.getContentPane().add(graphButton);

		//Adding an action listener for the button related to the tree graph.
		//The button changes the content of the _cards panel and change the color of button
		graphButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(_cards, "Graph");
				_active = graphButton;
				_notActive[0] = ganttButton;
				_notActive[1] = _statButton;
				changeActive();
			}
		});

		//Adding an action listener for the Gantt Chart button
		//This changes the content of the _cards panel into Gantt Chart and changne the color of the buttons
		ganttButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(_cards, "Gantt");
				_active = ganttButton;
				_notActive[0] = graphButton;
				_notActive[1] = _statButton;
				changeActive();
			}
		});

		//Initializing and adding button for statistics page
		_statButton = new CustomButton("Statistics Chart");
		_statButton.setText("Statistics");
		_statButton.setBounds(834, 137, 140, 50);
		_frame.getContentPane().add(_statButton);

		//Adding actionListener for the stats button which change the content of the main panel into statsTable 
		_statButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(_cards, "Stats");
				_active = _statButton;
				_notActive[0] = graphButton;
				_notActive[1] = ganttButton;
				changeActive();
			}
		});

		//Creating a button for exit.
		JButton btnClose = new CustomButton("Close");
		btnClose.setBounds(834, 505, 140, 50);
		_frame.getContentPane().add(btnClose);

		//Adding action listener which terminates the program when close button is pressed.
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run(){
						System.out.println("Shutting down all threads");	
					}
				});

				_frame.dispose();
				System.exit(0);
			}
		});

		//Initializing text area with the task name to be shown when the node in graphstream is pressed.
		_infoAreaTitle = new JTextField();
		_infoAreaTitle.setHorizontalAlignment(SwingConstants.CENTER);
		_infoAreaTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
		_infoAreaTitle.setText("Task");
		_infoAreaTitle.setForeground(new Color(255, 255, 255));
		_infoAreaTitle.setBackground(new Color(150, 63, 13));
		_infoAreaTitle.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		_infoAreaTitle.setEditable(false);
		_infoAreaTitle.setBounds(834, 220, 140, 30);
		_infoAreaTitle.setMargin(new Insets(5,5,5,5));

		_frame.getContentPane().add(_infoAreaTitle);

		//Initializing text area which shows the information about the task when node in the graphstream is pressed
		_infoArea = new JTextArea();
		_infoArea.setText("Press the node to see the info");
		_infoArea.setEditable(false);
		_infoArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
		_infoArea.setBounds(834, 248, 140, 246);
		_infoArea.setBorder(new LineBorder(new Color(13, 90, 150), 1, true));
		_infoArea.setBorder(BorderFactory.createCompoundBorder( 
				_infoArea.getBorder(),  
				BorderFactory.createEmptyBorder(5, 5, 5, 5))); 
		_infoArea.setBackground(new Color(250,250,250));
		_infoArea.setLineWrap(true);
		_infoArea.setWrapStyleWord(true);

		_frame.getContentPane().add(_infoArea);

	}

	/**
	 * Change the color of the active button on the GUI
	 */
	private void changeActive(){
		_active.setBackground(new Color(6, 47, 79));
		for(int i =0; i<_notActive.length;i++)
			_notActive[i].setBackground(new Color(13, 90, 150));
	}

	/**
	 * This method shows the vertex info on a text area
	 * @param task
	 * @param pNo
	 * @param startTime
	 * @param endTime
	 */
	public void showInfoOnTextArea(String task, int pNo, Object startTime, Object endTime){
		_infoAreaTitle.setText("Task "+task);
		_infoArea.setText("Processor Number: "+pNo+"\n\nStart Time: " + startTime + "\n\nEnd Time: "+endTime);

	}

	/**
	 * This method shows default info for an unscheduled task
	 * @param task
	 */
	public void noInfoToShow(String task){
		_infoAreaTitle.setText("Task "+task);
		_infoArea.setText("This task is not scheduled yet");
	}
}


