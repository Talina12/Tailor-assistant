package Gella.Tailor_assistant.View;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

import org.mortbay.log.Log;

import Gella.Tailor_assistant.controller.DbHandler;
import Gella.Tailor_assistant.controller.GoogleCalendarController;
import Gella.Tailor_assistant.model.Customer;
import Gella.Tailor_assistant.model.DescriptionRow;
import Gella.Tailor_assistant.model.Order;

import javax.swing.BoxLayout;

public class MainWindow2 {

	private JFrame frame;
	private Box resultsContainer;
    private JButton newOrderButton;
	private JButton updateOrderButton;
	private JLabel orderNumLabel;
	private JTextField orderNumField;
	private HintWindow hintWindow;
	private JScrollPane resultsScrollPane;
	private DbHandler dbHandler;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private Border border;
	private Fon fon;
	private int fontSize;
	private Dimension buttonSize,fieldSize,frameSize,resultConSize;
	private int frameBorder=20;
	public static Logger log = Logger.getLogger("View.MainWindow2");
	JButton synchronizeButton;
	GoogleCalendarController googleCalendarController;
	
	public MainWindow2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Uriel");
		frameSize=new Dimension(screenSize.width-frameBorder, screenSize.height-frameBorder);
		frame.setBounds(frameBorder,frameBorder,frameSize.width, frameSize.height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fon = new Fon();
		frame.setContentPane(fon);
		frame.getContentPane().setLayout(null);
		fontSize=screenSize.width/80;
		border = BorderFactory.createEtchedBorder();
		dbHandler=DbHandler.getInstance();
		googleCalendarController=GoogleCalendarController.getInstance();	
		hintWindow = new HintWindow();
		
		buttonSize= new Dimension(frame.getBounds().width/6,frame.getBounds().height/7);
		newOrderButton = new JButton("Новый заказ");
		int space=(frame.getBounds().width-buttonSize.width*3)/4;
		int x=space;
		int y=frame.getBounds().height/9;
		newOrderButton.setBounds(x,y,buttonSize.width,buttonSize.height);
		newOrderButton.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		newOrderButton.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
		    	NewOrderWindow newOrderWindow= new NewOrderWindow();
		    	newOrderWindow.setVisible(true);
			 }
		});
		updateOrderButton = new JButton("обновить заказ");
		x=x+buttonSize.width+space;
		updateOrderButton.setBounds(x, y, buttonSize.width, buttonSize.height);
		updateOrderButton.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		updateOrderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			 orderNumLabel.setVisible(true);
			 orderNumField.setText(null);
			 orderNumField.setVisible(true);
			 orderNumField.requestFocus();
			}
		});
		frame.add(newOrderButton);
		frame.add(updateOrderButton);
		
		synchronizeButton = new JButton("Синронизация");
		x=x+buttonSize.width+space;
		synchronizeButton.setBounds(x, y, buttonSize.width, buttonSize.height);
		//synchronizeButton.setBounds(x, y, buttonSize.width, buttonSize.height);
		synchronizeButton.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		synchronizeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				googleCalendarController.synchronize();
			}
		});
		frame.add(synchronizeButton);
		
		fieldSize= new Dimension(frame.getBounds().width/9,frame.getBounds().height/15);
		orderNumLabel = new JLabel("Order Number");
		orderNumLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize+2));
		x=frameSize.width/2-5-fieldSize.width;
		y=updateOrderButton.getBounds().y+buttonSize.height+frame.getBounds().height/15;
		orderNumLabel.setBounds(x,y,fieldSize.width,fieldSize.height);
		
		orderNumField = new JTextField();
		orderNumField.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		x=frameSize.width/2+5;
		orderNumField.setBounds(x,y,fieldSize.width,fieldSize.height);
		((AbstractDocument) (orderNumField.getDocument())).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)throws BadLocationException
			{
			  if (!Character.isDigit(string.charAt(0)))
					Toolkit.getDefaultToolkit().beep();
			  else 
				  fb.insertString( offset, string, attr );
			  }
			
			@Override
			public void replace(FilterBypass fb, int offset, int length, String string,AttributeSet attr) throws BadLocationException
			{
			 if (string==null) fb.remove(0,length);
			 else
			 {
				if (!Character.isDigit(string.charAt(0)))
					Toolkit.getDefaultToolkit().beep();
			  else
				fb.insertString( offset, string, attr );
			 }
					}
			} );
		orderNumField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resultsScrollPane.setVisible(false);
				setResultContainer(dbHandler.getOrderById(Integer.parseInt(orderNumField.getText())));
				resultsScrollPane.setVisible(true);
			    orderNumField.setText(null);
				}
        });
		
		frame.add(orderNumLabel);
		frame.add(orderNumField);
		orderNumLabel.setVisible(false);
		orderNumField.setVisible(false);
		 
		resultsContainer = new Box(BoxLayout.Y_AXIS);
		resultConSize= new Dimension(frameSize.width/2,Math.toIntExact((Math.round(frameSize.height/2.3))));
		x=frameSize.width/2-resultConSize.width/2;
		y=orderNumField.getBounds().y+fieldSize.height+frameSize.height/15;
		resultsContainer.setBounds(x,y,resultConSize.width, resultConSize.height);
		resultsContainer.setBorder(border);
		resultsContainer.setBackground(new Color(10,20,30,40));
		resultsScrollPane = new JScrollPane(resultsContainer);
		resultsScrollPane.setBounds(x,y,resultConSize.width, resultConSize.height);
		frame.add(resultsScrollPane);
		resultsScrollPane.setVisible(false);
		}

	public JFrame getFrame() {
		return frame;
	}

	private void setResultContainer(final Order order) {
	    String htmlDes="<ul>";
	    ArrayList<DescriptionRow> des=order.getDescription();
	    log.info(des.toString());
	    for (DescriptionRow d:des)
	    	log.info(d.getItem()+"  "+d.getPrice());
	    for (DescriptionRow d:des) 
	    	htmlDes=htmlDes.concat("<li>"+d.getItem()+"  "+d.getPrice()+"</li>"); 
	    htmlDes=htmlDes.concat("</ul>");
	    String html=
		"<html><body>"
			   + "<p><h4>"+order.getCustomer().getFirstName()+"   "+order.getCustomer().getLastName()+"</h4></p>"
			   +"<div>"
				+"<table>"
				  +"<tr>"
				   +"<td style='width:50%'>"
				   	 + "<ul>"
		               +"<li>Id: "+ order.getOrderNumber()+"</li>"
		               + "<li>"+order.getCustomer().getCellphone()+"  "+order.getCustomer().getHomePhone()+"</li>"
		               + "<li>Total price: "+order.getTotalPrice()+"</li>"
		               + "<li>Issue date: "+order.getIssueDate()+  "</li>"
		             +"</ul>"
		           +"</td>"
				   +"<td>" +htmlDes + "</td>"
				  + "</tr>"
				+"</table>"
		       +"</div>"
			+ "</body>";
	 JButton orderBut= new JButton(html);
	 orderBut.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
	 orderBut.setMinimumSize(new Dimension(screenSize.width/2,screenSize.height/4 ));
	 orderBut.setMaximumSize(new Dimension(screenSize.width/2,screenSize.height/4 ));
	 orderBut.setPreferredSize(new Dimension(screenSize.width/2,screenSize.height/4 ));
	 orderBut.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			UpdateOrderWindow updateOrderWindow = new UpdateOrderWindow(order);
			updateOrderWindow.setVisible(true);
			resultsContainer.removeAll();
			resultsScrollPane.setVisible(false);
			orderNumLabel.setVisible(false);
			orderNumField.setVisible(false);
			}
	});
	 resultsContainer.add(orderBut);
	 log.info(String.valueOf(resultsContainer.getComponentCount()));
	 }
	
	 
}
