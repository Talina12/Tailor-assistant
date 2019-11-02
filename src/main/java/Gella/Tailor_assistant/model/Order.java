package Gella.Tailor_assistant.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Logger;


public class Order {
	private int orderNumber;
	/**the day the order was accepted*/
	private Date recDate;
	private Customer customer;
	private ArrayList<DescriptionRow>  description;
	
	/**separator followed by a string*/
	private char strSeparator='|' ;
	/**separator followed by a digit*/
	private char numSeparator='~';
	private float totalPrice;
	/**date when the order can be completed*/
	private Date estimatedCompDate;
	/** number of fittings*/
	private int tryOn;
	private float paid;
	/**time to complete the order in hours*/
	private float execTime;
	private OrderStatus status;
	/**date of fitting*/
	private Date fitDay;
	private Date issueDate;
	private ArrayList<Event> events;
	private static final int tryOnMax=4;
	public static Logger log = Logger.getLogger("model.Order");
	
	/**creates an object with empty and zero fields */
	public Order() {
		super();
		orderNumber=0;
		recDate= new Date();
		customer= new Customer();
		description = new ArrayList<DescriptionRow>();
		totalPrice=0;
		estimatedCompDate= null;
		tryOn=0;
		paid=0;
		execTime=0;
		fitDay= null;
		issueDate=null;
		events =new ArrayList<Event>();
		customer = new Customer();
		}
	
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Date getRecDate() {
		return recDate;
	}
	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Date getEstimatedCompTime() {
		return estimatedCompDate;
	}
	public void setEstimatedCompTime(Date estimatedCompTime) {
		this.estimatedCompDate = estimatedCompTime;
	}
	public int getTryOn() {
		return tryOn;
	}
	public void setTryOn(int tryOn) {
		this.tryOn = tryOn;
	}
	public float getPaid() {
		return paid;
	}
	public void setPaid(float paid) {
		this.paid = paid;
	}
	public float getExecTime() {
		return execTime;
	}
	public void setExecTime(float execTime) {
		this.execTime = execTime;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public Date getFitDay() {
		return fitDay;
	}
	public void setFitDay(Date fitDay) {
		this.fitDay = fitDay;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public ArrayList<Event> getEvents() {
		return events;
	}
	public void setEvents(ArrayList<Event> event) {
		this.events = event;
	}
	
	public static int getTryOnMax() {
		return tryOnMax;
	}
	
	public ArrayList<DescriptionRow> getDescription() {
		return description;
	}
	
    public void setDescription(ArrayList<DescriptionRow> description) {
		
		this.description = new ArrayList<DescriptionRow>(description);
	}

    @Override
	public String toString() {
		return "Order [orderNumber=" + orderNumber + ",\n recDate=" + recDate + ",\ncustomer=" + customer
				+ ",\n description=" + description + ",\n totalPrice=" + totalPrice + ",\n estimatedCompDate="
				+ estimatedCompDate + ",\n tryOn=" + tryOn + ",\n paid=" + paid + ",\n execTime=" + execTime + ",\n status="
				+ status + ",\n fitDay=" + fitDay + ",\n issueDate=" + issueDate + ",\n event=" + events + "]";
	}
    
    /**
     * returns array of field titles
    */
    public static String[] getTitles() {
    	String[] titles = new String[13];
    	titles[0]="Order id ";
    	titles[1]="Day of receipt ";
    	titles[2]="Customer id ";
    	titles[3]="Description ";
    	titles[4]="Total price ";
    	titles[5]="Estimated date of completion ";
    	titles[6]="Try-on ";
    	titles[7]="Paid ";
    	titles[8]="Execution time ";
    	titles[9]="Status ";
    	titles[10]="Date of the next fitting ";
    	titles[11]="Issue date ";
    	titles[12]="Event";
    	return titles;
    	  	 }
	
 /**
  * add all descriptions to one string with separators
  * @return all description rows in one string with separators 
 */
    public String descriptionToString() {
	 String str= new String();
	 for (DescriptionRow d:description) {
		 //Replace separators with spaces if they occur
		 d.setItem(d.getItem().replace(strSeparator, ' '));
		 d.setItem(d.getItem().replace(numSeparator, ' '));
		 str+=d.getItem()+strSeparator+d.getPrice()+numSeparator;
		 }
	 return str;
 }

/**creates description from the string  according to separators
 * @return number of description items
 * */
    public int setDescription(String string) {
		int endStr,endNum;
		ArrayList<DescriptionRow> des=new ArrayList<DescriptionRow>();
		while (string.length()>0) {
    	 	endStr=string.indexOf(strSeparator);
    	 	endNum=string.indexOf(numSeparator);
    	if(endStr<0|endNum<0) { //if the end of the item description or the end of item price is not found, return the result
    		description=des;
    		return description.size();
    		} 
    	else {
    		DescriptionRow row=new DescriptionRow(); 
    		String str;
    		//read  from the string the description  and the price of the item 
    		row.setItem(string.substring(0, endStr-1));
    		str = string.substring(endStr+1, endNum-1);
    		try {
    		row.setPrice(new Integer(str));
    		}
    		catch (NumberFormatException nex) {
    			log.severe(nex.getClass() +" in setDescription(String str)");
    			description=des;
    			return description.size();
    			}; 
    		string = string.substring(endNum+1); //delete the read data from the string
    		des.add(row);
    		};
    	}
		description=des;
    	return description.size();
	}

public static Comparator<Order> idComparator = new Comparator<Order>() {
	@Override
	public int compare(Order o1, Order o2) {
    	return o1.getOrderNumber()-o2.getOrderNumber();
	}
};

public String title() {
	String str;
	str = customer.getFirstName()+"  "+customer.getLastName()+'\n'
	      +customer.getCellphone()+"  "+customer.getHomePhone();
	return str;
}

}
