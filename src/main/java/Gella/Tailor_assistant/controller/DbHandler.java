package Gella.Tailor_assistant.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
//import java.util.List;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.sqlite.JDBC;

//import Gella.Tailor_assistant.View.*;
import Gella.Tailor_assistant.model.*;
//import model.DescriptionRow;
//import model.Event;
//import model.Order;
//import model.OrderStatus;

//TODO check all possible exceptions and display messages for user
/**
 * class for working with a database
 * @author Gella
 *
 */
public class DbHandler {
    
	/**
	 * The object in which the database connection will be stored
	 */
    private Connection connection;
	/**
	 * Constant in which the connection address is stored
	 */
	private static String CON_STR ; 
	private static DbHandler instance = null;
	private String info;
	Logger log;
	 
	public static synchronized DbHandler getInstance()  {
        if (instance == null)
            instance = new DbHandler();
        return instance;
	}
        
   /**
    * creates an object and establishes a connection with the database
    * creates tables and the database in the same directory as application file if they do not exist
    * @see DbHandler#createCustomerTable(),
    * @see DbHandler#createDescriptionTable()
    * @see DbHandler#createOrderTable()
    * @throws SQLException
    */
	private DbHandler(){
        try {
		CON_STR=Settings.getCON_STR();
		// Register the driver
        DriverManager.registerDriver(new JDBC());
        //connecting to the database
        this.connection = DriverManager.getConnection(CON_STR);
        //create tables if they don't exist
        createCustomerTable();
        createOrderTable();
        createDescriptionTable();
        createEventsTable();
        info =new String();
        log=Logger.getLogger("Gella.Tailor_assistant.controller.DbHandler");
        }catch(SQLException s){
          log.severe(s.getMessage()+"  "+s.getClass().toString());
          JOptionPane.showMessageDialog(null,s.getClass().toString()+"unable to connect to database");
        }
        }
   
	/**
	 * returns list of all orders as an string array
	 * @return String[][]
	 */
   public String[][] getArrayOfOrders() {
	    String sql= "SELECT id, id_customer, rec_date, description, total_price, estimated_comp_day,"
	    		+ " try_on, paid, exec_time, status, fit_day, issue_date, id_event FROM Orders ORDER BY id";
       
       try (Statement statement = this.connection.createStatement()) {
           //  upload to ArrayList orders obtained from the database.
           ArrayList<String[]> orders = new ArrayList<String[]>();
           ResultSet resultSet = statement.executeQuery(sql);
           while (resultSet.next()) {
        	   Date d;
        	   String[] no= new String[Order.getTitles().length];
               no[0]=String.valueOf(resultSet.getInt("id"));
               no[1]= resultSet.getDate("rec_date").toString();
               no[2]=String.valueOf(resultSet.getInt("id_customer"));  
               no[3]=resultSet.getString("description");
               no[4]=String.valueOf(resultSet.getFloat("total_price"));
               no[5]=resultSet.getString("estimated_comp_day");
               no[6]=String.valueOf(resultSet.getInt("try_on"));
               no[7]=String.valueOf(resultSet.getFloat("paid"));
               no[8]=String.valueOf(resultSet.getFloat("exec_time"));
               no[9]=resultSet.getString("status");
               d=resultSet.getDate("fit_day");
               if (d==null) no[10]=null;
               else no[10]=d.toString();
               d=resultSet.getDate("issue_date");
               if (d==null) no[11]=null;
               else no[11]=d.toString();
               no[12]=resultSet.getString("id_event");
               orders.add(no);
              
           }
           
           return orders.toArray(new String[orders.size()][Order.getTitles().length]);

       } catch (SQLException e) {
    	   System.out.println("error in selecl all orders function");
           e.printStackTrace();
           return null;
       }
    }
   
