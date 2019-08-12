package Gella.Tailor_assistant.App;

import java.awt.EventQueue;
//import java.sql.SQLException;
//import javax.swing.JFrame;
//import org.sqlite.SQLiteException;
//import org.sqlite.*;

import Gella.Tailor_assistant.View.*;
//import controller.DbHandler;
//import model.Order;
//import model.Settings;


public class App {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				    MainWindow window = new MainWindow();
					window.getFrame().setVisible(true);
					}
				
			});
}
}
