package Gella.Tailor_assistant.model;

/**
 * class containing description item
 * @author Gella
 *
 */
public class DescriptionRow implements Comparable<DescriptionRow>{
	
	/**description of the item*/
	private String des;
	/**price of the item*/
	private Integer p;
	/**amount of class fields*/
	private static int fieldCount=2; //number of fields

	/**creates a class with empty description and price 0*/
	public DescriptionRow() {
		des = new String();
		p= new Integer(0);
		}
	
	public String getItem() {
		return des;
	}

	public void setItem(String des) {
		this.des = des;
	}

	public Integer getPrice() {
		return p;
	}

	public void setPrice(Integer p) {
		this.p = p;
	}
	
	public void setPrice(int p) {
		this.p = new Integer(p);
	}

	/**
	 * returns an array with field names
	 */
	public static String[] getTitles() { 
		String [] v = new String[2];
		v[0]="Item";
		v[1]="Price";
		return (v);
		}
	
	/**
	 * returns class returns class by field number
	 * @param i field number
	 * @throws NoSuchFieldException if there is no such field
	 */
	public static Class<?> getFieldClass(int i) throws NoSuchFieldException {
		switch (i) {
		case 0:
			return String.class;
		case 1:
			return Integer.class;
		default: throw new NoSuchFieldException("No such field in DescriptionRow");
		}
		
	}
	
	/**
	 * returns a field 
	 * @param i field number
	 * @throws NoSuchFieldException if there is no such field
	 */
	public Object getField(int i) throws NoSuchFieldException  {
		switch (i) {
		case 0:
			return des;
		case 1:
			return p;
		default: throw new NoSuchFieldException("No such field in DescriptionRow");
		}
	}
	
	/**
	 * assigns a value to a field
	 * @param field number
	 * @param value to assign
	 * @throws NoSuchFieldException if there is no such field
	 */
	public void setField(int field,Object value)throws NoSuchFieldException {
		switch (field) {
		case 0:
		{des=(String)value ;
			break;}
		case 1:
			{p=(Integer)value;
			break;}
		default: throw new NoSuchFieldException("No such field in DescriptionRow");
		}
	}
	
	/**
	 * returns amount of fields
	 */
	public static int getFieldCount() {return fieldCount;}

	@Override
	public String toString() {
		return " " + des + " " + p;
	}

	@Override
	public int compareTo(DescriptionRow arg0) {
		return this.des.compareTo(arg0.getItem());
	}
	}

