package com.example.BookStore;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.BookStore.entity.Account;
import com.example.BookStore.entity.Cart;
import com.example.BookStore.entity.Role;
import com.example.BookStore.entity.User;
import com.example.BookStore.repo.AccountRepo;
import com.example.BookStore.repo.CartRepo;
import com.example.BookStore.repo.RoleRepo;
import com.example.BookStore.repo.UserRepo;

@Component
public class DemoData implements ApplicationRunner{

	@Autowired
	RoleRepo roleRepo;
	
	@Autowired 
	AccountRepo accountRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	CartRepo cartRepo;
		
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
			
			//Tao user
			User user = new User();
			user.setAccount(account);
			userRepo.save(user);
			
			//Tao cart chi user
			Cart cart = new Cart();
			cart.setUser(user);
			cartRepo.save(cart);
		}
	}

}
