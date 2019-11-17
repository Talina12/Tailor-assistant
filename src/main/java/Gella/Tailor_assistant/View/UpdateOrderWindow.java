package Gella.Tailor_assistant.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MaskFormatter;
import javax.swing.text.DocumentFilter.FilterBypass;

import Gella.Tailor_assistant.controller.CalendarController;
import Gella.Tailor_assistant.controller.DbHandler;
import Gella.Tailor_assistant.controller.GoogleCalendarController;
import Gella.Tailor_assistant.model.Customer;
import Gella.Tailor_assistant.model.DescriptionRow;
import Gella.Tailor_assistant.model.Event;
import Gella.Tailor_assistant.model.Order;
import Gella.Tailor_assistant.model.OrderStatus;
import Gella.Tailor_assistant.model.Settings;

public class UpdateOrderWindow extends JFrame {

	private JScrollPane scrollContentPane;
	private JPanel contentPane;
	private JPanel rootContentPane;
	private JLabel FirstNameLabel;
	private JTextField firstNameField;
	private JLabel LastNameLabel;
	private JTextField lastNameField;
	private JLabel cellPhoneLabel;
	private JTextField cellphoneField;
	private JLabel homePhoneLabel;
	private JTextField homePhoneField;
	private JFormattedTextField recDayField;
	private JTable descriptionTable;
	private JLabel totalPriceLabel;
	private JFormattedTextField totalPriceField;
	private JButton calculateTotalPrice;
	private JLabel estimatedCompTimeLabel;
	private JFormattedTextField estimatedCompDateField;
	private JFormattedTextField estimatedCompTimeField;
	private JButton calculateDateButton;
	private JFormattedTextField paidField;
	private JFormattedTextField execTimeField;
	private JFormattedTextField fitDayField;
	private JFormattedTextField issueDateField;
	private JLabel descriptionLabel;
	private JSeparator separator;
	private JLabel recDateLabel;
	private JLabel tryOnLabel;
	private JSpinner tryOnSpinner;
	private  JLabel statusLabel;
	private JLabel execTimeLabel;
	private JComboBox<String> statusComboBox;
	private JButton calculateTimeButton;
	private JLabel paidLabel;
	private JLabel fitDateLabel;
	private JLabel issueDateLabel;
	private JButton updateButton;
	private Border defaultBorder;
	private HintWindow hintWindow;
	private DbHandler dbHandler;
	private Order order;
	private FocusTraversalPolicy focusPolicy;
	public static Logger log = Logger.getLogger("View.UpdateOrderWindow");
	private CalendarController calendarController;
	private Settings settings;
	private ArrayList<Event> dates;
	private GoogleCalendarController googleCalendarController;
	private Dimension frameSize;
	private boolean rescheduled;
	
	
	/**
	 * Create the frame.
	 */
	public UpdateOrderWindow(Order upOrder) {
		try {
			dbHandler = DbHandler.getInstance();
			googleCalendarController=GoogleCalendarController.getInstance();
			settings=Settings.getInstance();
		} catch (SQLException e3) {
			JOptionPane.showMessageDialog(null,e3.getClass().toString()+"unable to connect to database");
			log.severe("unable to connect to database");
		}
		order=upOrder;
		rescheduled=false;
		calendarController= CalendarController.getInstance();
		int x=Math.round(Toolkit.getDefaultToolkit().getScreenSize().width*(float)(0.8));
		int y=Math.round(Toolkit.getDefaultToolkit().getScreenSize().height*(float)(0.8));
		frameSize= new Dimension(x,y);
		setBounds(30, 20, frameSize.width,frameSize.height);
		setTitle("Update order");
		
		hintWindow = new HintWindow();
		rootContentPane = new JPanel();
		rootContentPane.setLayout(new BorderLayout());
		rootContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(rootContentPane);
		
		contentPane = new JPanel();
		
		contentPane.setLayout(null);
		contentPane.setPreferredSize(new Dimension(800, 580));
		
		FirstNameLabel = new JLabel("First name");
		FirstNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		FirstNameLabel.setBounds(37, 29, 75, 22);
		contentPane.add(FirstNameLabel);
		 
		
		firstNameField = new JTextField(upOrder.getCustomer().getFirstName());
		firstNameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		firstNameField.setBounds(122, 29, 135, 22);
		contentPane.add(firstNameField);
		firstNameField.setColumns(10);
		
		LastNameLabel = new JLabel("Last name");
		LastNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		LastNameLabel.setBounds(311, 29, 91, 22);
		contentPane.add(LastNameLabel);
		
		lastNameField = new JTextField(upOrder.getCustomer().getLastName());
		lastNameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lastNameField.setBounds(412, 29, 135, 22);
		contentPane.add(lastNameField);
		lastNameField.setColumns(10);
		
		cellphoneField = new JTextField(upOrder.getCustomer().getCellphone());
		cellphoneField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cellphoneField.setBounds(122, 78, 135, 22);
		contentPane.add(cellphoneField);
		cellphoneField.setColumns(10);
		
		homePhoneLabel = new JLabel("Home phone");
		homePhoneLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		homePhoneLabel.setBounds(312, 78, 90, 22);
		contentPane.add(homePhoneLabel);
		
		cellPhoneLabel = new JLabel("Cellphone");
		cellPhoneLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cellPhoneLabel.setBounds(37, 78, 75, 22);
		contentPane.add(cellPhoneLabel);
		
		homePhoneField = new JTextField(upOrder.getCustomer().getHomePhone());
		homePhoneField.setBounds(412, 78, 135, 22);
		homePhoneField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(homePhoneField);
		homePhoneField.setColumns(10);
		
		separator = new JSeparator();
		separator.setBounds(10, 118, 735, 2);
		contentPane.add(separator);
		
		recDateLabel = new JLabel("Day of receipt");
		recDateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		recDateLabel.setBounds(37, 134, 91, 22);
		contentPane.add(recDateLabel);
		
		recDayField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yy"));
		try {
			MaskFormatter dateMask = new MaskFormatter("##/##/####");
			dateMask.install(recDayField);
		} catch (ParseException e2) {
			log.severe("unable create recDayField");
			e2.printStackTrace();
				}
		recDayField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		recDayField.setBounds(133, 134, 86, 22);
		recDayField.setFocusLostBehavior(JFormattedTextField.REVERT);
		contentPane.add(recDayField);
		recDayField.setColumns(10);
		recDayField.setValue(upOrder.getRecDate()); //set default value by today's date
		
		tryOnLabel = new JLabel("Try-on");
		tryOnLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tryOnLabel.setBounds(334, 134, 68, 22);
		contentPane.add(tryOnLabel);
		
		tryOnSpinner = new JSpinner();
		tryOnSpinner.setModel(new SpinnerNumberModel(0, 0, Order.getTryOnMax(), 1));
		tryOnSpinner.setBounds(393, 131, 44, 30);
		tryOnSpinner.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tryOnSpinner.setValue(upOrder.getTryOn());
		contentPane.add(tryOnSpinner);
		
		statusLabel = new JLabel("Status");
		statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		statusLabel.setBounds(550, 134, 46, 22);
		contentPane.add(statusLabel);
		
	    statusComboBox = new JComboBox<String>(OrderStatus.titles()); 
		statusComboBox.setBounds(603, 134, 142, 22);
		statusComboBox.setEditable(false);
		contentPane.add(statusComboBox);
		
		
	    descriptionLabel = new JLabel("Description");
		descriptionLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		descriptionLabel.setBounds(37, 164, 91, 22);
		contentPane.add(descriptionLabel);
		
		descriptionTable = new JTable();
		final DescriptionTableModel t= new DescriptionTableModel(upOrder.getDescription());
		descriptionTable.setModel(t);
		descriptionTable.setPreferredSize(new Dimension(700,300));
		descriptionTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
		descriptionTable.setRowHeight(20);
		descriptionTable.getColumnModel().getColumn(1).setMaxWidth(80);
		descriptionTable.setCellSelectionEnabled(true);
		descriptionTable.setSurrendersFocusOnKeystroke(true);
		t.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent event) {
				if (event.getType()==TableModelEvent.INSERT) //if a new row has been added, set selection on the first cell of this row
					descriptionTable.changeSelection(event.getLastRow(),0, false, false);
				else
					if (event.getColumn()<t.getColumnCount()-1) //if the value entered not in the last column, then go to the next column
						descriptionTable.changeSelection(event.getLastRow(),event.getColumn()+1, false, false);
					else 
						if (event.getLastRow()<t.getRowCount()-1) //If the value is entered in the last column, go to the first column of the next line.
							descriptionTable.changeSelection(event.getLastRow()+1,0, false, false);
							}});
		
	    final JTextField component = new JTextField();
	    component.addCaretListener(new CaretListener() {
            @Override
			public void caretUpdate(CaretEvent arg0) {
            	 String str = ((JTextField) arg0.getSource()).getText();
       			 setHintWindow(dbHandler.getDescriptions(str),component);
       			 component.requestFocus();
       			}});
	    component.addActionListener(tableCellActionListener);
	    component.addFocusListener(textFieldFocusAdapter);
	    descriptionTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(component));
		JScrollPane jScrollDes = new JScrollPane(descriptionTable);
		jScrollDes.setBounds(37, 197, 789, 127);
		contentPane.add(jScrollDes);
		
		totalPriceLabel = new JLabel("Total price");
		totalPriceLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		totalPriceLabel.setBounds(576, 341, 70, 22);
		contentPane.add(totalPriceLabel);
		
		DecimalFormat f = new DecimalFormat("##.##", new DecimalFormatSymbols(Locale.US));
		totalPriceField = new JFormattedTextField(f);
		totalPriceField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		totalPriceField.setBounds(656, 341, 75, 22);
		totalPriceField.setValue(upOrder.getTotalPrice());
		contentPane.add(totalPriceField);
		totalPriceField.setColumns(10);
		totalPriceField.addActionListener(textFieldActionListener);
		calculateTotalPrice= new JButton("Calculate");
		calculateTotalPrice.setBounds(737, 341, 89, 22);
		contentPane.add(calculateTotalPrice);
		calculateTotalPrice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					totalPriceField.setValue(Double.valueOf(t.getSumColumn(1)));//summarize column 1 in the table
				} catch (NoSuchFieldException e1) {
					log.severe(e1.toString());
					e1.printStackTrace();
				} catch (UnsupportedOperationException e1) {
					log.severe(e1.toString());
					e1.printStackTrace();
				}
			}});
		
		execTimeLabel = new JLabel("Execution time");
		execTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		execTimeLabel.setBounds(37, 341, 110, 22);
		contentPane.add(execTimeLabel);
		
		execTimeField = new JFormattedTextField(f);
		execTimeField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		execTimeField.setBounds(157, 341, 86, 22);
		execTimeField.setColumns(10);
		execTimeField.addActionListener(textFieldActionListener);
		execTimeField.setValue(upOrder.getExecTime());
		contentPane.add(execTimeField);
		
		calculateTimeButton = new JButton("Calculate");
		calculateTimeButton.setBounds(251, 341, 89, 22);
		contentPane.add(calculateTimeButton);
		calculateTimeButton.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
				float total;
				try {total=Float.parseFloat(totalPriceField.getText());} //take value from TotalPriceField 
				 catch (NullPointerException e1) { total=0;} //if it is empty, we take 0
				execTimeField.setValue(total/settings.getHourRating()); //calculate time based on HourRating value from Settings 
			}});
		
		
		estimatedCompTimeLabel = new JLabel("Estimated date of completion");
		estimatedCompTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		estimatedCompTimeLabel.setBounds(37, 386, 182, 22);
		contentPane.add(estimatedCompTimeLabel);
		
		estimatedCompDateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
		try {
			MaskFormatter dateMask = new MaskFormatter("##/##/####");
			dateMask.install(estimatedCompDateField);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		estimatedCompDateField.setBounds(228, 386, 86, 22);
		estimatedCompDateField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		estimatedCompDateField.setValue(upOrder.getEvents().get((upOrder.getEvents().size()-1)).getEnd());
		contentPane.add(estimatedCompDateField);
		estimatedCompDateField.setColumns(10);
		
		estimatedCompTimeField= new JFormattedTextField(new SimpleDateFormat("kk.mm"));
		try {
			MaskFormatter timeMask = new MaskFormatter("##.##");
			timeMask.install(estimatedCompTimeField);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		estimatedCompTimeField.setBounds(318, 386, 86, 22);
		estimatedCompTimeField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		estimatedCompTimeField.setValue(upOrder.getEvents().get((upOrder.getEvents().size()-1)).getEnd());
		contentPane.add(estimatedCompTimeField);
		estimatedCompTimeField.setColumns(10);
		
		calculateDateButton = new JButton("Calculate");
		calculateDateButton.setBounds(414, 386, 89, 22);
		calculateDateButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
			try {	
			long duration=Math.round(Float.parseFloat(execTimeField.getText())*3600000); 
			dates= calendarController.getFreeDates(duration,order);
			if (dates.size()>0) {estimatedCompDateField.setValue(dates.get(dates.size()-1).getStart());
			                     estimatedCompTimeField.setValue(dates.get(dates.size()-1).getStart());
			                     rescheduled=true;
			                     }
			else JOptionPane.showMessageDialog(null,"could not find a suitable date");
			}
			catch(RuntimeException r) {JOptionPane.showMessageDialog(null,"not valid value in Execution time");
			log.warning(r.getClass().toString());
			r.printStackTrace();
			};
			
				
			}
		});
		contentPane.add(calculateDateButton);
		
		fitDateLabel = new JLabel("Date of the next fitting");
		fitDateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		fitDateLabel.setBounds(35, 431, 184, 22);
		contentPane.add(fitDateLabel);
		
		fitDayField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yy"));
		try {
			MaskFormatter dateMask = new MaskFormatter("##/##/##");
			dateMask.install(fitDayField);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		fitDayField.setBounds(228, 431, 76, 22);
		fitDayField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		fitDayField.setValue(upOrder.getFitDay());
		contentPane.add(fitDayField);
		fitDayField.setColumns(10);
		
		paidLabel = new JLabel("Paid");
		paidLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		paidLabel.setBounds(37, 521, 44, 22);
		contentPane.add(paidLabel);
		
		paidField = new JFormattedTextField(new DecimalFormat());
		paidField.setBounds(86, 521, 75, 22);
		paidField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		paidField.setValue(upOrder.getPaid());
		contentPane.add(paidField);
		paidField.setColumns(10);
		
		issueDateLabel = new JLabel("Issue date");
		issueDateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		issueDateLabel.setBounds(35, 476, 77, 22);
		contentPane.add(issueDateLabel);
		issueDateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yy"));
			try {
				MaskFormatter dateMask = new MaskFormatter("##/##/##");
				dateMask.install(issueDateField);
			} catch (ParseException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		issueDateField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		issueDateField.setBounds(133, 476, 76, 22);
		issueDateField.setValue(upOrder.getIssueDate());
		contentPane.add(issueDateField);
		issueDateField.setColumns(10);
		
		updateButton= new JButton("Update");
		updateButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		updateButton.setBounds(351, 513, 309, 30);
		updateButton.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent arg0) {
			 setOrder(order);
			 if(rescheduled) {	
				 int id= dbHandler.updateOrder(order);
				 if(id>0)
				 for (Event ev: dbHandler.getEventsByOrderId(id))
				   googleCalendarController.addEvent(ev);	
			 }
			 else dbHandler.updateOrderwithoutEvents(order);
				 dispose();
				
			}});
		
		contentPane.add(updateButton);
		
		scrollContentPane = new JScrollPane(contentPane);
		rootContentPane.add(scrollContentPane,BorderLayout.CENTER);
		focusPolicy = this.getFocusTraversalPolicy();
	}
	

