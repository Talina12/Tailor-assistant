package App;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import model.Customer;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
       Calendar calendar1 = new GregorianCalendar();
	   calendar1.set(Calendar.YEAR, 2019);
	   calendar1.set(Calendar.MONTH, 8);
	   calendar1.set(Calendar.DAY_OF_MONTH, 8);
	   calendar1.set(Calendar.HOUR_OF_DAY, 0);
	   calendar1.set(Calendar.MINUTE, 0);
	   calendar1.set(Calendar.SECOND, 0);

	   System.out.println(calendar1.getTime());
	   
	   Calendar calendar2 = new GregorianCalendar();
	   calendar2.set(Calendar.YEAR, 2019);
	   calendar2.set(Calendar.MONTH, 8);
	   calendar2.set(Calendar.DAY_OF_MONTH, 8);
	   calendar2.set(Calendar.HOUR_OF_DAY, 17);
	   calendar2.set(Calendar.MINUTE, 0);
	   calendar2.set(Calendar.SECOND, 0);

	   System.out.println(calendar2.getTime());
	   long d = calendar2.getTime().getTime()-calendar1.getTime().getTime();
	   System.out.println(d);
	   
	   Calendar calendar3 = new GregorianCalendar();
	   System.out.println(calendar3.getTime());
	   calendar3.set(Calendar.DAY_OF_WEEK,1);
	   System.out.println(calendar3.getTime());
	}
  
	

}



