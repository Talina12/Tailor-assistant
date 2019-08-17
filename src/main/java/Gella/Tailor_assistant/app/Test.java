package Gella.Tailor_assistant.app;

import java.io.IOException;
import java.sql.SQLException;

import Gella.Tailor_assistant.controller.*;


public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			GoogleCalendarController googleCalendarController= GoogleCalendarController.getInstance();
			googleCalendarController.addCalendarIfNotExist();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException i) {
			i.printStackTrace();
		}
		
	}
  
	

}



