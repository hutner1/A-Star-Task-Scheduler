package visualization.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JSeparator;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * Generates a panel containing the legend for the graph produced by the Visualizer class. 
 * 
 * The legend labels colours based on the processors that will be carrying out the tasks. 
 * The colour of the nodes on the graph indicates which processor a certain task is ran on.
 *
 */
public class Legend extends JPanel {
	private int _proNo;
	private int _height = 0;


	/**
	 * Create the legend panel.
	 */
	public Legend(int proccesorNum) {
		_proNo = proccesorNum;
		setLayout(null);

		//The title for legends
		JTextField title = new JTextField();
		title.setForeground(new Color(255, 255, 255));
		title.setBackground(new Color(51, 102, 255));
		title.setEditable(false);
		title.setText("Processors");
		title.setFont(new Font("SansSerif", Font.BOLD, 17));
		title.setBounds(0, 0, 120, 50);
		title.setHorizontalAlignment(JTextField.CENTER);
		title.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); 
		add(title);


		//Setting up the panel to contain the labels.
		JPanel content = new JPanel();

		JScrollPane scroll = new JScrollPane(content);
		content.setLayout(new FlowLayout(FlowLayout.LEFT));

		scroll.setBounds(0, 50, 120, 333);
		scroll.setBorder(new LineBorder(Color.black));
		add(scroll);

		//Adding the legends into the content panel
		ArrayList<JPanel> legends = new ArrayList<JPanel>();
		for(int i=0; i<_proNo;i++){
			legends.add(createLegends(i));
			_height+=30;
			content.add(legends.get(i));
		}

		content.setPreferredSize(new Dimension(90,_height+100));


		//Adding help text to allow users to understand what is being shown on the tree graph.
		JTextArea help = new JTextArea();
		help.setLineWrap(true);
		help.setEditable(false);
		help.setWrapStyleWord(true);
		help.setText("\n** The processors are where each task is assigned to.");
		help.setFont(new Font("SansSerif", Font.ITALIC, 15));
		help.setBounds(0, 380, 120, 160);
		help.setBorder(new LineBorder(Color.black));
		help.setBorder(BorderFactory.createCompoundBorder( 
				help.getBorder(),  
				BorderFactory.createEmptyBorder(5, 5, 5, 5))); 
		add(help);
	}

	/**
	 * This method creates a JPanel that contains the legend 
	 * label with colored panel and the text.
	 * @param currentProcessor
	 * @return JPanel
	 */
	private JPanel createLegends(int currentProcessor){
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(90,30));

		JPanel coloredPanel = new JPanel();
		Color color = getColor(currentProcessor);
		coloredPanel.setBackground(color);
		coloredPanel.setBounds(5, 6, 10, 10);

		panel.add(coloredPanel);

		JTextField text = new JTextField();
		text.setEditable(false);
		text.setFont(new Font("SansSerif", Font.BOLD, 10));
		text.setText("Processor "+(currentProcessor+1));
		text.setBounds(25, 0, 90, 20);
		text.setBorder(BorderFactory.createEmptyBorder());
		panel.add(text);

		return panel;
	}

	/**
	 * Method to get the color for correct legend/label
	 * @param index
	 * @return
	 */
	public Color getColor(int index){
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(new Color(231, 71, 60));   //dark red
		colors.add(new Color(255, 195, 0));  // yellow     
		colors.add(new Color(29, 131, 72));  // dark green
		colors.add( new Color(142, 68, 173));  //  purple
		colors.add( new Color(40, 116, 166));  // navy blue
		colors.add( new Color(230, 126, 34));  // orange
		colors.add( new Color(93, 109, 126));  // grey
		colors.add( new Color(69, 179, 157)); //mint
		colors.add( new Color(174, 214, 241)); //light light blue
		colors.add (new Color(217, 252, 103)); //greenish yellow
		colors.add( new Color(204, 92, 146)); //magenta
		colors.add( new Color(240, 160, 160)); //peach

		return colors.get(index);
	}
}
