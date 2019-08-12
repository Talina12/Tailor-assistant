package Gella.Tailor_assistant.View;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import Gella.Tailor_assistant.model.*;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import Gella.Tailor_assistant.controller.*;


/**
 *  root screen of the application
 * @author Gella
 *
 */
public class MainWindow {

	private JFrame frame;
	private JTextArea test;
	private DbHandler dbHandler;
	private JButton newOrderButton;
	private JButton ordersButton;
	private JButton customersButton;
	private JButton descriptionsButton;
	
	public MainWindow() {
		initialize();
		}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		Fon fon = new Fon();
		frame.setContentPane(fon);
		frame.setBounds(10, 10,1350, 700);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    newOrderButton = new JButton("New order");
		newOrderButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		newOrderButton.setBounds(487, 23, 359, 100);
		newOrderButton.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
		    	dbHandler.resetInfo();
		    	NewOrderWindow newOrderWindow= new NewOrderWindow();
		    	newOrderWindow.setVisible(true);
			 }
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(newOrderButton);
		
		ordersButton = new JButton("Orders");
		ordersButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ordersButton.setBounds(167, 227, 223, 74);
		ordersButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			TableWindow ordersWindow = new TableWindow(dbHandler.getArrayOfOrders(),Order.getTitles());
			ordersWindow.setVisible(true);
			}
		});
		frame.getContentPane().add(ordersButton);
		
		customersButton = new JButton("Customers");
		customersButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		customersButton.setBounds(556, 227, 223, 74);
		customersButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				///String[][] s =dbHandler.getArrayOfCustomers();
				//System.out.println(s.toString());
			//	System.out.println(s.length);
			TableWindow customersWindow = new TableWindow(dbHandler.getArrayOfCustomers(),Customer.getTitles());
			 customersWindow.setVisible(true);
			}
		});
		frame.getContentPane().add(customersButton);
		
		descriptionsButton = new JButton("Descriptions");
		descriptionsButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		descriptionsButton.setBounds(945, 227, 223, 74);
		descriptionsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			String[] desTitles=	Arrays.copyOf(DescriptionRow.getTitles(), DescriptionRow.getTitles().length+1);
			desTitles[desTitles.length-1]="id";
			TableWindow descriptionsWindow = new TableWindow(dbHandler.getArrayOfDescriptions(),desTitles);
			descriptionsWindow.setVisible(true);
			}
		});
		frame.getContentPane().add(descriptionsButton,BorderLayout.CENTER);
		
		test = new JTextArea();
		test.setBounds(294, 419, 745, 177);
		frame.getContentPane().add(test);
		
		JButton testButton = new JButton("Show DbHandler.info");
		testButton.setBounds(591, 611, 151, 23);
		testButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			test.setText(dbHandler.getInfo());
			}
		});
		fon.add(testButton);
		
		
		
		try {
			dbHandler = DbHandler.getInstance();
		} catch (SQLException e) {
			System.out.println("error dbHandler");
			e.printStackTrace();
		}
	}

	public JFrame getFrame() {
		return frame;
	}
}
