package Gella.Tailor_assistant.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import Gella.Tailor_assistant.controller.*;


public class Test {

	public static void main(String[] args) {
		GoogleCalendarController googleCalendarController= GoogleCalendarController.getInstance();
		String dIn = "2019-08-23 08:00:00";
		String dIne = "2019-08-27 20:00:00";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date start = df.parse(dIn);
			Date end =df.parse(dIne);
			googleCalendarController.getBusy(start, end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
  
	

}



