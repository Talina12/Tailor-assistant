package Gella.Tailor_assistant.model;

import java.util.Comparator;

/**
 *the  class stores customer data
 * @author Gella
 *
 */
public class Customer {
	
	private int id;
	private String firstName;
	private String lastName;
	private String cellphone;
	private String homePhone;
	
	/**
	 * constructor creates a client object with empty fields
	 */
	public Customer() {
		firstName=null;
		lastName=null;
		cellphone=null;
		homePhone=null;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	/**
	 * returns array with  names of the class fields
	 */
	public static String[] getTitles() {
		String[] t = new String[5];
		t[0]="id ";
		t[1]="First name ";
		t[2]="Last name ";
		t[3]="Cellphone ";
		t[4]="Home phone ";
		return t;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public static Comparator<Customer> firsNameComparator = new Comparator<Customer>() {
		@Override
		public int compare(Customer c1, Customer c2) {
        	return c1.getFirstName().compareTo(c2.getFirstName());
		}
    };
	
    public static Comparator<Customer> lastNameComparator= new Comparator<Customer>() {
	    @Override
		public int compare(Customer c1, Customer c2) {
        	return c1.getLastName().compareTo(c2.getLastName());
		}
    };
	public static Comparator <Customer> cellphoneComparator= new Comparator<Customer>() {
	    @Override
		public int compare(Customer c1, Customer c2) {
        	return c1.getCellphone().compareTo(c2.getCellphone());
		}
    };
	public static Comparator <Customer> homePhoneComparator= new Comparator<Customer>() {
	    @Override
		public int compare(Customer c1, Customer c2) {
        	return c1.getHomePhone().compareTo(c2.getHomePhone());
		}
    };

	@Override
	public String toString() {
		return (firstName + ", " + lastName + ", " + cellphone+ ", " + homePhone);
	}
    
}
