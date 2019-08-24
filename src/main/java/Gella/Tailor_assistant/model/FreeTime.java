package Gella.Tailor_assistant.model;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * class contains free time items for planing new orders
 * @author Gella
 *
 */
public class FreeTime {
	/**
	 * start of free time
	 */
 private Date start;
 Settings settings;
 /**
  * the duration of free time
  */
 private long duration;
 
 /**
  * creates free time for today according to the schedule 
  */
 public FreeTime(){
	 try {
		settings=Settings.getInstance();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 init();
  }
 
 /**
  * creates free time for day =today+offset according to the schedule
  * if in day there is no free time creates an object with zero duration
  */
  public FreeTime(int offset){
	  try {
		settings=Settings.getInstance();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  if (offset>0) {
	  GregorianCalendar calendar1 = new GregorianCalendar();
	  GregorianCalendar calendar2 = new GregorianCalendar();
	  calendar2.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), 
			  calendar1.get(Calendar.DAY_OF_MONTH), 0, 0);
	  calendar2.add(Calendar.DAY_OF_YEAR, offset); //TODO handle the transition to the new year
	  calendar1.setTime(settings.getSheduleStart(calendar2.get(Calendar.DAY_OF_WEEK)));
	  calendar1.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH));
	  start= calendar1.getTime();
	  int dayOfWeek = calendar2.get(Calendar.DAY_OF_WEEK);
	  if (settings.getSheduleEnd(dayOfWeek).after(settings.getSheduleStart(dayOfWeek)))
		  duration=settings.getSheduleEnd(dayOfWeek).getTime()-settings.getSheduleStart(dayOfWeek).getTime();
      else duration=0;
	  }
	  else if (offset<0) {start= new Date();duration=0;}
	       else init();
  }
  
public long getDuration() {
		return duration;
}

/**
 * creates the object with current day and time according to week schedule
 */
protected void init() {
	start= new Date();
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(start);
    //get the day of the week
    int dayOfWeek= calendar.get(Calendar.DAY_OF_WEEK);
    //check if there are working hours for this day
    if (settings.getSheduleEnd(dayOfWeek).after(settings.getSheduleStart(dayOfWeek))) {
     //if now we are in the middle of working hours	
    if (start.after(settings.getSheduleStart(dayOfWeek))&& start.before(settings.getSheduleEnd(dayOfWeek))) 
	 //that free time from now until the end of business hours
    	duration= settings.getSheduleEnd(dayOfWeek).getTime()-start.getTime();
    //otherwise if now we are before working hours
    else if (start.before(settings.getSheduleStart(dayOfWeek))) {
	      duration= settings.getSheduleEnd(dayOfWeek).getTime()-settings.getSheduleStart(dayOfWeek).getTime();
	      start=settings.getSheduleStart(dayOfWeek);
         }
         else duration = 0;
      }
  }
public Date getStart() {
	return start;
}

public Date getEnd() {
	return new Date(start.getTime()+duration);
}

/**
 * sets the beginning of free time and recounts the duration. 
 * If the start after the end of free time, the duration is set to 0 or negative 
 * Caution! compliance with the work schedule is not checked
 * @param start new beginning of free time
 */
public void setStart(Date start){
	Date end= new Date(this.start.getTime()+duration);
	this.start=start;
	duration=end.getTime()-this.start.getTime();
	this.start=start;
}

/**
 * sets the end of free time and recounts the duration. 
 * If the start after the end of free time, the duration is set to 0 or negative 
 * Caution! compliance with the work schedule is not checked
 * @param end new beginning of free time
 */
public void setEnd(Date end) {
 duration= end.getTime()-this.start.getTime();	
 }
 }

