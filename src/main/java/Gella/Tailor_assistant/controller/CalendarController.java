package Gella.Tailor_assistant.controller;

import java.util.ArrayList;
import Gella.Tailor_assistant.model.Event;
import Gella.Tailor_assistant.model.FreeTime;

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
 
 private static CalendarController instance = null;
 
 public static  CalendarController getInstance()  {
     if (instance == null)
         instance = new CalendarController();
     return instance;
	}
 

 /**
  * creates an object and fills  a free time sheet for 10 days in advance
  */
 public CalendarController(){
	offset=10;
	freeTimes = new ArrayList <FreeTime>();
	for (int i=0;i<offset;i++) {
		FreeTime t = new FreeTime(i);
	    if (t.getDuration()>0)  //do not add objects with zero duration
	    	freeTimes.add(t);
}
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
 public Event[] getFreeDates(double duration) {
	ArrayList<Event> dates = new ArrayList<Event>();
	double sum =duration; 
	int i=0;
	while (sum>0) {
	 if (i>freeTimes.size()-1) addTime();
	 
	 sum=sum-freeTimes.get(i).getDuration();
	 dates.add(new Event(freeTimes.get(i).getStart(),freeTimes.get(i).getDuration()));
	 i++;
	}
	return  dates.toArray(new Event[dates.size()]);
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
	FreeTime t = new FreeTime(offset);
	if (t.getDuration()>0)
    	{freeTimes.add(t);
    	  notAdded=false;}
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
}	    