/**
 * sets hintWindow parameters and data for display
 * @param d data to display in hintWindow
 * @param tf field associated with the hint
 */
private void setHintWindow(ArrayList d, JTextField tf) {
  if (d.size()>0) {
	    Point point;
		if (d.get(0) instanceof Customer) {
	    hintWindow.getHintTable().setModel(new HintCustTableModel(d));
		point = tf.getLocationOnScreen();
		}
		else {hintWindow.getHintTable().setModel(new HintDesTableModel(d));
		point = descriptionTable.getLocationOnScreen();
		point.y=point.y+descriptionTable.getSelectedRow()*18;
		}
		hintWindow.getHintTable().setPreferredSize(new Dimension(200, d.size()*18));
		hintWindow.getHintTable().changeSelection(0, 0, false, false);
		int x= Math.toIntExact(Math.round(point.getX()));
		int y = Math.toIntExact(Math.round(point.getY()));
		hintWindow.setBounds(x,y+25,300,d.size()*18+20);
		hintWindow.setVisible(true); 
		}
		else hintWindow.setVisible(false);	
}

private ActionListener tableCellActionListener = new ActionListener() {
	@Override
	public void actionPerformed(ActionEvent e) {
		if (hintWindow.isVisible()) {
		 if(hintWindow.getHintTable().getModel() instanceof HintDesTableModel) {
	    	DescriptionRow d;
			  d=((HintDesTableModel) hintWindow.getHintTable().getModel()).getDescriptionRow(hintWindow.getHintTable().getSelectedRow());
			  setDesc(d);
			  hintWindow.setVisible(false);
			  }
			  else {log.warning("HintTableModel is not instance of HintDesTableModel");
			  hintWindow.setVisible(false);}
		}
	}
    };
    
    private FocusAdapter textFieldFocusAdapter = new FocusAdapter() {
		public void focusLost(FocusEvent e) {
			hintWindow.setVisible(false);
			}
	};
	
