package Gella.Tailor_assistant.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar.Freebusy;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.ColorDefinition;
import com.google.api.services.calendar.model.Colors;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.FreeBusyCalendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;

import Gella.Tailor_assistant.model.Order;
import Gella.Tailor_assistant.model.Settings;

//TODO check all possible exceptions and display messages for user
public class GoogleCalendarController {
 private static GoogleCalendarController instance = null;
 private static final String APPLICATION_NAME= Settings.getApplicationName();
 private static final String CALENDAR_NAME= Settings.getCalendarName(); 
 private Calendar workingCalendar;
 DbHandler dbHandler;
	
 /** Directory to store user credentials. */
 private static final   java.io.File DATA_STORE_DIR= Settings.getDataStoreDir();
	  
 /**
 * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
 * globally shared instance across your application.
 */
 private static FileDataStoreFactory dataStoreFactory;
	  
 /** Global instance of the HTTP transport. */
 private static HttpTransport httpTransport;
	  
 /** Global instance of the JSON factory. */
 private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
 private static com.google.api.services.calendar.Calendar client;
 public static Logger log ;
 /** Authorizes the installed application to access user's protected data. */
 private static Credential authorize() throws Exception {
  // load client secrets
  GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
  new InputStreamReader(GoogleCalendarController.class.getResourceAsStream("/client_secrets.json")));
   if (clientSecrets.getDetails().getClientId().startsWith("Enter")
	    || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
	      System.out.println(
	        "Enter Client ID and Secret from https://code.google.com/apis/console/?api=calendar "
	         + "into calendar-cmdline-sample/src/main/resources/client_secrets.json");
	      System.exit(1);
   }
  // set up authorization code flow
  GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, JSON_FACTORY, clientSecrets,
	        Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(dataStoreFactory)
	        .build();
	    // authorize
   return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }
	  
  public GoogleCalendarController(){
	// initialize the transport
	log = Logger.getLogger("controller.GoogleCalendarController");
    try {dbHandler=DbHandler.getInstance();
		 httpTransport = GoogleNetHttpTransport.newTrustedTransport(); 
	     // initialize the data store factory
	     dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
         // authorization
	     Credential credential = authorize();
	     // set up global Calendar instance
		 client = new com.google.api.services.calendar.Calendar.Builder(
		          httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
		 workingCalendar=addCalendarIfNotExist();   
	     }catch (IOException e) {
	          //System.err.println(e.getMessage());
	    	  log.severe(e.getMessage()+" IOException");
	      }catch (Throwable t) {
	         // t.printStackTrace();
	    	  log.severe(t.getMessage()+" Throwable");
	      }
		  }
	  
  public static synchronized GoogleCalendarController getInstance() {
   if (instance == null) instance = new GoogleCalendarController();
   return instance;
  }	  

  /**
  * creates calendar "Tailor" if it not exist
  * @return created calendar or existing calendar
  * @throws IOException if failed to create
  */
  public Calendar addCalendarIfNotExist() throws IOException {
   CalendarList list =client.calendarList().list().execute();
   List<CalendarListEntry> calendarList = list.getItems();
   if (calendarList!=null) {
     ListIterator<CalendarListEntry> calendarListIterator = calendarList.listIterator();
	 CalendarListEntry listEentry;
	 while (calendarListIterator.hasNext()) {
	   listEentry=calendarListIterator.next();
	 // System.out.println(listEentry.getSummary());
       if (listEentry.getSummary().equals(CALENDAR_NAME)) 
		 { log.info(" the calendar exist");
		   return client.calendars().get(listEentry.getId()).execute();
		 }
	 }
   }
   log.info("Add calendar"+" "+CALENDAR_NAME);
   Calendar entry = new Calendar();
   entry.setSummary(CALENDAR_NAME);
   Calendar result = client.calendars().insert(entry).execute();
   return result;
 }
	  
  public void synchronize() {
	try {
	//request all events from the working calendar
		Events events= client.events().list(workingCalendar.getId()).execute();
	//	Events events= client.events().list(workingCalendar.getId()).execute();
	///	System.out.println(events.getDescription());
		List<com.google.api.services.calendar.model.Event> googleEvents = events.getItems();
				//client.events().list(workingCalendar.getId()).execute().getItems();
	//request a list of all events from the database	
		ArrayList<Gella.Tailor_assistant.model.Event> localEvents=dbHandler.getEvents();
		Boolean[] ifPlanned = new Boolean[localEvents.size()];
		for (int i=0;i<ifPlanned.length;i++)
			ifPlanned[i]=false;
	//if the list is not empty
	    if (googleEvents!=null) {
	    //	for each item in the list:
	     for (com.google.api.services.calendar.model.Event ge:googleEvents) {
	     Boolean b=false;
	      // check if there is a corresponding event in the database
	      for(int i=0;i<localEvents.size();i++) {
	    	  if (localEvents.get(i).getGoogleId().equals(ge.getId())) {
	    		  //mark that this event in the database has a corresponding event in google calendar
	    		  ifPlanned[i]=true;
	              //set the data of event in the database to the data of event in google calendar
	    		  dbHandler.updateEvent(setLocalEvent(localEvents.get(i),ge));
	    		  //mark that corresponding event in the database is found
	              b=true;
	              if (!localEvents.get(i).getColorId().equals(ge.getColorId())){
	               ge.setColorId(localEvents.get(i).getColorId());
	            	  client.events().update(workingCalendar.getId(), ge.getId(), ge).execute();
	              }
	              i=localEvents.size();
	              };
	      }
	     if (!b) client.events().delete(workingCalendar.getId(),ge.getId()).execute();
	     
		 }
	    }
	    //go through all the events from the database that do not have a corresponding event in google calendar
	    //and add the events to google calendar
	    for(int i=0;i<ifPlanned.length;i++)  
	    	if (!(ifPlanned[i]))
	    		addEvent(localEvents.get(i));
	} catch (IOException e){
		log.severe(e.getMessage()+"  "+e.getClass().toString());
	}
}
  
 private Gella.Tailor_assistant.model.Event setLocalEvent
      (Gella.Tailor_assistant.model.Event locEv,com.google.api.services.calendar.model.Event ge) {
   log.info("setLocalEvent");
   locEv.setStart(new Date(ge.getStart().getDateTime().getValue()));
   long duration =ge.getEnd().getDateTime().getValue()-ge.getStart().getDateTime().getValue();
   locEv.setDuration(duration);
   locEv.setName(ge.getSummary());
   locEv.setGoogleId(ge.getId());
   locEv.setDescription(ge.getDescription());
   locEv.setColorId(dbHandler.getOrderById(locEv.getOrderId()).getIssueDate());
   return locEv;
}

/*
 public void synchronizeLocalToGoogle() {
	  ArrayList<Gella.Tailor_assistant.model.Event> localEvents  = dbHandler.getEvents();
	  for(Gella.Tailor_assistant.model.Event lev:localEvents) {
		  com.google.api.services.calendar.model.Event ge;  
		  try {
			ge =client.events().get(workingCalendar.getId(), lev.getGoogleId()).execute();
			if ((ge.getStart()==null)||(ge.getStatus().compareTo("cancelled")==0))//==null||ge.getEnd()==null||ge.getId()==null) 
				addEvent(lev);	
			else  dbHandler.updateEvent(setLocalEvent(lev, ge));
			
		  }
		  catch ( GoogleJsonResponseException e) {
			log.info(e.getMessage()+" "+ e.getClass().toString());
			addEvent(lev);
   		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
  }
  */
  
  public com.google.api.services.calendar.model.Event addEvent(Gella.Tailor_assistant.model.Event ev) {
	log.info("add event");
	  com.google.api.services.calendar.model.Event event = setGoogleEvent(new com.google.api.services.calendar.model.Event(), ev);
    try {
		 Event  result = client.events().insert(workingCalendar.getId(), event).execute();
		 log.info("added  "+result.getSummary());
		 ev.setGoogleId(result.getId());
		 dbHandler.updateEvent(ev);
		  return result;
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
	return null;
  }
  
  public com.google.api.services.calendar.model.Event setGoogleEvent
           (com.google.api.services.calendar.model.Event ge,Gella.Tailor_assistant.model.Event locEv){
	  Date startDate =locEv.getStart();
	  DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
	  ge.setStart(new EventDateTime().setDateTime(start));
	  Date endDate = locEv.getEnd();
	  DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
	  ge.setEnd(new EventDateTime().setDateTime(end));
	  ge.setSummary(locEv.getName());
	  ge.setDescription(locEv.getDescription());
	  ge.setColorId(locEv.getColorId());
	  return ge;
  }
  
  /**
   * returns the beginning and end of busy periods in all calendars. 
   * result sorted
   * @param start start of checking period
   * @param end end of checking period
   * @return ArrayList of Date[2] in which Date[0] is a start of busy period 
   * and Date[1] is an end of busy period
   */
  public ArrayList<Date[]> getBusy(Date start, Date end) {
	  FreeBusyRequest req = new FreeBusyRequest();
	  DateTime startTime = new DateTime(start, TimeZone.getDefault());
	  DateTime endTime = new DateTime(end, TimeZone.getDefault());
	  req.setTimeMin(startTime);
	  req.setTimeMax(endTime);
	  CalendarList list;
	try {
		list = client.calendarList().list().execute();
	    ArrayList<Date[]> result = new  ArrayList<Date[]>();
		List<CalendarListEntry> calendarList = list.getItems();
	    List<FreeBusyRequestItem> items = new ArrayList<FreeBusyRequestItem>();
	    for (CalendarListEntry c:calendarList) 
	      items.add(new FreeBusyRequestItem().setId(c.getId()));
	    req.setItems(items);
	    //execute the request
	    FreeBusyResponse fbq = client.freebusy().query(req).execute();
	    Map<java.lang.String,FreeBusyCalendar> map =fbq.getCalendars();
	    for (CalendarListEntry c:calendarList) {
	    	List<TimePeriod> timePeriods= map.get(c.getId()).getBusy();
	    	for (TimePeriod tp:timePeriods) {
	    	 Date[] period = new Date[2];
	    	 period[0]=new Date(tp.getStart().getValue());
	    	 period[1]= new Date(tp.getEnd().getValue());
	    	 result.add(period);
	    	}
	     }
	    //sort the result
	    Collections.sort(result, new Comparator<Date[]>() {
            @Override
			public int compare(Date[] o1, Date[] o2) {
				return o1[0].compareTo(o2[0]);
			}
		});
	 /*   for(Date[] d:result) 
	    	System.out.println(d[0].toString()+"   "+d[1].toString()); */
	    return result;
	    } catch (IOException e) {
		JOptionPane.showMessageDialog(null,e.getClass().toString()+"   unable to request freeBusy");
		log.severe(e.getMessage() +"  "+e.getClass().toString());
	}
	return null;
  }

/**sets all events of the order to free
 * @return number of updated events
 * **/
 public int setFree(Order order) {
	ArrayList<Gella.Tailor_assistant.model.Event> events= dbHandler.getEventsByOrderId(order.getOrderNumber());
	int i=0;
	for(Gella.Tailor_assistant.model.Event ev:events) {
		try {
			// Retrieve the event from the API
			com.google.api.services.calendar.model.Event gev = client.events().get(workingCalendar.getId(),ev.getGoogleId()).execute();
			// Make a change
			gev.setTransparency("transparent");
			// Update the event
			client.events().update(workingCalendar.getId(), gev.getId(), gev).execute();
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,e.getClass().toString()+"   unable to update event");
			log.severe(e.getMessage() +"  "+e.getClass().toString());
		}
	}
	i++;
	return i;
}

 /**sets all events of the order to free
  * @return number of updated events
  * **/
public int setBusy(Order order) {
	ArrayList<Gella.Tailor_assistant.model.Event> events= dbHandler.getEventsByOrderId(order.getOrderNumber());
	int i=0;
	for(Gella.Tailor_assistant.model.Event ev:events) {
		try {
			// Retrieve the event from the API
			com.google.api.services.calendar.model.Event gev = client.events().get(workingCalendar.getId(),ev.getGoogleId()).execute();
			// Make a change
			gev.setTransparency("opaque");
			// Update the event
			client.events().update(workingCalendar.getId(), gev.getId(), gev).execute();
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,e.getClass().toString()+"   unable to update event");
			log.severe(e.getMessage() +"  "+e.getClass().toString());
		}
	}
	i++;
	return i;
}
}
