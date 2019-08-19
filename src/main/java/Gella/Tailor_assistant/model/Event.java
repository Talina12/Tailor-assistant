package Gella.Tailor_assistant.model;

import java.util.Date;

public class Event {
	int id;
	Date start;
	double duration; 
	int orderId;
	String name;
	String googleId;
	
public Event() {
	start=new Date();
	name= new String();
	id=0;
	duration=0;
	orderId=0;
	googleId=new String();
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

public String getGoogleId() {
	return googleId;
}

public void setGoogleId(String googleId) {
	this.googleId = googleId;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}



}