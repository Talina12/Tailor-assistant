package Gella.Tailor_assistant.model;

import java.util.Date;

public class Event {
	int id;
	Date start;
	long duration; 
	int orderId;
	String name;
	String googleId;
	String description;
	String colorId;
	
public Event() {
	start=new Date();
	name= new String();
	id=0;
	duration=0;
	orderId=0;
	googleId=new String();
	description = new String();
	colorId="8";
}

public Event(Date start, long duration) {
	this();
	this.start=start;
	this.duration=duration;
}

public void setStart(Date start) {
	this.start = start;
}

public void setDuration(long duration) {
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

public long getDuration() {
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

public Date getEnd() {
	return (new Date(start.getTime()+duration));
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public String getColorId() {
	return colorId;
}

public void setColorId(Date date) {
	String color="";
	if (date!=null) {
	long gap=date.getTime()-(this.start.getTime()+this.duration) ;
	if (gap>259200000) color="9";
        else 
      	if (gap>172800000)color="7";
      	else
      	  if (gap>86400000) color="5";
      	  else
      		if (gap>0)  color ="4";
      		else
      			if(gap>-64800000) color="6";
      			else color="11";
        }
     else color="10";
	colorId=color;
}

public void setColorId() {
	// TODO Auto-generated method stub
}

public void setColorId(String string) {
	this.colorId=string;
}
}