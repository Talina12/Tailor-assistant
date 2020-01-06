package Gella.Tailor_assistant.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
	// Find your Account Sid and Auth Token at twilio.com/console
    
	public SmsSender(String accountSid, String authToken, String phoneNumber) {
		ACCOUNT_SID=accountSid;
		AUTH_TOKEN=authToken;
		PHONE_NUMBER=phoneNumber;
	}
	private  final String ACCOUNT_SID ;
    private  final String AUTH_TOKEN;
    private final String PHONE_NUMBER;

    public void send(String mes,String num) {
    	Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
                .creator(new PhoneNumber("+972"+num.substring(1)), // to
                        new PhoneNumber(PHONE_NUMBER), // from
                      mes)
                .create();
       
    }
}
//"שלום, הזמנה שלך מוכנה "+'\n'+"בברכה זיגזג אומנות התפירה"