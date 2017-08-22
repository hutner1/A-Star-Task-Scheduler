package visualization.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class StatisticTable extends JPanel{

	private JTable _table;
	private final static String[] _columns = { "Statistics", "No." };
	/*	private int _solCreated;
	private int _solPopped;
	private int _solPruned;
	private int _timeTaken;
	private Integer _optimalFinishTime;*/


	/**
	 * Constructor for the statistics table
	 * @param Core
	 */
	public StatisticTable(int Core){

		setLayout(new BorderLayout());
		/*setBackground(new Color(250,250,250));*/

		Object[][] data = new Object[7][2];

		data[0][0] = "Number of cores";
		data[0][1] = Core;
		data[1][0] = "Solutions created";
		data[1][1] = "Processing...";
		data[2][0] = "Solutions popped";
		data[2][1] = "Processing...";
		data[3][0] = "Solutions pruned";
		data[3][1] = "Processing...";
		data[4][0] = "Memory usage (MB)";
		data[4][1] = "Processing...";
		data[5][0] = "Time taken(ms)";
		data[5][1] = "Processing...";
		data[6][0] = "Current Optimal Solution Time";
		data[6][1] = "Processing...";

		_table = new JTable(new Model(data, _columns));
		_table.getTableHeader().setReorderingAllowed(false);
		_table.getTableHeader().setResizingAllowed(false);
		_table.setRowSelectionAllowed(false);
/*		_table.getColumnModel().getColumn(0).setPreferredWidth(200);
		_table.getColumnModel().getColumn(1).setPreferredWidth(200);*/
		_table.setFont(new Font("SansSerif", Font.PLAIN, 18));
		_table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 20));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        _table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		
		for (int row = 0; row < _table.getRowCount(); row++){
	        int rowHeight = _table.getRowHeight();
	        
	        for (int column = 0; column < _table.getColumnCount(); column++){
	            Component component = _table.prepareRenderer(_table.getCellRenderer(row, column), row, column);
	            rowHeight = Math.max(rowHeight, component.getPreferredSize().height);
	        }
	        _table.setRowHeight(row, rowHeight);
	    }

		JScrollPane scroll = new JScrollPane(_table);
		/*scroll.setBackground(new Color(250,250,250));*/
		add(scroll);
	}


	/**
	 * Method to update all the statistics in the table
	 * @param solCreated
	 * @param solPopped
	 * @param solPruned
	 * @param time
	 */
	public void updateStats(int solCreated, int solPopped, int solPruned, int time){
		updateStats(solCreated, solPopped, solPruned, time, null);
	}

	/**
	 * Method to update all the statistics in the table with finish time
	 * @param solCreated
	 * @param solPopped
	 * @param solPruned
	 * @param time
	 * @param finishTime
	 */
	public void updateStats(int solCreated, int solPopped, int solPruned, int time, Integer finishTime){

		/*		_solCreated = solCreated;
		_solPopped = solPopped;
		_solPruned = solPruned;
		_timeTaken = time;
		_optimalFinishTime = finishtime;*/

		DefaultTableModel model = (DefaultTableModel)_table.getModel();

		model.setValueAt(solCreated, 1, 1);
		model.setValueAt(solPopped, 2, 1);
		model.setValueAt(solPruned, 3, 1);
		model.setValueAt((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024, 4, 1);
		
		if(!(time < 1)){
			model.setValueAt(time, 5, 1);
		}
		
		if(finishTime != null){
			model.setValueAt(finishTime, 6, 1);
		}

	}

	@SuppressWarnings("serial")
	private class Model extends DefaultTableModel {

		Model(Object[][] data, String[] column) {
			super(data, column);
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}
	
	public JTable getTable(){
		
		return _table;
	}

}
