package Gella.Tailor_assistant.View;

//import java.awt.BorderLayout;
import java.awt.Dimension;
//import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Font;

public class TableWindow extends JFrame {

	private JPanel contentPane;
	private JTable ordersTable;

	
	public TableWindow(String[][] data, String[]titles) {
		setBounds(100, 100, 1100, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ordersTable = new JTable(data,titles);
		ordersTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
		ordersTable.setPreferredSize(new Dimension(800, 600));
		ordersTable.setCellSelectionEnabled(true);
		JScrollPane jScrollDes = new JScrollPane(ordersTable);
		jScrollDes.setBounds(10, 10, 1064, 440);
		contentPane.add(jScrollDes);
	}
}
