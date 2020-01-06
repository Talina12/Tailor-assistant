package Gella.Tailor_assistant.controller;

import java.util.ArrayList;
import java.util.Date;

import Gella.Tailor_assistant.model.Event;
import Gella.Tailor_assistant.model.FreeTime;
import Gella.Tailor_assistant.model.Order;
import Gella.Tailor_assistant.model.Settings;

/**
 * class for managing time, events  and scheduling
 * @author Gella
 *
 */
public class CalendarController {
 /**
  * variable stores a list of free times for planning new orders
  */
 ArrayList <FreeTime> freeTimes;
 /**
  * how many days from today are checked and transferred to the free time list
  */
 int offset;
 GoogleCalendarController googleCalendarController = GoogleCalendarController.getInstance();
 
 private static CalendarController instance = null;
 
 public static  CalendarController getInstance()  {
     if (instance == null)
         instance = new CalendarController();
     return instance;
	}
 


/**
 * goes through the list of free times from the beginning and returns the date
 *  with free time of suitable duration or several dates with 
 *  shorter duration which in total give the desired duration
 *  if there is no suitable date, the method adds a new day to the list of free times
 *   until  can find the time
 * @see   CalendarController#addTime()
 * @param duration
 * @return Date[]
 */
 public ArrayList<Event> getFreeDates(long duration) {
	ArrayList<Event> dates = new ArrayList<Event>();
	long sum =duration; 
	int i=0;
	setFreeTimes();
	while (sum>0) {
	 if (i>freeTimes.size()-1) addTime();
	 sum=sum-freeTimes.get(i).getDuration();
	 if (sum<0) dates.add(new Event(freeTimes.get(i).getStart(),freeTimes.get(i).getDuration()+sum));
	 else dates.add(new Event(freeTimes.get(i).getStart(),freeTimes.get(i).getDuration()));
	 i++;
	}
	return  dates;
	}

/**
 * sets list of free times for today
 * sets freeTimes to empty list if there are no free time today
 */
 private void setFreeTimes() {
    ArrayList<FreeTime> result = new ArrayList<FreeTime>();
	FreeTime freeTime = new FreeTime();
	System.out.println("freeTYime 0="+freeTime.getStart().toString()+"  "+freeTime.getEnd().toString());
	ArrayList<Date[]> busyTimes;
	//request events from google calendar
	if (freeTime.getDuration()>0) {
	  busyTimes = googleCalendarController.getBusy(freeTime.getStart(), freeTime.getEnd());
	for (Date[] event:busyTimes) {
	 if (freeTime.getStart().before(event[0])) { 
		 FreeTime f= new FreeTime();
		 f.setStart(freeTime.getStart());
		 f.setEnd(event[0]);
		 freeTime.setStart(event[1]);
		 if (f.getDuration()>Settings.getMinEvent()) 
			 result.add(f);
		 if (freeTime.getDuration()<=0) break;
	 }
	 else {freeTime.setStart(event[1]);
	       if (freeTime.getDuration()<=0) break;
	 }
	}
	if (freeTime.getDuration()>Settings.getMinEvent()) 
		result.add(freeTime);
   freeTimes = result;
   offset =0;
}
	else {
		freeTimes= new ArrayList<FreeTime>();
	    offset=0;
	}
}


/**
 * adds a new day to the free time list
 * according to week schedule in Setting
 * @see Settings#weekShedule()
 */
 protected void addTime() {
	boolean notAdded=true;
	do {
	offset=offset+1;
	FreeTime freeTime = new FreeTime(offset);
	if (freeTime.getDuration()>0){
		ArrayList<Date[]> busyTimes = googleCalendarController.getBusy(freeTime.getStart(), freeTime.getEnd());
		for (Date[] event:busyTimes) {
			 if (freeTime.getStart().before(event[0])) { 
				 FreeTime f= new FreeTime();
				 f.setStart(freeTime.getStart());
				 f.setEnd(event[0]);
				 freeTime.setStart(event[1]);
				 if (f.getDuration()>Settings.getMinEvent()) {
					 freeTimes.add(f);
					 notAdded=false;
				 }
				 if (freeTime.getDuration()<=0) break;
			 }
			 else {freeTime.setStart(event[1]);
			       if (freeTime.getDuration()<=0) break;
			 }
			}
		if (freeTime.getDuration()>Settings.getMinEvent()) 
			{freeTimes.add(freeTime);
		    notAdded=false;
		    }
		}
	}
	while (notAdded);
	
}
/*
public Event[] setEvents( double duration, ) {
	ArrayList<Event> dates = new ArrayList<Event>();
	double sum =duration; 
	int i=0;
	while (sum>0) {
	 if (i>freeTimes.size()-1) addTime();
	 sum=sum-freeTimes.get(i).getDuration();
	 dates.add(freeTimes.get(i).getStart());
	 i++;
	}
	return  dates.toArray(new Date[dates.size()]);	
}*/



 /**
  * frees up the time reserved for the order, goes through the list of free times from the beginning and returns the date
  *  with free time of suitable duration or several dates with 
  *  shorter duration which in total give the desired duration
  *  if there is no suitable date, the method adds a new day to the list of free times
  *   until  can find the time
  * @see   CalendarController#addTime()
  * @param duration
  * @return Date[]
  */
public ArrayList<Event> getFreeDates(long duration, Order order) {
	ArrayList<Event> dates = new ArrayList<Event>();
	long sum =duration; 
	int i=0;
	googleCalendarController.setFree(order);
	setFreeTimes();
	while (sum>0) {
	 if (i>freeTimes.size()-1) addTime();
	 sum=sum-freeTimes.get(i).getDuration();
	 if (sum<0) dates.add(new Event(freeTimes.get(i).getStart(),freeTimes.get(i).getDuration()+sum));
	 else dates.add(new Event(freeTimes.get(i).getStart(),freeTimes.get(i).getDuration()));
	 i++;
	}
	googleCalendarController.setBusy(order);
	return  dates;
}
}	    

