package Gella.Tailor_assistant.View;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import Gella.Tailor_assistant.model.*;

/**
 * data model for descriptions hint table
 * @author Gella
 *
 */
public class HintDesTableModel extends AbstractTableModel{
	private ArrayList<DescriptionRow> data;
	 private int columnCount;
	 
	 HintDesTableModel(){
		 data= new ArrayList<DescriptionRow>();
		 columnCount=1;
	 }

	public HintDesTableModel(ArrayList<DescriptionRow> data) {
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
		String str = data.get(row).getItem()+"  "+data.get(row).getPrice();
		return str;
	}

	public void setData(ArrayList<DescriptionRow> data) {
		this.data = data;
	}
	 
	 public DescriptionRow getDescriptionRow(int i) {
		 try {
		 return data.get(i);
		 }
		 catch (IndexOutOfBoundsException e) {
		 return null;}
	 }

}
