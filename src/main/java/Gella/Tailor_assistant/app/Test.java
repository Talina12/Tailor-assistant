package Gella.Tailor_assistant.app;

import java.sql.SQLException;

import Gella.Tailor_assistant.controller.*;
import Gella.Tailor_assistant.model.Event;


public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			GoogleCalendarController googleCalendarController= GoogleCalendarController.getInstance();
			Event ev = new Event();
			ev.setGoogleId(googleCalendarController.GetTestEvent());
			googleCalendarController.test(ev);
			ev.setGoogleId(ev.getGoogleId()+"rt");
			googleCalendarController.test(ev);
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
  
	

}



