package com.contact.service.implementation;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

@Service
public class EmailSendService {

	
	public boolean sendEmail(String recipient,String subject,String body,HttpSession session)
	{
		boolean status=false;
		
		final String from="unr.638@gmail.com";
		final String pass="dybs anih stus kznz";
		
		Properties prop=System.getProperties();
		
		prop.put("mail.smtp.host","smtp.gmail.com");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.auth", "true");
		
		Session ses =Session.getInstance(prop, new javax.mail.Authenticator()
				{
					@Override
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(from,pass);
					}
				}
		);
		
		try
		{
			MimeMessage mes=new MimeMessage(ses);
			
			//set from 
			mes.setFrom(new InternetAddress(from,"RKU"));
			
			//set to
			mes.addRecipient(Message.RecipientType.TO,new InternetAddress(recipient));
			
			
			//set the subject
			mes.setSubject(subject);
			
			//create random otp of 6 digit
			int rand=(new Random()).nextInt(900000);
			
			//add the OTP to the session
			session.setAttribute("otp",rand);
			session.setAttribute("username", recipient);			
			
			//create the body of the mail
			String emailbody="<div style='border:1px solid gray; padding:10px'>"+
			
					"<h3>"+body+"</h3><h2> "+rand+"</h2>"
					
					+ "</div>";

			//			mes.setText("OTP as Password for user="+recipient+" is "+rand);
			mes.setContent(emailbody, "text/html");
			
			
			Transport.send(mes);
			status=true;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return status;
	}
	
}
