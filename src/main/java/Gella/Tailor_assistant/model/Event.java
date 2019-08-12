package Gella.Tailor_assistant.model;

import java.util.Date;

public class Event {
	Date start;
	double duration; 
	int orderId;
	String name;
	
public Event() {
	start=new Date();
	name= new String();
}

public Event(Date start, double duration) {
	this();
	this.start=start;
	this.duration=duration;
}

public void setStart(Date start) {
	this.start = start;
}

public void setDuration(double duration) {
	this.duration = duration;
}

public void setOrderId(int orderId) {
	this.orderId = orderId;
}

public void setName(String name) {
	this.name = name;
}

public Date getStart() {
	return start;
}

public double getDuration() {
	return duration;
}

public int getOrderId() {
	return orderId;
}

public String getName() {
	return name;
}


}