    /**
     * adds an order to the database
     * @param order 
     */
    public int addOrder(Order order) {
       // prepare the INSERT statement
       try (PreparedStatement statement = this.connection.prepareStatement(
                       "INSERT INTO Orders(`id_customer`, `rec_date`,`description`,"
                       + "`total_price`,`estimated_comp_day`,`try_on`,`paid`,`exec_time`,`status`,"
                       + "`fit_day`,`issue_date`) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ? )",Statement.RETURN_GENERATED_KEYS)) {
    	  //if there is no customer with such data we create a new customer
    	   Customer cust  = order.getCustomer();
    	   int custId=getCustomerId(cust);
    	   if (custId==0)
    		   {info=info+"Customer not exist"+ '\n';
    		    custId=addNewCustomer(cust); //TODO handle exceptions if cust don't added
    		    }
    	   else info=info+"Customer already exists"+'\n';
    	   statement.setInt(1, custId);
           java.sql.Date rd = new java.sql.Date(order.getRecDate().getTime());
           statement.setDate(2,  rd);
           addDescription(order.getDescription());
           statement.setString(3, order.descriptionToString());
           statement.setFloat(4,order.getTotalPrice());
           if (order.getEstimatedCompTime()!=null)
           rd = new java.sql.Date(order.getEstimatedCompTime().getTime());
           else rd=null;
           statement.setDate(5, rd);
           statement.setInt(6, order.getTryOn());
           statement.setFloat(7, order.getPaid());
           statement.setFloat(8,order.getExecTime());
           statement.setString(9,order.getStatus().toString());
           if (order.getFitDay()!=null)
           rd=new java.sql.Date(order.getFitDay().getTime());
           else rd=null;
           statement.setDate(10, rd);
           if (order.getIssueDate()!=null)
           rd=new java.sql.Date(order.getIssueDate().getTime());
           else rd=null;
           statement.setDate(11, rd);
           statement.execute();
           ResultSet rs =statement.getGeneratedKeys();
           int orderId = 0;
           if (rs.next()) {
               orderId = rs.getInt(1);
           };
           for(Event e:order.getEvents())
            {e.setOrderId(orderId);
             addEvent(e);
            }
           return orderId;
       } catch (SQLException e) {
    	   JOptionPane.showMessageDialog(null,e.getClass().toString()+"Error in DbHandler.addOrder");
    	   e.printStackTrace();
       }
	return 0;
   }
   
     private void addEvent(Event e) {
    	 try {
 			PreparedStatement statement = this.connection.prepareStatement(
 			         "INSERT INTO Events(`id_order`,`start`,`duration`,`name`,`id_google`, "
 			         + "`description`, `color_id`)  VALUES(?,?,?,?,?,?,?)") ;
 			statement.setInt(1, e.getOrderId());
 			java.sql.Date rd = new java.sql.Date(e.getStart().getTime());
 			statement.setDate(2,rd);
 			statement.setDouble(3, e.getDuration());
 			statement.setString(4, e.getName());
 			statement.setString(5, e.getGoogleId());
 			statement.setString(6, e.getDescription());
 			statement.setString(7, e.getColorId());
 			statement.execute();
 			 }
             catch (SQLException exep) {
 			// TODO Auto-generated catch block
 			exep.printStackTrace();
 		}
 		
	}

	/**
      * creates a new order table if it does not exist
      */
     public void createOrderTable() {
    	 String sqiStat = "CREATE TABLE IF NOT EXISTS Orders (\n"
    		     + "	id integer PRIMARY KEY,\n"
                 + "    id_customer integer NOT NULL,\n"
    		     + "    rec_date integer,\n"
                 + "    description text,\n"
    		     + "    total_price real, \n"
                 + "    estimated_comp_day integer, \n"
    		     + "    try_on integer, \n"
                 + "    paid real, \n"
    		     + "    exec_time integer, \n"
                 + "    status integer, \n"
    		     + "    fit_day integer, \n"
                 + "    issue_date integer, \n"
    		     + "FOREIGN KEY (id_customer) REFERENCES Customers(id))";
    	try {
			Statement stmt = connection.createStatement();
			// create a new table
            stmt.execute(sqiStat);
		} catch (SQLException e) {
			//System.out.println("error in creation stst");
			e.printStackTrace();
		}
             
     }
     
     /**
      * creates a new customer table if it does not exist
      */
     public void createCustomerTable() {
    	 String sqiStat = "CREATE TABLE IF NOT EXISTS Customers (\n"
    		     + "	id integer PRIMARY KEY,\n"
                 + "    first_name text ,\n"
    		     + "    last_name text ,\n"
                 + "    cellphone text,\n"
    		     + "    home_phone text \n);";
    	try {
			Statement stmt = connection.createStatement();
			// create a new table
            stmt.execute(sqiStat);
		} catch (SQLException e) {
			System.out.println("error in creation stat. customer table");
			e.printStackTrace();
		} 
     }
     
