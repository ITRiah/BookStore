package com.example.BookStore.service;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	SpringTemplateEngine templateEngine;
	
	public void sendMail(String subject, String body, String to) {	
		//sendMail
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message , StandardCharsets.UTF_8.name());
		
		try {
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			helper.setFrom("vhai31102002@gmail.com");
			
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	
	public void sendBirthDay(String to, String name) {
		String subject = "Happy birthday " + name;
		
		//Gán biến cho view
		Context context = new Context();
		context.setVariable("name", name);
		
		//Tạo body cho mail
		String body = templateEngine.process("./hpbd.html", context);
		
		//sendMail
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message , StandardCharsets.UTF_8.name());
		
		try {
			
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			helper.setFrom("vhai31102002@gmail.com");
			
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
}
