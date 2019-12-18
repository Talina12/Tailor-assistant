package Gella.Tailor_assistant.app;

import java.awt.EventQueue;
import Gella.Tailor_assistant.View.*;
import Gella.Tailor_assistant.controller.GoogleCalendarController;

public class App {
  public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			 GoogleCalendarController googleCalendarController=GoogleCalendarController.getInstance();	
				googleCalendarController.synchronizeGoogleToLocal();
				googleCalendarController.synchronizeLocalToGoogle();
				    MainWindow2 window = new MainWindow2();
					window.getFrame().setVisible(true);
					}
				
			});
}
}
