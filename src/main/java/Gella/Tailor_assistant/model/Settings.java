package Gella.Tailor_assistant.model;

import java.awt.Color;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//import Gella.Tailor_assistant.controller.*;

//TODO Singleton
 public  class Settings {
 private  int HOUR_RATING;
 /**
  * weekly work schedule. the first value is the beginning of working hours in milliseconds from 
  * the beginning of the day
  * the second value is the end of working hours in milliseconds from the beginning of the day
 */
 private static Date[][] weekShedule; 
 private static Settings instance = null;
 private static final String  CON_STR="jdbc:sqlite:./tailor.db";
 private static final String APPLICATION_NAME = "Tailor";
 private static final String CALENDAR_NAME = "Tailor calendar";
 private static final java.io.File DATA_STORE_DIR =
	      new java.io.File(System.getProperty("user.dir"), ".store/"+APPLICATION_NAME);
 private static final long minEvent=900000;
 
 public static synchronized Settings getInstance() throws SQLException {
     if (instance == null)
         instance = new Settings();
     return instance;
	}
  
 public Settings() {
	 HOUR_RATING=100;
	 weekShedule=new Date[7][2];
	 
	 Calendar calendar = new GregorianCalendar();
	 calendar.set(Calendar.DAY_OF_WEEK, 1);
	 calendar.set(Calendar.HOUR_OF_DAY, 9);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[0][0]=calendar.getTime();
	 calendar.set(Calendar.HOUR_OF_DAY, 14);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[0][1]=calendar.getTime();
	 
	 calendar.set(Calendar.DAY_OF_WEEK, 2);
	 calendar.set(Calendar.HOUR_OF_DAY, 9);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[1][0]=calendar.getTime();
	 calendar.set(Calendar.HOUR_OF_DAY, 14);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[1][1]=calendar.getTime();
	 
	 calendar.set(Calendar.DAY_OF_WEEK, 3);
	 calendar.set(Calendar.HOUR_OF_DAY, 9);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[2][0]=calendar.getTime();
	 calendar.set(Calendar.HOUR_OF_DAY, 14);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[2][1]=calendar.getTime();
	 
	 calendar.set(Calendar.DAY_OF_WEEK, 4);
	 calendar.set(Calendar.HOUR_OF_DAY, 9);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[3][0]=calendar.getTime();
	 calendar.set(Calendar.HOUR_OF_DAY, 14);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[3][1]=calendar.getTime();
	 
	 calendar.set(Calendar.DAY_OF_WEEK, 5);
	 calendar.set(Calendar.HOUR_OF_DAY, 9);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[4][0]=calendar.getTime();
	 calendar.set(Calendar.HOUR_OF_DAY, 14);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[4][1]=calendar.getTime();
	 
	 calendar.set(Calendar.DAY_OF_WEEK, 6);
	 calendar.set(Calendar.HOUR_OF_DAY, 11);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[5][0]=calendar.getTime();
	 calendar.set(Calendar.HOUR_OF_DAY, 11);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[5][1]=calendar.getTime();
	 
	 calendar.set(Calendar.DAY_OF_WEEK, 7);
	 calendar.set(Calendar.HOUR_OF_DAY, 8);
	 calendar.set(Calendar.MINUTE, 0);
	 calendar.set(Calendar.SECOND, 0);
	 weekShedule[6][0]=calendar.getTime();
	 weekShedule[6][1]=calendar.getTime();
 };
 
 public int getHourRating() {return HOUR_RATING;}

 public void setHourRating(int i) {HOUR_RATING=i;}

 /**
  * returns the end of the working hours in milliseconds from the beginning of the day
 */
 public  Date getSheduleEnd(int dayOfWeek) {
	return weekShedule[dayOfWeek-1][1];
	}

 /**
  returns the beginning of the working hours in milliseconds from the beginning of the day
   */
public Date getSheduleStart(int dayOfWeek) {
	return weekShedule[dayOfWeek-1][0];
}

public static String getCON_STR() {
	return CON_STR;
}

public static String getApplicationName() {
	return APPLICATION_NAME;
}

public static String getCalendarName() {
	return CALENDAR_NAME;
}

public static java.io.File getDataStoreDir() {
	return DATA_STORE_DIR;
}

public static long getMinEvent() {
	return minEvent;
}





}
