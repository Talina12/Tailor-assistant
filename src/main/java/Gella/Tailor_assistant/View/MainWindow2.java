package Gella.Tailor_assistant.View;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
import Gella.Tailor_assistant.model.Settings;

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
	private JLabel firstNameLabel;
	private JTextField firstNameField;
	private JLabel lastNameLabel;
	private JTextField lastNameField;
	private JLabel cellPhoneLabel;
	private JTextField cellphoneField;
	private JLabel homePhoneLabel;
	private JTextField homePhoneField;
	
	
	public MainWindow2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame(Settings.getUserName());
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
		newOrderButton = new JButton("New order");
		int space=(frame.getBounds().width-buttonSize.width*3)/4;
		int x=space;
		int y=frame.getBounds().height/20;
		newOrderButton.setBounds(x,y,buttonSize.width,buttonSize.height);
		newOrderButton.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		newOrderButton.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
		    	NewOrderWindow newOrderWindow= new NewOrderWindow();
		    	newOrderWindow.setVisible(true);
			 }
		});
		updateOrderButton = new JButton("Update order");
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
			 firstNameLabel.setVisible(true);
			 firstNameField.setVisible(true);
			 firstNameField.setText(null);
			 lastNameField.setVisible(true);
			 lastNameField.setText(null);
			 lastNameLabel.setVisible(true);
			 cellPhoneLabel.setVisible(true);
			 cellphoneField.setText(null);
			 cellphoneField.setVisible(true);
			 homePhoneLabel.setVisible(true);
			 homePhoneField.setText(null);
			 homePhoneField.setVisible(true);
			 resultsContainer.removeAll();
			 resultsScrollPane.setVisible(false);
			}
		});
		frame.add(newOrderButton);
		frame.add(updateOrderButton);
		
		synchronizeButton = new JButton("Synchronize");
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
		
		fieldSize= new Dimension(frame.getBounds().width/9,frame.getBounds().height/17);
		orderNumLabel = new JLabel("Order Number");
		orderNumLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize+2));
		x=frameSize.width/2-5-fieldSize.width;
		y=updateOrderButton.getBounds().y+buttonSize.height+frame.getBounds().height/26;
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
		
		firstNameLabel = new JLabel("First name");
		firstNameLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize+2));
		x=(frameSize.width-fieldSize.width*4)/5;
		y=orderNumField.getBounds().y+fieldSize.height+frame.getBounds().height/34;
		firstNameLabel.setBounds(x,y,fieldSize.width,fieldSize.height);
		frame.add(firstNameLabel);
		firstNameLabel.setVisible(false);
		
		y=y+fieldSize.height+5;
		firstNameField= new JTextField ();
		firstNameField.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		firstNameField.setBounds(x,y,fieldSize.width,fieldSize.height);
		firstNameField.addFocusListener(textFieldFocusAdapter);
		firstNameField.addCaretListener(new CaretListener(){
			@Override
			public void caretUpdate(CaretEvent e) {
			if (firstNameField.isFocusOwner()	){
			 String str = firstNameField.getText();
			 setHintWindow(dbHandler.getCustomersByFirstName(str),firstNameField);
			 }
	        }	
			});
		firstNameField.addKeyListener(textFieldKeyAdapter);
		firstNameField.addActionListener(textFieldActionListener);
		frame.add(firstNameField);
		firstNameField.setVisible(false);
		
		lastNameLabel = new JLabel("Last name");
		lastNameLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize+2));
		x=x+(frameSize.width-fieldSize.width*4)/5+fieldSize.width;
		lastNameLabel.setBounds(x,firstNameLabel.getBounds().y,fieldSize.width,fieldSize.height);
		frame.add(lastNameLabel);
		lastNameLabel.setVisible(false);
		
		lastNameField= new JTextField ();
		lastNameField.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		lastNameField.setBounds(x,firstNameField.getBounds().y,fieldSize.width,fieldSize.height);
		lastNameField.addFocusListener(textFieldFocusAdapter);
		lastNameField.addCaretListener(new CaretListener(){
			@Override
			public void caretUpdate(CaretEvent e) {
			if (lastNameField.isFocusOwner()	){
			 String str = lastNameField.getText();
			 setHintWindow(dbHandler.getCustomersByFirstName(str),lastNameField);
			 }
	        }	
			});
		lastNameField.addKeyListener(textFieldKeyAdapter);
		lastNameField.addActionListener(textFieldActionListener);
		frame.add(lastNameField);
		lastNameField.setVisible(false);
		
		cellPhoneLabel = new JLabel("Cellphone");
		cellPhoneLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize+2));
		x=lastNameField.getBounds().x+fieldSize.width+(frameSize.width-fieldSize.width*4)/5;
		cellPhoneLabel.setBounds(x,firstNameLabel.getBounds().y,fieldSize.width,fieldSize.height);
		frame.add(cellPhoneLabel);
		cellPhoneLabel.setVisible(false);
		
		cellphoneField= new JTextField ();
		cellphoneField.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		cellphoneField.setBounds(x,firstNameField.getBounds().y,fieldSize.width,fieldSize.height);
		((AbstractDocument) (cellphoneField.getDocument())).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)throws BadLocationException
			{
			  if (!Character.isDigit(string.charAt(0)))
					Toolkit.getDefaultToolkit().beep();
			  else
			     if (offset==3||offset==7||offset==10)
				    fb.insertString( offset, '-'+string, attr );
				 else 
					 if (offset<=12)
					  fb.insertString( offset, string, attr );
			  
			}
			
			@Override
			public void replace(FilterBypass fb, int offset, int length, String string,AttributeSet attr) throws BadLocationException
			{
				  if (string==null) fb.remove(0,length);
				  else {
				  if (!Character.isDigit(string.charAt(0)))
						Toolkit.getDefaultToolkit().beep();
				  else
					if (offset==3||offset==7||offset==10)
					    fb.insertString( offset, '-'+string, attr );
					else 
						if (offset<=12)
						fb.insertString( offset, string, attr );
				        }
						}
					} );
		cellphoneField.setColumns(10);
		cellphoneField.addFocusListener(textFieldFocusAdapter);
		cellphoneField.addCaretListener(new CaretListener () {
			@Override
			public void caretUpdate(CaretEvent e) {
				if (cellphoneField.isFocusOwner()) {
				String str = cellphoneField.getText();
				setHintWindow(dbHandler.getCustomersByCellphone(str),cellphoneField);
				}
				}	
			}	);
		cellphoneField.addKeyListener(textFieldKeyAdapter);
		cellphoneField.addActionListener(textFieldActionListener);
		frame.add(cellphoneField);
		cellphoneField.setVisible(false);
		
		homePhoneLabel = new JLabel("Home phone");
		homePhoneLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize+2));
		x=x+fieldSize.width+(frameSize.width-fieldSize.width*4)/5;
		homePhoneLabel.setBounds(x,firstNameLabel.getBounds().y,fieldSize.width,fieldSize.height);
		
		frame.add(homePhoneLabel);
		homePhoneLabel.setVisible(false);
		
		homePhoneField= new JTextField ();
		homePhoneField.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		homePhoneField.setBounds(x,firstNameField.getBounds().y,fieldSize.width,fieldSize.height);
		((AbstractDocument) (homePhoneField.getDocument())).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)throws BadLocationException
			{
			  if (Character.isLetter(string.charAt(0)))
					Toolkit.getDefaultToolkit().beep();
			  else
			     if (offset==3||offset==7||offset==10)
				    fb.insertString( offset, '-'+string, attr );
				 else 
					 if (offset<=12)
					  fb.insertString( offset, string, attr );
			  
			}
			
			@Override
			public void replace(FilterBypass fb, int offset, int length, String string,AttributeSet attr) throws BadLocationException
			{
			  if (string==null) fb.remove(0,length);
			  else {
				if (Character.isLetter(string.charAt(0)))
					Toolkit.getDefaultToolkit().beep();
			  else
				if (offset==2||offset==6||offset==9)
				    fb.insertString( offset, '-'+string, attr );
				else 
					if (offset<=11)
					fb.insertString( offset, string, attr );
			  }
					}
					} );
		homePhoneField.setColumns(10);
		homePhoneField.addFocusListener(textFieldFocusAdapter);
		homePhoneField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if (homePhoneField.isFocusOwner()) {
					String str = homePhoneField.getText();
					setHintWindow(dbHandler.getCustomersByHomePhone(str),homePhoneField);
					}
				}	
			});
		homePhoneField.addKeyListener(textFieldKeyAdapter);
		homePhoneField.addActionListener(textFieldActionListener);
		frame.add(homePhoneField);
		homePhoneField.setVisible(false);
		 
		resultsContainer = new Box(BoxLayout.Y_AXIS);
		resultConSize= new Dimension(frameSize.width/2,Math.toIntExact((Math.round(frameSize.height/2.3))));
		x=frameSize.width/2-resultConSize.width/2;
		y=firstNameLabel.getBounds().y+fieldSize.height+frameSize.height/10;
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
			
			firstNameLabel.setVisible(false);
			firstNameField.setVisible(false);
			lastNameField.setVisible(false);
			lastNameLabel.setVisible(false);
			cellPhoneLabel.setVisible(false);
			cellphoneField.setVisible(false);
			homePhoneLabel.setVisible(false);
			homePhoneField.setVisible(false);
			}
	});
	 resultsContainer.add(orderBut);
	 log.info(String.valueOf(resultsContainer.getComponentCount()));
	 }
	 /**
     * sets hintWindow parameters and data for display
     * @param d data to display in hintWindow
     * @param tf field associated with the hint
     */
	private void setHintWindow(ArrayList<Customer> d, JTextField tf) {
      if (d.size()>0) {
    	    Point point;
			hintWindow.getHintTable().setModel(new HintCustTableModel(d));
			point = tf.getLocationOnScreen();
			hintWindow.getHintTable().setPreferredSize(new Dimension(200, d.size()*18));
			hintWindow.getHintTable().changeSelection(0, 0, false, false);
			int x= Math.toIntExact(Math.round(point.getX()));
			int y = Math.toIntExact(Math.round(point.getY()));
			hintWindow.setBounds(x,y+25,300,d.size()*18+20);
			hintWindow.setVisible(true); 
			}
			else hintWindow.setVisible(false);	
    }	
	private FocusAdapter textFieldFocusAdapter = new FocusAdapter() {
		public void focusLost(FocusEvent e) {
			hintWindow.setVisible(false);
			}
	};	
	private KeyAdapter textFieldKeyAdapter = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			int i;
			if (hintWindow.isVisible()) {
			switch (e.getKeyCode()) {
			 case (KeyEvent.VK_DOWN):
			 i=hintWindow.getHintTable().getSelectedRow();
			 if (i<hintWindow.getHintTable().getRowCount()-1) 
			  hintWindow.getHintTable().changeSelection(i+1, 0,false, false);
			else hintWindow.getHintTable().changeSelection(0, 0,false, false);
			 break;
			case (KeyEvent.VK_UP):
			i=hintWindow.getHintTable().getSelectedRow();
			if (i>0)
			 hintWindow.getHintTable().changeSelection(i-1, 0,false, false);	
			else hintWindow.getHintTable().changeSelection(hintWindow.getHintTable().getRowCount()-1, 0,false, false);
			case (KeyEvent.VK_ENTER):
		     if(hintWindow.getHintTable().getModel() instanceof HintCustTableModel) {
		    	 Customer c;
				 c=((HintCustTableModel) hintWindow.getHintTable().getModel()).getCustomer(hintWindow.getHintTable().getSelectedRow());
				 setCustomer(c);
				 ArrayList<Order> orders=dbHandler.getOrdersByCustId(c.getId());
				 for(Order o:orders) 
				  setResultContainer(o);
				 resultsScrollPane.setVisible(true);
				}
				  else {log.warning("HintTableModel is not instance of HintCustTableModel");
				  hintWindow.setVisible(false);}
			break;
			}
			}
	    }
	};
	
	private ActionListener textFieldActionListener = new ActionListener() {
        @Override
		public void actionPerformed(ActionEvent arg0) {
	    	resultsScrollPane.requestFocus();
        	}
	};	
	/**
	 * enters customer data in the appropriate form fields
	 * @param c
	 */
	 protected void setCustomer(Customer c) {
		if (c!=null) {
			firstNameField.setText(c.getFirstName());
			lastNameField.setText(c.getLastName());
			cellphoneField.setText(null);
			cellphoneField.setText(c.getCellphone());
			homePhoneField.setText(null);
			homePhoneField.setText(c.getHomePhone());
			}
		}	
}
