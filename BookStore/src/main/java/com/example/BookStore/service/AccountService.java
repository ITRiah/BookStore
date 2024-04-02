package com.example.BookStore.service;

import java.util.ArrayList;
import java.util.List;

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
import com.example.BookStore.entity.Role;
import com.example.BookStore.repo.AccountRepo;

import jakarta.persistence.NoResultException;

public interface AccountService {
	public void create(AccountDTO accountDTO);

	public void update(AccountDTO accountDTO);

	public void delete(int id);

	public Page<AccountDTO> getAll(SearchDTO searchDTO);
	
	public Page<AccountDTO> search(SearchDTO searchDTO);

	@Service
	public class AccountServiceImpl implements AccountService, UserDetailsService {

		@Autowired
		AccountRepo accountRepo;

		@Override
		public void create(AccountDTO accountDTO) {
			Account account = new ModelMapper().map(accountDTO, Account.class);
			account.setPassWord(new BCryptPasswordEncoder().encode(account.getPassWord())); // nên convert khi lưu db
			accountRepo.save(account);
		}
		
		@Override
		public void update(AccountDTO accountDTO) {
			Account accountCurrent = accountRepo.findById(accountDTO.getId()).orElseThrow(NoResultException::new);
			accountCurrent = new ModelMapper().map(accountDTO, Account.class);
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
			String username = searchDTO.getKeyword() != null  ? "" : searchDTO.getKeyword();
			
			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<Account> page = accountRepo.searchByUsername("%" + username + "%", pageRequest);
			
			Page<AccountDTO> page2 = page.map(account -> new ModelMapper().map(account, AccountDTO.class));
			
			return page2;
		}

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			Account account = accountRepo.findByUserName(username);

			System.out.println(account.getPassWord());

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
