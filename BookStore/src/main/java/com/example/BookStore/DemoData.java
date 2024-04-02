package com.example.BookStore;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.BookStore.entity.Account;
import com.example.BookStore.entity.Role;
import com.example.BookStore.repo.AccountRepo;
import com.example.BookStore.repo.RoleRepo;

@Component
public class DemoData implements ApplicationRunner{

	@Autowired
	RoleRepo roleRepo;
	
	@Autowired 
	AccountRepo accountRepo;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		Role role = new Role();
		role.setName("ADMIN");
		
		if(roleRepo.findByName(role.getName()) ==  null) {
			roleRepo.save(role);
			
			//Tạo account
			Account account = new Account();
			account.setUserName("admin");
			account.setPassWord(new BCryptPasswordEncoder().encode("123456"));
			account.setStatus("Enable");
			account.setRoles(Arrays.asList(role));
			
			accountRepo.save(account);
		}
	}

}
