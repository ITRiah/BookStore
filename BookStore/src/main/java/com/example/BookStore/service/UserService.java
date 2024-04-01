package com.example.BookStore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.dto.UserDTO;
import com.example.BookStore.entity.User;
import com.example.BookStore.repo.UserRepo;

public interface UserService {
	public void create(UserDTO userDTO);
	public Page<UserDTO> getAll(SearchDTO searchDTO);
	public void updateAvatar(String fileName, int id);
	
	@Service
	public class UserServiceImpl implements UserService{
		
		@Autowired
		UserRepo userRepo;

		@Override
		public void create(UserDTO userDTO) {
			User user = new ModelMapper().map(userDTO, User.class);
			userRepo.save(user);
		}

		@Override
		public Page<UserDTO> getAll(SearchDTO searchDTO) {
			
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage()  ;
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			
			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<User> page = userRepo.findAll(pageRequest);
			
			Page<UserDTO> page2 =  page.map(user -> new ModelMapper().map(user, UserDTO.class));
			
			return page2;
		}

		@Override
		public void updateAvatar(String fileName, int id) {
			User user = userRepo.getById(id);
			user.setAvatar(fileName);
			userRepo.save(user);
		}
		
	}
} 
