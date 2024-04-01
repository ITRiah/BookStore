package com.example.BookStore.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.dto.UserDTO;
import com.example.BookStore.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/member/user")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody UserDTO userDTO) {
		userService.create(userDTO);
		return ResponseDTO.<Void>builder().status(200).msg("ok").build();
	}

	@GetMapping("/")
	public ResponseDTO<Page<UserDTO>> getAll(@ModelAttribute SearchDTO searchDTO) {
		return ResponseDTO.<Page<UserDTO>>builder().status(200).msg("ok").data(userService.getAll(searchDTO)).build();
	}

	@PostMapping("/upload-avatar")
	public ResponseDTO<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("id") int id)
			throws IllegalStateException, IOException {

		String imageDirectory = "E:\\Studying\\Java Backend\\Springboot\\BookStore\\images\\user\\";

		// Tạo thư mục nếu chưa có
		File directory = new File(imageDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		String fileName = file.getOriginalFilename();
		String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

		// Save the file to the images directory
		Path filePath = Path.of(imageDirectory + uniqueFileName);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		
		//Update avatar
		userService.updateAvatar(uniqueFileName, id);

		return ResponseDTO.<String>builder().data(uniqueFileName).status(200).msg("ok").build();

	}

	@GetMapping("/download")
	public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
		File file = new File("E:\\Studying\\Java Backend\\Springboot\\BookStore\\images\\user\\" + fileName);
		Files.copy(file.toPath(), response.getOutputStream());// lấy dữ liệu từ file để tải về hình ảnh cho web
	}

}
