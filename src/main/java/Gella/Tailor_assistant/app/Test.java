package Gella.Tailor_assistant.app;

import java.awt.EventQueue;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Gella.Tailor_assistant.View.MainWindow;
import Gella.Tailor_assistant.View.MainWindow2;
import Gella.Tailor_assistant.controller.*;


public class Test {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			 
				    MainWindow2 window = new MainWindow2();
					window.getFrame().setVisible(true);
					}
				
			});
}
}



