package visualization.gui;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

import javax.swing.border.Border;
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
	private JButton ganttButton;
	private JButton statButton;
	private JButton[] _notActive ={ganttButton,statButton};;
	private JTextArea infoArea;
	private JTextField txtrTask;
	private Gantt _gantt;
	private StatisticTable _stats;
	private int _numProcessor;

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
					Gui window = new Gui(null,null,null,1);
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
		//Initializing the outer container/frame for the gui.
		frame = new JFrame();
		frame.setTitle("Imagine Breaker - Task Scheduler");
		frame.getContentPane().setBackground(new Color(213, 213, 213));
		frame.setBounds(50, 50, 990, 600);
		/*frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
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


		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		try {
			frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("resources\\back5.jpg")))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*frame.setContentPane(new JLabel(new ImageIcon("C:\\Users\\Computer\\Downloads\\back4.png")));*/

		//Initializing the panel with a card layout to store graphs. 
		_cards = new JPanel(new CardLayout());
		_cards.setBorder(new LineBorder(new Color(13, 90, 150),2));
		_cards.setBounds(15, 15, 800, 540);
		_cards.setPreferredSize(new Dimension(800,540));
		CardLayout cardLayout = (CardLayout) _cards.getLayout();
		frame.getContentPane().add(_cards);

		//Initializing the graph from GraphStream than adding it to the cards panel.
		JPanel wrapPanel = new JPanel();
		wrapPanel.setLayout(null);
		wrapPanel.setBounds(0, 0, 680, 540);
		JPanel wrapPanel2 = new JPanel();
		wrapPanel2.setBounds(0, -4, 680, 540);
		_graphPage = new GraphPage(_visualizer);
		_graphPage.setPreferredSize(new Dimension(680,540));
		
		wrapPanel2.add(_graphPage);
		wrapPanel.add(wrapPanel2);
		
		JPanel legend = new Legend(_numProcessor);
		legend.setBounds(680, 0, 120, 540);
		
		wrapPanel.add(legend);
		
		
		_cards.add(wrapPanel, "Graph");

		//Initializing the Gantt chart than adding it to the cards panel.

		JPanel ganttPanel = _gantt.createDemoPanel();

		ganttPanel.setPreferredSize(new Dimension(1200, 800));

		_cards.add(ganttPanel,"Gantt");

		_stats.setPreferredSize(new Dimension(1200, 800));
		/*stats.setBackground(new Color(250,250,250));*/
		_cards.add(_stats,"Stats");

		//Initializing the button related to the gantt chart for usage.
		ganttButton = new CustomButton("Gantt Chart");

		//Initializing the buttons
		JButton graphButton = new CustomButton("Tree Graph");

		_active = graphButton;
		graphButton.setBackground(new Color(6, 47, 79));

		graphButton.setBounds(834, 15, 140, 50);
		frame.getContentPane().add(graphButton);

		//Adding an action listener for the button related to the tree graph.
		graphButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(_cards, "Graph");
				_active = graphButton;
				_notActive[0] = ganttButton;
				_notActive[1] = statButton;
				changeActive();
			}
		});


		//Giving looks to the gantt chart button
		ganttButton.setBounds(834, 76, 140, 50);
		frame.getContentPane().add(ganttButton);

		//Adding an action listener for the button related to the gantt chart.
		ganttButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(_cards, "Gantt");
				_active = ganttButton;
				_notActive[0] = graphButton;
				_notActive[1] = statButton;
				changeActive();
			}
		});

		statButton = new CustomButton("Statistics Chart");
		statButton.setText("Statistics");
		statButton.setBounds(834, 137, 140, 50);
		frame.getContentPane().add(statButton);
		statButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(_cards, "Stats");
				_active = statButton;
				_notActive[0] = graphButton;
				_notActive[1] = ganttButton;
				changeActive();
			}
		});
		/*statButton.setEnabled(false);*/

		//Creating a button for exit.
		JButton btnClose = new CustomButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run(){
						System.out.println("Shutting down all threads");	
					}
				});

				frame.dispose();
				System.exit(0);
			}
		});

		btnClose.setBounds(834, 505, 140, 50);
		frame.getContentPane().add(btnClose);

		//TODO
		infoArea = new JTextArea();
		infoArea.setText("Press the node to see the info");
		infoArea.setEditable(false);
		infoArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
		infoArea.setBounds(834, 248, 140, 246);
		infoArea.setBorder(new LineBorder(new Color(13, 90, 150), 1, true));
		infoArea.setBorder(BorderFactory.createCompoundBorder( 
				infoArea.getBorder(),  
				BorderFactory.createEmptyBorder(5, 5, 5, 5))); 
		/*Border roundedBorder = new LineBorder(new Color(13, 90, 150), 5, true);
		infoArea.setBorder(roundedBorder);*/

		infoArea.setBackground(new Color(250,250,250));
		infoArea.setLineWrap(true);
		infoArea.setWrapStyleWord(true);
		infoArea.setMargin(new Insets(5,5,5,5));

		frame.getContentPane().add(infoArea);

		txtrTask = new JTextField();
		txtrTask.setHorizontalAlignment(SwingConstants.CENTER);
		txtrTask.setFont(new Font("SansSerif", Font.BOLD, 20));
		txtrTask.setText("Task");
		txtrTask.setForeground(new Color(255, 255, 255));
		txtrTask.setBackground(new Color(150, 63, 13));
		//129, 55, 114
		txtrTask.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		txtrTask.setEditable(false);
		txtrTask.setBounds(834, 220, 140, 30);
		txtrTask.setMargin(new Insets(5,5,5,5));

		frame.getContentPane().add(txtrTask);

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
		txtrTask.setText("Task "+task);
		infoArea.setText("Processor Number: "+pNo+"\n\nStart Time: " + startTime + "\n\nEnd Time: "+endTime);

	}

	/**
	 * This method shows default info for an unscheduled task
	 * @param task
	 */
	public void noInfoToShow(String task){
		txtrTask.setText("Task "+task);
		infoArea.setText("This task is not scheduled yet");
	}

	public void schedulingFinished(){
		statButton.setEnabled(true);;
	}
}


