package Gella.Tailor_assistant.app;

import java.awt.EventQueue;
import java.awt.print.PrinterJob;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import Gella.Tailor_assistant.View.MainWindow;
import Gella.Tailor_assistant.View.MainWindow2;
import Gella.Tailor_assistant.controller.*;
import Gella.Tailor_assistant.model.Customer;
import Gella.Tailor_assistant.model.Order;
import Gella.Tailor_assistant.model.PrintOrder;
import Gella.Tailor_assistant.model.DescriptionRow;;


public class Test {

 public static void main(String[] args) {
	Order order1=new Order();
	Order order2=new Order();
	ArrayList<DescriptionRow> des=new ArrayList<DescriptionRow>();
	DescriptionRow dr1=new DescriptionRow();
	dr1.setItem("юбка ушить");
	dr1.setPrice(80);
	DescriptionRow dr2=new DescriptionRow();
	dr2.setItem("пиджак укоротить");
	dr2.setPrice(30);
	DescriptionRow dr3=new DescriptionRow();
	dr3.setItem("עכעיכעחעיעח");
	dr3.setPrice(150);
	Customer customer1=new Customer();
	Customer customer2=new Customer();
	customer1.setFirstName("נננננננננננ");
	customer1.setLastName("בבבבבבבבב");
	customer1.setCellphone("1233445");
	
	des.add(dr1);
	des.add(dr2);
	des.add(dr3);
	order1.setDescription(des);
	order1.setRecDate(new Date());
	order1.setTotalPrice(250);
	order1.setIssueDate(new Date());
	
	customer2.setFirstName("yyyyyyyy");
	customer2.setLastName("qqqqqqq");
	customer2.setHomePhone("666666666");
	order1.setCustomer(customer1);
	order2.setCustomer(customer2);
	PrinterJob printerJob=PrinterJob.getPrinterJob();
	 if (printerJob!=null) {
		 //print order
		 printerJob.setPrintable(new PrintOrder(order1));
			try{
				printerJob.print();
				}catch(Exception e){ System.out.print((e.getMessage()));}
	 }
	 else JOptionPane.showMessageDialog(null," Нет принтера для печати");
 }
}



