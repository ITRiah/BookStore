package com.example.BookStore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.ProductDetailDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.ProductDetail;
import com.example.BookStore.repo.ProductDetailRepo;

import jakarta.persistence.NoResultException;

public interface ProductDetailService {
	public void create(ProductDetailDTO ProductDetailsDTO);

	public void update(ProductDetailDTO ProductDetailsDTO);

	public Page<ProductDetailDTO> getAll(SearchDTO searchDTO);

	public List<ProductDetailDTO> findByProductId(int productId);

	public ProductDetailDTO findByProductIdAndColor(int productId, String color);

	@Service
	public class ProductDetailsServiceImpl implements ProductDetailService {

		@Autowired
		ProductDetailRepo productDetailsRepo;

		@Override
		public void create(ProductDetailDTO ProductDetailsDTO) {
			ProductDetail ProductDetails = new ModelMapper().map(ProductDetailsDTO, ProductDetail.class);
			productDetailsRepo.save(ProductDetails);
		}

		@Override
		public void update(ProductDetailDTO ProductDetailsDTO) {
			ProductDetail productDetailsCurrent = productDetailsRepo.findById(ProductDetailsDTO.getId())
																			.orElseThrow(NoResultException::new);
			productDetailsCurrent = new ModelMapper().map(ProductDetailsDTO, ProductDetail.class);
			productDetailsRepo.save(productDetailsCurrent);
		}

		@Override
		public Page<ProductDetailDTO> getAll(SearchDTO searchDTO) {

			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage();
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();

			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();

			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<ProductDetail> page = productDetailsRepo.findAll(pageRequest);

			Page<ProductDetailDTO> page2 = page
					.map(ProductDetails -> new ModelMapper().map(ProductDetails, ProductDetailDTO.class));

			return page2;
		}

		@Override
		public List<ProductDetailDTO> findByProductId(int productId) {
			List<ProductDetail> details = productDetailsRepo.getByProductId(productId);

			List<ProductDetailDTO> detailsDTOs = details.stream()
					.map(productDetails -> new ModelMapper().map(productDetails, ProductDetailDTO.class))
					.collect(Collectors.toList());

			return detailsDTOs;
		}

		@Override
		public ProductDetailDTO findByProductIdAndColor(int productId, String color) {
			ProductDetail productDetails = productDetailsRepo.getByProductIdColor(productId, color);

			ProductDetailDTO productDetailsDTO = new ModelMapper().map(productDetails, ProductDetailDTO.class);

			return productDetailsDTO;
		}
	}
}
