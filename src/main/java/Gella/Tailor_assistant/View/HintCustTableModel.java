package Gella.Tailor_assistant.View;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import Gella.Tailor_assistant.model.*;

/**
 * data model for customer hint table
 * @author Gella
 *
 */
public class HintCustTableModel extends AbstractTableModel {
 private ArrayList<Customer> data;
 private int columnCount;
 
 HintCustTableModel(){
	 data= new ArrayList<Customer>();
	 columnCount=1;
 }

public HintCustTableModel(ArrayList<Customer> data) {
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
	String str = data.get(row).getFirstName()+"  "+data.get(row).getLastName()+
			"  "+data.get(row).getCellphone()+"  "+data.get(row).getHomePhone();
	return str;
}

public void setData(ArrayList<Customer> data) {
	this.data = data;
}
 
 public Customer getCustomer(int i) {
	 try {
	 return data.get(i);
	 }
	 catch (IndexOutOfBoundsException e) {
	 return null;}
 }
}
