package Gella.Tailor_assistant.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;

import Gella.Tailor_assistant.model.Settings;


public class GoogleCalendarController {
	private static GoogleCalendarController instance = null;
	private static final String APPLICATION_NAME= Settings.getApplicationName();
	private static final String CALENDAR_NAME= Settings.getCalendarName(); 
	private Calendar workingCalendar;
	
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
		  try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport(); 
	     // initialize the data store factory
	        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
         // authorization
	        Credential credential = authorize();
	     // set up global Calendar instance
		      client = new com.google.api.services.calendar.Calendar.Builder(
		          httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
		      
	      }catch (IOException e) {
	          //System.err.println(e.getMessage());
	    	  log.severe(e.getMessage()+" IOException");
	      }catch (Throwable t) {
	         // t.printStackTrace();
	    	  log.severe(t.getMessage()+" Throwable");
	      }
		  client.f
	  }
	  
	  public static synchronized GoogleCalendarController getInstance() throws SQLException {
	        if (instance == null)
	            instance = new GoogleCalendarController();
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
     			if (listEentry.getSummary().equals(CALENDAR_NAME)) 
		    	{ log.info(" the calendar exist");
		    	return client.calendars().get(listEentry.getId()).execute();}
		    	}
		    	}
		    log.info("Add calendar"+" "+CALENDAR_NAME);
		    Calendar entry = new Calendar();
		    entry.setSummary(CALENDAR_NAME);
		    Calendar result = client.calendars().insert(entry).execute();
		    return result;
		     }	  
}