/**
	 * moves focus to the next field when a value is entered
 */
 private ActionListener textFieldActionListener = new ActionListener() {
        @Override
		public void actionPerformed(ActionEvent arg0) {
	    	((JComponent) arg0.getSource()).setBorder(defaultBorder);
			focusPolicy.getComponentAfter(UpdateOrderWindow.this, (Component) arg0.getSource()).requestFocus();
		    }
 };	
 
 public void setOrder(Order newOrder) {
	 newOrder.getCustomer().setFirstName(firstNameField.getText());
	 newOrder.getCustomer().setLastName(lastNameField.getText());
	 newOrder.getCustomer().setCellphone(cellphoneField.getText());
	 newOrder.getCustomer().setHomePhone(homePhoneField.getText());
	 newOrder.setRecDate((Date) recDayField.getValue());
	 newOrder.setDescription(((DescriptionTableModel) descriptionTable.getModel()).getData());
	 newOrder.setTotalPrice(Float.parseFloat(totalPriceField.getText()));
	 if ((estimatedCompDateField).getValue()!= null) 
		 newOrder.setEstimatedCompTime((Date) ((JFormattedTextField) estimatedCompDateField).getValue());
	 else newOrder.setEstimatedCompTime(null);
	 newOrder.setTryOn(((Integer) tryOnSpinner.getValue()).intValue());
	 newOrder.setPaid(Float.parseFloat(paidField.getText()));
	 newOrder.setExecTime(Float.parseFloat(execTimeField.getText()));
	 newOrder.setStatus(OrderStatus.valueOfTitle((String)statusComboBox.getSelectedItem()));
	 if (fitDayField.getValue()!=null) 
		 newOrder.setFitDay((Date) fitDayField.getValue());
	 else newOrder.setFitDay(null);
	 if (issueDateField.getValue()!=null)
		 newOrder.setIssueDate((Date) issueDateField.getValue());
	 else newOrder.setIssueDate(null);
	 if(rescheduled) {
	 for(Event i:dates ) {
	   i.setName(newOrder.getCustomer().toString()+ " "+ newOrder.getTotalPrice());
	   i.setOrderId(newOrder.getOrderNumber());
	   }
	 newOrder.setEvents(dates);
	 }
	}
 
 /**
  * enters the value of param  into the table
 */
   protected void setDesc(DescriptionRow d) {
   int i=descriptionTable.getSelectedRow();
   descriptionTable.setValueAt(d.getItem(), i, 0);
   descriptionTable.setValueAt(d.getPrice(), i, 1);
   ((AbstractTableModel) descriptionTable.getModel()).fireTableCellUpdated( i, 1);
 }
}
