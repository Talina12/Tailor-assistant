package Gella.Tailor_assistant.View;

import java.awt.Dimension;
import javax.swing.*;
import javax.swing.table.TableModel;


public class HintWindow extends JWindow {
	private JTable hintTable;
	
	public HintWindow() {
     setBounds(200, 200, 300, 250);
	 //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 hintTable  = new JTable();
	 hintTable.setPreferredSize(new Dimension(100,100));
	 this.getContentPane().add(new JScrollPane(hintTable));
     
	}

	public JTable getHintTable() {
		return hintTable;
	}

	public void setTableModel(TableModel tableModel) {
		hintTable.setModel(tableModel);
	}

	
}
