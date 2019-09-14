package Gella.Tailor_assistant.View;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
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

import Gella.Tailor_assistant.controller.DbHandler;
import Gella.Tailor_assistant.model.Customer;
import Gella.Tailor_assistant.model.Order;

import javax.swing.BoxLayout;

public class MainWindow2 {

	private JFrame frame;
	private Box buttonsContainer ;
	private JPanel fieldsContainer;
	private JPanel info;
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
		frame.setContentPane(fon);
		fontSize=screenSize.width/95;
		border = BorderFactory.createEtchedBorder();
		dbHandler=DbHandler.getInstance();
		
		buttonsContainer = new Box(BoxLayout.X_AXIS);
		buttonsContainer.setMinimumSize(new Dimension((int) (screenSize.width/4),screenSize.height/17 ));
		buttonsContainer.setMaximumSize(new Dimension((int) (screenSize.width/2),screenSize.height/10 ));
		buttonsContainer.setPreferredSize(new Dimension((int) (screenSize.width/2),screenSize.height/10 ));
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
		fieldsContainer.setMinimumSize(new Dimension(200,30 ));
		fieldsContainer.setMaximumSize(new Dimension((int) (screenSize.width-100),screenSize.height/3 ));
		fieldsContainer.setPreferredSize(new Dimension((int) (screenSize.width/2),screenSize.height/3 ));
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
		orderNumField.addCaretListener(new CaretListener () {
			@Override
			public void caretUpdate(CaretEvent e) {
				if (orderNumField.isFocusOwner()) {
				String str = orderNumField.getText();
				setHintWindow(dbHandler.getOrdersById(str),orderNumField);
				}
				}	
			}	);
		orderNumField.addKeyListener(textFieldKeyAdapter);
		setField(orderNumLabel,fieldsContainer);
		setField(orderNumField, fieldsContainer);
		fieldsContainer.setVisible(false);
		frame.getContentPane().add(fieldsContainer);
		
		
		info = new JPanel();
		info.setPreferredSize(new Dimension(500,30 ));
		info.setMaximumSize(new Dimension(500,70 ));
	//	frame.getContentPane().add(info);
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
		    	 recDayField.requestFocus(); 
		    	 Customer c;
				  c=((HintCustTableModel) hintWindow.getHintTable().getModel()).getCustomer(hintWindow.getHintTable().getSelectedRow());
				  setCustomer(c);
				}
				  else {log.warning("HintTableModel is not instance of HintCustTableModel");
				  hintWindow.setVisible(false);}
			break;
			}
			}
	    }
	};
}
