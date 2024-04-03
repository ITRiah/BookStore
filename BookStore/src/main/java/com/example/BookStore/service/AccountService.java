package com.example.BookStore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.AccountDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.Account;
import com.example.BookStore.entity.Cart;
import com.example.BookStore.entity.Role;
import com.example.BookStore.entity.User;
import com.example.BookStore.repo.AccountRepo;
import com.example.BookStore.repo.CartRepo;
import com.example.BookStore.repo.UserRepo;

import jakarta.persistence.NoResultException;

public interface AccountService {
	public void create(AccountDTO accountDTO);

	public void update(AccountDTO accountDTO);

	public void delete(int id);
	
	public AccountDTO findByUsername(String username);

	public void forgotPassword(String username);
	
	public void resetPassword(String username, String password);

	public Page<AccountDTO> getAll(SearchDTO searchDTO);

	public Page<AccountDTO> search(SearchDTO searchDTO);

	@Service
	public class AccountServiceImpl implements AccountService, UserDetailsService {

		@Autowired
		AccountRepo accountRepo;

		@Autowired
		UserRepo userRepo;

		@Autowired
		CartRepo cartRepo;

		@Autowired
		EmailService emailService;

		@Override
		public void create(AccountDTO accountDTO) {
			Account account = new ModelMapper().map(accountDTO, Account.class);
			account.setPassWord(new BCryptPasswordEncoder().encode(account.getPassWord())); // nên convert khi lưu db
			accountRepo.save(account);

			// Tao user
			User user = new User();
			user.setAccount(account);
			userRepo.save(user);

			// Tao cart chi user
			Cart cart = new Cart();
			cart.setUser(user);
			cartRepo.save(cart);
		}

		@Override
		public void update( AccountDTO accountDTO) {
			Account accountCurrent = accountRepo.findById(accountDTO.getId()).orElseThrow(NoResultException::new);
			accountCurrent = new ModelMapper().map(accountDTO, Account.class);
			accountCurrent.setPassWord(new BCryptPasswordEncoder().encode(accountCurrent.getPassWord())); // nên convert
																											// khi lưu
																											// db
			accountRepo.save(accountCurrent);
		}

		@Override
		public void delete(int id) {
			accountRepo.deleteById(id);
		}

		@Override
		public Page<AccountDTO> getAll(SearchDTO searchDTO) {

			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage();
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();

			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();

			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<Account> page = accountRepo.findAll(pageRequest);

			Page<AccountDTO> page2 = page.map(account -> new ModelMapper().map(account, AccountDTO.class));

			return page2;
		}

		@Override
		public Page<AccountDTO> search(SearchDTO searchDTO) {
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage();
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			String username = searchDTO.getKeyword() != null ? "" : searchDTO.getKeyword();

			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();

			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<Account> page = accountRepo.searchByUsername("%" + username + "%", pageRequest);

			Page<AccountDTO> page2 = page.map(account -> new ModelMapper().map(account, AccountDTO.class));

			return page2;
		}

		@Override
		public AccountDTO findByUsername(String username) {
			Account account = accountRepo.findByUserName(username);
			if(account == null) {
				return null;
			}
			AccountDTO accountDTO = new ModelMapper().map(account, AccountDTO.class);
			return accountDTO;
		}

		@Override
		public void forgotPassword(String username) {
			Account account = accountRepo.findByUserName(username);

			if (account != null) {

				// Tao password moi
				Random random = new Random();
				int randomNumber = random.nextInt(900000) + 100000;
				String password = randomNumber + "";

				// save account
				account.setPassWord(new BCryptPasswordEncoder().encode(password));
				accountRepo.save(account);

				// send mail
				User user = userRepo.findByAccountId(account.getId());
				String email = user.getEmail();
				emailService.sendMail("Yêu cầu lấy lại mật khẩu", "Mật khẩu mới của bạn là: " + "<h1>" + password + "<h1>", email);
			}
		}
		
		@Override
		public void resetPassword(String username, String password) {
			// TODO Auto-generated method stub
			Account account = accountRepo.findByUserName(username);
			if (account != null) {
				account.setPassWord(new BCryptPasswordEncoder().encode(password));
				accountRepo.save(account);
			}
		}

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			Account account = accountRepo.findByUserName(username);

			if (account != null) {
				// Tạo list quyền
				List<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>();

				// Lấy quyền của user thêm vào list quyền
				for (Role role : account.getRoles()) {
					list.add(new SimpleGrantedAuthority(role.getName()));
				}

				return new org.springframework.security.core.userdetails.User(username, account.getPassWord(), list);
			} else {
				throw new UsernameNotFoundException("not found");
			}
		}		
	}
}
