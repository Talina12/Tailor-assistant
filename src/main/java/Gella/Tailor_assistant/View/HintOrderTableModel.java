package Gella.Tailor_assistant.View;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import Gella.Tailor_assistant.model.Order;

public class HintOrderTableModel extends AbstractTableModel{
    private ArrayList<Order> data;
	private int columnCount;
	
	public HintOrderTableModel() {
		data= new ArrayList<Order>();
		columnCount=1;
	}
	
	public HintOrderTableModel(ArrayList<Order> data ) {
		this.data=data;
		columnCount=1;
	}
	@Override
	public int getColumnCount() {
		return columnCount;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col>columnCount-1) {
			try {
				throw new NoSuchFieldException("HintTableModel.getValueAt exeption");
			} catch (NoSuchFieldException e) {
				JOptionPane.showMessageDialog(null,e.getClass().toString()+"Error in HintTableModel.getValueAt");
				e.printStackTrace();
			}}
		String str = Integer.toString(data.get(row).getOrderNumber());
		return str;
	}

}
