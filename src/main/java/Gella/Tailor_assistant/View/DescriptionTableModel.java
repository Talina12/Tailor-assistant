package Gella.Tailor_assistant.View;

import java.util.ArrayList;
//import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
//import javax.swing.table.DefaultTableModel;
import java.lang.Number;
import Gella.Tailor_assistant.model.*;

/**
 * data model for description table in new order window
 * @author Gella
 *
 */
public class DescriptionTableModel extends AbstractTableModel{
	private ArrayList <DescriptionRow> data;
	
	public DescriptionTableModel() {
	    data =new ArrayList<DescriptionRow>();
		data.add(new DescriptionRow());
		}
	
	/** Creates a new object with data from the description. if null is passed creates new empty object*/
	public DescriptionTableModel (ArrayList<DescriptionRow>  description) {
		if (description!= null) data =description;
		else data =new ArrayList<DescriptionRow>();
		if (data.size()==0) data.add(new DescriptionRow());
	}

	

	@Override
	public Class<?> getColumnClass(int column)  {
		try {
			return DescriptionRow.getFieldClass(column);
			} 
		catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			}
		}
	
	@Override
	public int getRowCount() {return data.size();}
	
	@Override
	public int getColumnCount() { return DescriptionRow.getFieldCount();}
	
	@Override
	public Object getValueAt(int row, int column) {
	      try {
			return data.get(row).getField(column);
			} 
	      catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	    }
	@Override
	public String getColumnName(int column) {
	      return DescriptionRow.getTitles()[column];
	    }
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		try {
			data.get(row).setField(col,value);
		} catch (NoSuchFieldException e) {
			JOptionPane.showMessageDialog(null,e.getClass().toString()+"Error in DescriptionTableModel.setValueAt");
			e.printStackTrace();
		}
		//if a value is entered in the last column of the last row, then we add a new row
		if ((row==data.size()-1)&(col==DescriptionRow.getFieldCount()-1)) {
			data.add(new DescriptionRow());
			fireTableRowsInserted(data.size()-1,data.size()-1);
			}
		else
		fireTableCellUpdated( row, col);
		
}
	
	@Override
	public boolean isCellEditable(int row, int col) { return true; }
	
	/**
	 * returns the sum of the column 
	 * @param column number of column to sum
	   @throws NoSuchFieldException if there is no such column
	 * @throws UnsupportedOperationException if  the column is not of numeric type
	 */
	public double getSumColumn(int column) throws NoSuchFieldException,UnsupportedOperationException {
		if (java.lang.Number.class.isAssignableFrom(DescriptionRow.getFieldClass(column))) {
			double sum = 0;
			for (DescriptionRow d :data) 
				if(d.getField(column)!=null) sum +=((Number) d.getField(column)).doubleValue(); //summarize by skipping empty cells
			return sum;	
		}
		    
		else throw new UnsupportedOperationException("Non-numeric type of field "); 
	}
	
	/**
	 * add an empty row at the end of the table
	 */
	public void addRow() {
     data.add(new DescriptionRow());
	 fireTableRowsInserted(data.size()-1,data.size()-1);
	 fireTableDataChanged(  );
	 }
	
	public ArrayList<DescriptionRow> getData() {
		return data;
	}
	
}
