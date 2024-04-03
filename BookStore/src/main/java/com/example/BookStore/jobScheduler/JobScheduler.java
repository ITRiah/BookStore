package com.example.BookStore.jobScheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.BookStore.entity.User;
import com.example.BookStore.repo.UserRepo;
import com.example.BookStore.service.EmailService;

@Component
public class JobScheduler {
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	EmailService emailService;
	
	//@Scheduled(cron = "* * 8 * * *")
	public void sendMail() {
		LocalDate date = LocalDate.now();
		int month = date.getMonthValue();
		int day = date.getDayOfMonth();		

		List<User> users = userRepo.searchByBirthDay(day, month);
		
		for (User user : users) {
			emailService.sendBirthDay(user.getEmail(), user.getName());
		}		
	}
}