     /**
      * returns the id of the customer with the same values as the customer parameter. if not, then returns 0
      * @param cust 
      * @return id of customer identical to cust
      */
     public int getCustomerId(Customer cust) {
    	 String sql = "SELECT id FROM Customers WHERE first_name=? AND last_name=? AND cellphone=? AND home_phone=?";
     	 try {
    	    PreparedStatement statement  = this.connection.prepareStatement(sql);
			statement.setString(1, cust.getFirstName());
			statement.setString(2,cust.getLastName());
			statement.setString(3,cust.getCellphone());
			statement.setString(4, cust.getHomePhone());
			ResultSet rs  = statement.executeQuery();
			if (rs.next()) return rs.getInt("id");
			else return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error in prepared stat. getCustomeId");
			return 0 ;
		}
     }
     
     /**
      * adds a new customer to the database
      * @param cust
      * @returns id of customer added or 0 if failed
      */
  public   int addNewCustomer(Customer cust) {
    	 try {
			PreparedStatement statement = this.connection.prepareStatement(
			         "INSERT INTO Customers(`first_name`,`last_name`, `cellphone`,`home_phone`) " +
			          "VALUES(?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS) ;
			statement.setString(1, cust.getFirstName());
			statement.setString(2,cust.getLastName());
			statement.setString(3, cust.getCellphone());
			statement.setString(4,cust.getHomePhone());
			statement.execute();
			info = info+"Customer added "+ '\n';
			ResultSet rs = statement.getGeneratedKeys();
            int customerId = 0;
            if (rs.next()) {
                customerId = rs.getInt(1);
            }
            return customerId;
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		return 0;
    	 }
     
     /**
      * creates a new description table if it does not exist
      */
     public void createDescriptionTable() {
    	 String sqiStat = "CREATE TABLE IF NOT EXISTS Descriptions (\n"
    		     + "	id integer PRIMARY KEY,\n"
                 + "    item text ,\n"
    		     + "    price integer \n);";
    	try {
			Statement stmt = connection.createStatement();
			// create a new table
            stmt.execute(sqiStat);
		} catch (SQLException e) {
			System.out.println("error in creation stat. descriptions table");
			e.printStackTrace();
		} 
     }
     
     /**
      * adds new descriptions to the table
      * checks each description row in database if no such row puts it into the table
      * @param des
      * @return returns string with id of all description rows separated by space
      */
    
    public  String addDescription(ArrayList<DescriptionRow> des) {
    	String idString = new String();
    	for (int i=0;i<des.size();i++)
    	{int desId = getDescriptionRowId(des.get(i));
    	 if (desId==0) 
    	 {desId=addDescriptionRow(des.get(i));
    	  info = info+"description added "+ '\n';
    	 }
    	 else info = info+"description already exists"+'\n';
    	 idString = idString+ String.valueOf(desId)+' '; 
    	};
    	return idString;
     }
    
    /**
     * returns id of description row identical to row
     * @param row
     * @return if there is no such row in database returns 0
     */
    public int getDescriptionRowId(DescriptionRow row) {
    	String sql = "SELECT id FROM Descriptions WHERE item=? AND price=? ";
    	 try {
   	    PreparedStatement statement  = this.connection.prepareStatement(sql);
			statement.setString(1, row.getItem());
			statement.setInt(2,row.getPrice());
			ResultSet rs  = statement.executeQuery();
			if (rs.next()) return rs.getInt("id");
			else return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error in prepared stat. getCustomeId");
			return 0 ;
		}
    }
    
    /**
     * adds description row to the database 
     * @param row
     * @return id of description added or 0 if fail
     */
    public int addDescriptionRow(DescriptionRow row) {
    	try {
			PreparedStatement statement = this.connection.prepareStatement(
			         "INSERT INTO Descriptions(`item`,`price`) " +
			          "VALUES(?, ?)",Statement.RETURN_GENERATED_KEYS) ;
			statement.setString(1, row.getItem());
			statement.setInt(2,row.getPrice());
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
            int descriptionId = 0;
            if (rs.next()) {
            	descriptionId = rs.getInt(1);
            }
            return descriptionId;
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;	
    }

	/**
	 * returns all customers from database as array of string
	 * @return String[][]
	 */
    public String[][] getArrayOfCustomers() {
		String sql= "SELECT id, first_name, last_name, cellphone, home_phone FROM Customers ORDER BY id";
       
       try (Statement statement = this.connection.createStatement()) {
           //  upload to ArrayList orders obtained from the database.
           ArrayList<String[]> customers = new ArrayList<String[]>();
           ResultSet resultSet = statement.executeQuery(sql);
           while (resultSet.next()) {
        	   String[] nc= new String[Customer.getTitles().length];
               nc[0]=String.valueOf(resultSet.getInt("id"));
               nc[1]= resultSet.getString("first_name");
               nc[2]=resultSet.getString("last_name");  
               nc[3]=resultSet.getString("cellphone");
               nc[4]=resultSet.getString("home_phone");
               customers.add(nc);
               }
           return customers.toArray(new String[customers.size()][Customer.getTitles().length]);

       } catch (SQLException e) {
    	   System.out.println("error in selecl all customers function");
           e.printStackTrace();
           return null;
       }
	}

	/**
	 * returns all descriptions from database as array of String
	 * @return String[][]
	 */
    public String[][] getArrayOfDescriptions() {
		String sql= "SELECT id, item, price FROM Descriptions ORDER BY id";
	       
	       try (Statement statement = this.connection.createStatement()) {
	           //  upload to ArrayList orders obtained from the database.
	           ArrayList<String[]> descriptions = new ArrayList<String[]>();
	           ResultSet resultSet = statement.executeQuery(sql);
	           while (resultSet.next()) {
	        	   String[] nd= new String[DescriptionRow.getTitles().length+1];
	               nd[2]=String.valueOf(resultSet.getInt("id"));
	               nd[0]=resultSet.getString("item");  
	               nd[1]=String.valueOf(resultSet.getInt("price"));
	               descriptions.add(nd);
	               }
	           return descriptions.toArray(new String[descriptions.size()][DescriptionRow.getTitles().length+1]);

	       } catch (SQLException e) {
	    	   System.out.println("error in selecl all customers function");
	           e.printStackTrace();
	           return null;
	       }
	}

	/**
	 * returns a list of customers whose first names begin with  str
	 * @param str string for search 
	 * @return null if there is no such customer
	 */
    public ArrayList<Customer> getCustomersByFirstName(String str) {
		 String sql = "SELECT id, first_name, last_name, cellphone, home_phone FROM Customers "
		 		+ " WHERE first_name LIKE ?";

      try ( PreparedStatement stat  = this.connection.prepareStatement(sql)){
       // set the value
      stat.setString(1,str+"%");
      ResultSet rs  = stat.executeQuery();
      // loop through the result set
       ArrayList<Customer> data= new ArrayList<Customer>();
       while (rs.next()) {
	     Customer cust = new Customer();
	     cust.setId(rs.getInt("id"));
	     cust.setFirstName(rs.getString("first_name"));
	     cust.setLastName(rs.getString("last_name"));
	     cust.setHomePhone(rs.getString("home_phone"));
	     cust.setCellphone(rs.getString("cellphone"));
	     data.add(cust);
        }
      Collections.sort(data,Customer.firsNameComparator);
     return data; 
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
   return null;
  }

    /**
	 * returns a list of customers whose last names begin with  str
	 * @param str string for search 
	 * @return null if there is no such customer
	 */
    public ArrayList<Customer> getCustomersByLastName(String str) {
		String sql = "SELECT id, first_name, last_name, cellphone, home_phone FROM Customers "
		 		+ " WHERE last_name LIKE ?";

try ( PreparedStatement stat  = this.connection.prepareStatement(sql)){
   // set the value
   stat.setString(1,str+"%");
   ResultSet rs  = stat.executeQuery();
   // loop through the result set
   ArrayList<Customer> data= new ArrayList<Customer>();
   while (rs.next()) {
	   Customer cust = new Customer();
	   cust.setId(rs.getInt("id"));
	   cust.setFirstName(rs.getString("first_name"));
	   cust.setLastName(rs.getString("last_name"));
	   cust.setHomePhone(rs.getString("home_phone"));
	   cust.setCellphone(rs.getString("cellphone"));
	   data.add(cust);
   }
  Collections.sort(data,Customer.lastNameComparator);
  return data; 
} catch (SQLException e) {
   System.out.println(e.getMessage());
   e.printStackTrace();
}
return null;
	}

    /**
	 * returns a list of customers whose cellphones begin with  str
	 * @param str string for search 
	 * @return null if there is no such customer
	 */
    public ArrayList<Customer> getCustomersByCellphone(String str) {
		String sql = "SELECT id, first_name, last_name, cellphone, home_phone FROM Customers "
		 		+ " WHERE cellphone LIKE ?";

try ( PreparedStatement stat  = this.connection.prepareStatement(sql)){
   // set the value
   stat.setString(1,str+"%");
   ResultSet rs  = stat.executeQuery();
   // loop through the result set
   ArrayList<Customer> data= new ArrayList<Customer>();
   while (rs.next()) {
	   Customer cust = new Customer();
	   cust.setId(rs.getInt("id"));
	   cust.setFirstName(rs.getString("first_name"));
	   cust.setLastName(rs.getString("last_name"));
	   cust.setHomePhone(rs.getString("home_phone"));
	   cust.setCellphone(rs.getString("cellphone"));
	   data.add(cust);
   }
  Collections.sort(data,Customer.cellphoneComparator);
  return data; 
} catch (SQLException e) {
   System.out.println(e.getMessage());
   e.printStackTrace();
}
return null;
	}

    /**
	 * returns a list of customers whose home phones begin with  str
	 * @param str string for search 
	 * @return null if there is no such customer
	 */
    public ArrayList<Customer> getCustomersByHomePhone(String str) {
		String sql = "SELECT id, first_name, last_name, cellphone, home_phone FROM Customers "
		 		+ " WHERE home_phone LIKE ?";

try ( PreparedStatement stat  = this.connection.prepareStatement(sql)){
   // set the value
   stat.setString(1,str+"%");
   ResultSet rs  = stat.executeQuery();
   // loop through the result set
   ArrayList<Customer> data= new ArrayList<Customer>();
   while (rs.next()) {
	   Customer cust = new Customer();
	   cust.setId(rs.getInt("id"));
	   cust.setFirstName(rs.getString("first_name"));
	   cust.setLastName(rs.getString("last_name"));
	   cust.setHomePhone(rs.getString("home_phone"));
	   cust.setCellphone(rs.getString("cellphone"));
	   data.add(cust);
   }
  Collections.sort(data,Customer.homePhoneComparator);
  return data; 
} catch (SQLException e) {
   System.out.println(e.getMessage());
   e.printStackTrace();
}
return null;
		}

    /**
	 * returns a list of descriptions whose names begin with  str
	 * @param str string for search 
	 * @return null if there is no such description
	 */
    public ArrayList<DescriptionRow> getDescriptions(String str) {
		String sql = "SELECT id, item, price FROM Descriptions "
		 		+ " WHERE item LIKE ?";

try ( PreparedStatement stat  = this.connection.prepareStatement(sql)){
   // set the value
   stat.setString(1,str+"%");
   ResultSet rs  = stat.executeQuery();
   // loop through the result set
   ArrayList<DescriptionRow> data= new ArrayList<DescriptionRow>();
   while (rs.next()) {
	   DescriptionRow des = new DescriptionRow();
	   des.setItem(rs.getString("item"));
	   des.setPrice(rs.getInt("price"));
	   data.add(des);
   }
  Collections.sort(data);
  return data; 
} catch (SQLException e) {
   System.out.println(e.getMessage());
   e.printStackTrace();
}
return null;
	}
   
    public void resetInfo() {
    	info ="";
    }
    
 public String getInfo() {return info;}
 

public void createEventsTable() {
	 String sqiStat = "CREATE TABLE IF NOT EXISTS Events (\n"
		     +  "id integer PRIMARY KEY,\n"
             +  "id_order integer NOT NULL,\n"
		     +  "id_google string, \n"
		     +  "start integer,\n"
             +  "duration integer,\n"
		     +  "name string , \n"
		     +  "description string, \n"
		     +  "color_id,  \n"
             + "FOREIGN KEY (id_order) REFERENCES Orders(id))";
	try {
		Statement stmt = connection.createStatement();
		// create a new table
       stmt.execute(sqiStat);
	} catch (SQLException e) {
		//System.out.println("error in creation stst");
		e.printStackTrace();
	}
        
}

public ArrayList<Event> getEventsByGoogleId(String id) {
	String sql = "SELECT id, id_order, id_google, start, duration, name, description, color_id FROM Events "
	 		+ " WHERE id_google LIKE ?";

try ( PreparedStatement stat  = this.connection.prepareStatement(sql)){
// set the value
stat.setString(1,id);
ResultSet rs  = stat.executeQuery();
// loop through the result set
ArrayList<Event> data= new ArrayList<Event>();
while (rs.next()) {
   Event ev = new Event();
   ev.setId(rs.getInt("id"));
   ev.setOrderId(rs.getInt("id_order"));
   ev.setGoogleId(rs.getString("id_google"));
   ev.setStart(rs.getDate("start"));
   ev.setDuration(rs.getLong("duration"));
   ev.setName(rs.getString("name"));
   ev.setDescription(rs.getString("description"));
   ev.setColorId(rs.getString("color_id"));
   data.add(ev);
}
return data; 
} catch (SQLException e) {
log.severe(e.getMessage()+"  "+ e.getClass().toString());
}
return null;
}

ArrayList<Event> getEvents(){
 String sql= "SELECT id, id_order, id_google, start, duration, name, description, color_id FROM Events ORDER BY id";
 try (Statement statement = this.connection.createStatement()) {
   //  upload to ArrayList orders obtained from the database.
   ArrayList<Event> events = new ArrayList<Event>();
   ResultSet resultSet = statement.executeQuery(sql);
   while (resultSet.next()) {
     Event ev= new Event();
     ev.setId(resultSet.getInt("id"));
     ev.setOrderId(resultSet.getInt("id_order"));
     ev.setGoogleId(resultSet.getString("id_google"));  
     ev.setStart(resultSet.getDate("start"));
     ev.setDuration(resultSet.getLong("duration"));
     ev.setName(resultSet.getString("name"));
     ev.setDescription(resultSet.getString("description"));
     ev.setColorId(resultSet.getString("color_id"));
     events.add(ev);
   }
   return events;
 } catch (SQLException e) {
 	   System.out.println("error in selecl all customers function");
        e.printStackTrace();
        return null;
    }	
}

public void updateEvent(Event lev) {
	// TODO Auto-generated method stub
 String sql = "UPDATE events SET id_google = ? ,start = ?, duration=?, name=?, description=?, color_id=? WHERE id = ?";
 PreparedStatement pstmt;
try {
	pstmt = this.connection.prepareStatement(sql);
	pstmt.setString(1, lev.getGoogleId());
	java.sql.Date d = new java.sql.Date(lev.getStart().getTime());
	pstmt.setDate(2, d);
	pstmt.setLong(3, lev.getDuration());
	pstmt.setString(4, lev.getName());
	pstmt.setString(5, lev.getDescription());
	pstmt.setString(6, lev.getColorId());
	pstmt.setInt(7, lev.getId());
	pstmt.executeUpdate();
    } catch (SQLException e) {
	   // TODO Auto-generated catch block
	  e.printStackTrace();
}	 
}

public ArrayList<Event> getEventsByOrderId(int id) {
	String sql = "SELECT id, id_order, id_google, start, duration, name, description, color_id "
			+ "FROM Events  WHERE id_order LIKE ?";

try ( PreparedStatement stat  = this.connection.prepareStatement(sql)){
// set the value
stat.setInt(1,id);
ResultSet rs  = stat.executeQuery();
// loop through the result set
ArrayList<Event> data= new ArrayList<Event>();
while (rs.next()) {
   Event ev = new Event();
   ev.setId(rs.getInt("id"));
   ev.setOrderId(rs.getInt("id_order"));
   ev.setGoogleId(rs.getString("id_google"));
   ev.setStart(rs.getDate("start"));
   ev.setDuration(rs.getLong("duration"));
   ev.setName(rs.getString("name"));
   ev.setDescription(rs.getString("description"));
   ev.setColorId(rs.getString("color_id"));
   data.add(ev);
}
return data; 
} catch (SQLException e) {
log.severe(e.getMessage()+"  "+ e.getClass().toString());
}
return null;	// TODO Auto-generated method stub
}
}

