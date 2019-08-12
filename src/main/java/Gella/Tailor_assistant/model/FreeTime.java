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
 private double duration;
 
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
  
public double getDuration() {
		return duration;
}

/**
 * creates the object with current day and time according to week schedule
 */
protected void init() {
	start= new Date();
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(start);
    int dayOfWeek= calendar.get(Calendar.DAY_OF_WEEK);
    if (settings.getSheduleEnd(dayOfWeek).after(settings.getSheduleStart(dayOfWeek))) {
    if (start.after(settings.getSheduleStart(dayOfWeek))&& start.before(settings.getSheduleEnd(dayOfWeek))) 
	 duration= settings.getSheduleEnd(dayOfWeek).getTime()-start.getTime();
    else if (start.before(settings.getSheduleStart(dayOfWeek))) 
	      duration= settings.getSheduleEnd(dayOfWeek).getTime()-settings.getSheduleStart(dayOfWeek).getTime();
         else duration = 0;
      }
  }
public Date getStart() {
	return start;
}
 }

