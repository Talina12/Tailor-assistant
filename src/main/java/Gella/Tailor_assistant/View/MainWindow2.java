package Gella.Tailor_assistant.View;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import Gella.Tailor_assistant.model.Customer;
import Gella.Tailor_assistant.model.DescriptionRow;
import Gella.Tailor_assistant.model.Order;

import javax.swing.BoxLayout;

public class MainWindow2 {

	private JFrame frame;
	private Box buttonsContainer ;
	private JPanel fieldsContainer;
	private Box resultsContainer;
    private JButton newOrderButton;
	private JButton updateOrderButton;
	private JLabel orderNumLabel;
	private JTextField orderNumField;
	private HintWindow hintWindow;
	DbHandler dbHandler;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Border border;
	Fon fon;
	int fontSize;
	
	public MainWindow2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(20,10,screenSize.width-40, screenSize.height-60);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fon = new Fon();
		//fon.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		frame.setContentPane(fon);
		fontSize=screenSize.width/95;
		border = BorderFactory.createEtchedBorder();
		dbHandler=DbHandler.getInstance();
		hintWindow = new HintWindow();
		
		buttonsContainer = new Box(BoxLayout.X_AXIS);
		buttonsContainer.setMinimumSize(new Dimension((int) (screenSize.width/4+40),screenSize.height/17+40 ));
		buttonsContainer.setMaximumSize(new Dimension((int) (screenSize.width/2+40),screenSize.height/10+40 ));
		buttonsContainer.setPreferredSize(new Dimension((int) (screenSize.width/2+40),screenSize.height/10+40 ));
		buttonsContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		//buttons.setBackground(new Color(0,0,0,0));
		newOrderButton = new JButton("New order");
		newOrderButton.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
		    	NewOrderWindow newOrderWindow= new NewOrderWindow();
		    	newOrderWindow.setVisible(true);
			 }
		});
		updateOrderButton = new JButton("Update order");
		updateOrderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			 fieldsContainer.setVisible(true);
			 orderNumField.requestFocus();
			}
		});
		setButton(newOrderButton,buttonsContainer);
		setButton(updateOrderButton, buttonsContainer);
		frame.getContentPane().add(buttonsContainer);
		fieldsContainer = new JPanel();
		fieldsContainer.setMinimumSize(new Dimension((int) (screenSize.width/4),screenSize.height/17 ));
		fieldsContainer.setMaximumSize(new Dimension((int) (screenSize.width/2),screenSize.height/10 ));
		fieldsContainer.setPreferredSize(new Dimension((int) (screenSize.width/2),screenSize.height/10 ));
		fieldsContainer.setBorder(border);
		orderNumLabel = new JLabel("Order Number");
		orderNumField = new JTextField();
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
			  if (!Character.isDigit(string.charAt(0)))
					Toolkit.getDefaultToolkit().beep();
			  else
				fb.insertString( offset, string, attr );
					}
					} );
		orderNumField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setResultContainer(dbHandler.getOrderById(Integer.parseInt(orderNumField.getText())));	}
        });
		setField(orderNumLabel,fieldsContainer);
		setField(orderNumField, fieldsContainer);
		fieldsContainer.setVisible(false);
		frame.getContentPane().add(fieldsContainer);
		
		resultsContainer = new Box(BoxLayout.Y_AXIS);
		resultsContainer.setMinimumSize(new Dimension((int) (screenSize.width-100),screenSize.height/3 ));
		resultsContainer.setMaximumSize(new Dimension((int) (screenSize.width-100),screenSize.height/3 ));
		resultsContainer.setPreferredSize(new Dimension((int) (screenSize.width/2),screenSize.height/3 ));
		resultsContainer.setBorder(border);
		resultsContainer.setBackground(new Color(0,0,0,0));
		frame.getContentPane().add(resultsContainer);
		resultsContainer.setVisible(false);
		}

	private void setField(JComponent b, JPanel panel) {
		b.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		b.setMinimumSize(new Dimension(89,22 ));
		b.setMaximumSize(new Dimension(screenSize.width/9,screenSize.height/18 ));
		b.setPreferredSize(new Dimension(screenSize.width/9,screenSize.height/18 ));
		panel.add(b);
	}

	public JFrame getFrame() {
		return frame;
	}

	private void setButton(JButton b,Box panel) {
		b.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		//b.setPreferredSize(new Dimension(screenSize.width/10,screenSize.height/9 ));
	    panel.add(Box.createHorizontalGlue());
		b.setMinimumSize(new Dimension(89,22 ));
		b.setMaximumSize(new Dimension(screenSize.width/9,screenSize.height/9 ));
		b.setPreferredSize(new Dimension(screenSize.width/9,screenSize.height/9 ));
		panel.add(b);
		panel.add(Box.createHorizontalGlue());
		
	}
	
	private void setHintWindow(ArrayList<Order> d, JTextField tf) {
	      if (d.size()>0) {
	    	    Point point;
			    hintWindow.getHintTable().setModel(new HintOrderTableModel(d));
				point = tf.getLocationOnScreen();
				hintWindow.getHintTable().setPreferredSize(new Dimension(200, d.size()*18));
				hintWindow.getHintTable().changeSelection(0, 0, false, false);
				int x= Math.toIntExact(Math.round(point.getX()));
				int y = Math.toIntExact(Math.round(point.getY()));
				hintWindow.setBounds(x,y+25,300,d.size()*18+20);
				hintWindow.setVisible(true); 
				Log.info("setHintWindow");
				}
				else hintWindow.setVisible(false);	
	    }
	
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
			break;
			case (KeyEvent.VK_ENTER):
		     if(hintWindow.getHintTable().getModel() instanceof HintCustTableModel) {
		    	 /*recDayField.requestFocus(); 
		    	 Customer c;
				  c=((HintCustTableModel) hintWindow.getHintTable().getModel()).getCustomer(hintWindow.getHintTable().getSelectedRow());
				  setCustomer(c);
			*/	}
				  else {
					  hintWindow.setVisible(false);}
			break;
			}
			}
	    }
	};
	
	private void setResultContainer(Order order) {
	    String htmlDes="<ul>";
	    ArrayList<DescriptionRow> des=order.getDescription();
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
	 orderBut.setMinimumSize(new Dimension(600,300 ));
	 orderBut.setMaximumSize(new Dimension(screenSize.width/2,screenSize.height/4 ));
	 orderBut.setPreferredSize(new Dimension(screenSize.width/2,screenSize.height/4 ));
	 // orderBut.setText(order.title());
	 resultsContainer.add(orderBut);
	 resultsContainer.setVisible(true);
	}
	
	
}
