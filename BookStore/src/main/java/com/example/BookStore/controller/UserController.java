package com.example.BookStore.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.UserDTO;
import com.example.BookStore.service.UserService;

@RestController
@RequestMapping("/member/user")
public class UserController {
	@Autowired
	UserService userService;
	
	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody UserDTO userDTO) {
		userService.create(userDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@PostMapping("/get-by-id/{id}")
	public ResponseDTO<UserDTO> getById(@PathVariable int id ) {
		
		return ResponseDTO.<UserDTO>builder()
				.status(200)
				.msg("ok")
				.data(userService.getById(id))
				.build();
	}
	
	
		
	@PutMapping("/")
	public ResponseDTO<Void> update(@ModelAttribute UserDTO userDTO) throws IOException {
		
		MultipartFile file = userDTO.getFile();
		String imageDirectory = "C:\\Users\\Admin\\Desktop\\BookStore\\BookStore\\images\\user\\";

		if(file != null) {
			String fileName = file.getOriginalFilename();
			String uniqueFileName =  UUID.randomUUID().toString() + "_" + fileName;
			Path filePath = Path.of(imageDirectory + uniqueFileName);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			userDTO.setAvatar(uniqueFileName);
		}
		
		userService.create(userDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("ok")
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable int id) {
		userService.delete(id);
		
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("ok")
				.build();
	}
	
	@PostMapping("/upload-avatar")
	public ResponseDTO<Void>  uploadAvatar(
				@RequestParam("file") MultipartFile file,
				@RequestParam("id") int id) throws IOException {
		
		String imageDirectory = "C:\\Users\\Admin\\Desktop\\BookStore\\BookStore\\images\\user\\";
		String uniqueFileName = "";
		
		if(file != null) {
			String fileName = file.getOriginalFilename();
			uniqueFileName =  UUID.randomUUID().toString() + "_" + fileName;
			Path filePath = Path.of(imageDirectory + uniqueFileName);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		}
		
		userService.updateAvatar(uniqueFileName, id);
		
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("ok")
				.build();
	}
}
