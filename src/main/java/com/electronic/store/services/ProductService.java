package com.electronic.store.services;

import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.ProductDto;

public interface ProductService {
	ProductDto create(ProductDto productDto);

	ProductDto update(ProductDto productDto, String productId);

	void delete(String productId);

	ProductDto getById(String productId);

	PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

	PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);

	PageableResponse<ProductDto> searchAllByTitle(String subTitle, int pageNumber, int pageSize, String sortBy,
			String sortDir);

	ProductDto createWithCategory(ProductDto productDto, String categoryId);

	ProductDto updateCategory(String productId, String categoryId);

	PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy,
			String sortDir);
}
