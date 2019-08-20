package Gella.Tailor_assistant.app;

import java.awt.EventQueue;
//import java.sql.SQLException;
//import javax.swing.JFrame;
//import org.sqlite.SQLiteException;
//import org.sqlite.*;
import java.sql.SQLException;

import Gella.Tailor_assistant.View.*;
import Gella.Tailor_assistant.controller.GoogleCalendarController;
//import controller.DbHandler;
//import model.Order;
//import model.Settings;


public class App {
  public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			 GoogleCalendarController googleCalendarController=GoogleCalendarController.getInstance();	
				googleCalendarController.synchronizeGoogleToLocal();
				googleCalendarController.synchronizeLocalToGoogle();
				    MainWindow window = new MainWindow();
					window.getFrame().setVisible(true);
					}
				
			});
}
}
