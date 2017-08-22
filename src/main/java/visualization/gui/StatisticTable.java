package visualization.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class StatisticTable extends JPanel{

	private JTable _table;
	private final static String[] _columns = { "Statistics", "No." };
/*	private int _solCreated;
	private int _solPopped;
	private int _solPruned;
	private int _timeTaken;
	private Integer _optimalFinishTime;*/


	public StatisticTable(int Core){
		
		setLayout(new BorderLayout());
		/*setBackground(new Color(250,250,250));*/
		
		Object[][] data = new Object[7][2];
		
		data[0][0] = "Number of cores";
		data[0][1] = Core;
		data[1][0] = "Solutions created";
		data[1][1] = 0;
		data[2][0] = "Solutions popped";
		data[2][1] = 0;
		data[3][0] = "Solutions pruned";
		data[3][1] = 0;
		data[4][0] = "Memory usage (MB)";
		data[4][1] = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024;
		data[5][0] = "Time taken so far (ms)";
		data[5][1] = 0;
		data[6][0] = "Optimal Finish Time";
		data[6][1] = "infinity";

		_table = new JTable(new Model(data, _columns));
		_table.getTableHeader().setReorderingAllowed(false);
		_table.getTableHeader().setResizingAllowed(false);
		_table.setRowSelectionAllowed(false);
		_table.getColumnModel().getColumn(0).setPreferredWidth(200);
		_table.getColumnModel().getColumn(1).setPreferredWidth(200);
		
		JScrollPane scroll = new JScrollPane(_table);
		/*scroll.setBackground(new Color(250,250,250));*/
		add(scroll);
	}


	public void updateStats(int solCreated, int solPopped, int solPruned, int time){
		updateStats(solCreated, solPopped, solPruned, time, null);
	}

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
		model.setValueAt(time, 4, 1);
		model.setValueAt(finishTime, 5, 1);
		
		if(finishTime != null){
			model.setValueAt(finishTime, 6, 1);
		}
		
	}

	@SuppressWarnings("serial")
	private class Model extends DefaultTableModel {

		Model(Object[][] data, String[] column) {
			super(data, column);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}

}
