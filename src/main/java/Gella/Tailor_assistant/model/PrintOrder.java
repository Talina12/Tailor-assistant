package Gella.Tailor_assistant.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;

public class PrintOrder implements Printable{

	private Order order;
	
	public PrintOrder(Order order) {
	 this.order=order;	
	}
	
	@Override
	public int print(Graphics g, PageFormat pageFormat, int pageNumber) throws PrinterException {
		int height=(int) Math.round(pageFormat.getImageableHeight());
        int width=(int) Math.round(pageFormat.getImageableWidth());
        int startX=(int)Math.round(pageFormat.getImageableX());
        int startY= (int)Math.round(pageFormat.getImageableY());
        if(pageNumber>0){
			return(Printable.NO_SUCH_PAGE);
		}else{
			Graphics2D g2d;
			g2d=(Graphics2D)g;
			g2d.setFont(new Font("Courier",Font.PLAIN,12));
			//g2d.setPaint(new GradientPaint(50f,100f,Color.red,250f,100f,Color.blue));
			//g2d.drawString("לקוח: ",width/2,height/2);
			int s1=65;
			int s2=84;
			int s3=17;
			SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yy");
			g2d.drawString("הזמנה מס:",startX+width-s1,startY+28);
			g2d.drawString(String.valueOf(order.getOrderNumber()),startX+width-s1-s2,startY+28);
			int ks3=1;
			g2d.drawLine(startX, startY+28+ks3*s3, startX+width, startY+28+ks3*s3);
			ks3++;
			g2d.drawString("לקוח: ",startX+width-s1,startY+28+ks3*s3);
			if (order.getCustomer().getFirstName()!=null)
			 g2d.drawString(order.getCustomer().getFirstName(),startX+width-s1-s2,startY+28+ks3*s3);
			ks3++;
			if (order.getCustomer().getLastName()!=null)
			 g2d.drawString(order.getCustomer().getLastName(), startX+width-s1-s2, startY+28+ks3*s3);
			ks3++;
			g2d.drawString("טלפון:", startX+width-s1, startY+28+ks3*s3);
			if (order.getCustomer().getCellphone()!=null)
			 g2d.drawString(order.getCustomer().getCellphone(), startX+width-s1-s2, startY+28+ks3*s3);
			ks3++;
			if (order.getCustomer().getHomePhone()!=null)
			 g2d.drawString(order.getCustomer().getHomePhone(), startX+width-s1-s2, startY+28+ks3*s3);
			ks3++;
			g2d.drawLine(startX, startY+28+ks3*s3, startX+width, startY+28+ks3*s3);
			ks3++;
			g2d.drawString("התקבל ב:", startX+width-s1, startY+28+ks3*s3);
			if (order.getRecDate()!=null)
			 g2d.drawString(formatDate.format(order.getRecDate()), startX+width-s1-s2, startY+28+ks3*s3);
			ks3++;
			g2d.drawLine(startX, startY+28+ks3*s3, startX+width, startY+28+ks3*s3);
			ks3++;
			for (int i=0; i<order.getDescription().size();i++,ks3++) {
				DescriptionRow d;
				d=order.getDescription().get(i);
				if (d.getPrice()!=null)
				 g2d.drawString(d.getPrice().toString(), startX+width-s1, startY+28+ks3*s3);
				if (d.getItem()!=null)
				 g2d.drawString(d.getItem(), startX+width-s1-s2-56, startY+28+ks3*s3);
				
			}
			ks3++;
			g2d.drawString("סה\"כ", startX+width-s1, startY+28+ks3*s3);
			g2d.drawString(String.valueOf(order.getTotalPrice()), startX+width-s1-s2, startY+28+ks3*s3);
			ks3++;
			g2d.drawString("שולם:", startX+width-s1, startY+28+ks3*s3);
			g2d.drawString(String.valueOf(order.getPaid()), startX+width-s1-s2, startY+28+ks3*s3);
			ks3++;
			g2d.drawLine(startX, startY+28+ks3*s3, startX+width, startY+28+ks3*s3);
			ks3++;
			g2d.drawString("מוכן עד:", startX+width-s1, startY+28+ks3*s3);
			if (order.getIssueDate()!=null)
			 g2d.drawString(formatDate.format(order.getIssueDate()), startX+width-s1-s2, startY+28+ks3*s3);
		}
		return(Printable.PAGE_EXISTS);
	}

}
