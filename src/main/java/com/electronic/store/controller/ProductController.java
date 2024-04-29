package com.electronic.store.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.electronic.store.dtos.ApiResponseMessage;
import com.electronic.store.dtos.ImageResponse;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.ProductDto;
import com.electronic.store.services.FileService;
import com.electronic.store.services.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/products")
//@CrossOrigin("*")
@SecurityRequirement(name = "scheme1")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private FileService fileService;

	@Value("${product.image.path}")
	private String imagePath;

	@PostMapping()
	public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) {
		ProductDto createProduct = productService.create(productDto);
		return new ResponseEntity<>(createProduct, HttpStatus.CREATED);
	}

	@PutMapping("/{productId}")
	public ResponseEntity<ProductDto> update(@RequestBody ProductDto productDto, @PathVariable String productId) {
		ProductDto updatedProduct = productService.update(productDto, productId);
		return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId) {
		productService.delete(productId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message("Product is deleted successfully")
				.status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getUserById(@PathVariable String productId) {
		ProductDto productDto = productService.getById(productId);
		return new ResponseEntity<>(productDto, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}

	// get all live products
	@GetMapping("/live")
	public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PageableResponse<ProductDto> pageableResponse = productService.getAllLive(pageNumber, pageSize, sortBy,
				sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}

	@GetMapping("/search/{query}")
	public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
			@PathVariable String query) {
		PageableResponse<ProductDto> pageableResponse = productService.searchAllByTitle(query, pageNumber, pageSize,
				sortBy, sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}

	@PostMapping("/image/{productId}")
	public ResponseEntity<ImageResponse> uploadProductImage(@PathVariable String productId,
			@RequestParam("productImage") MultipartFile image) throws IOException {
		String fileName = fileService.uploadFile(image, imagePath);
		ProductDto productDto = productService.getById(productId);
		productDto.setProductImageName(fileName);
		ProductDto updatedProduct = productService.update(productDto, productId);

		ImageResponse imageResponse = ImageResponse.builder().imageName(updatedProduct.getProductImageName())
				.message("Product Image is successfully uploaded!!").status(HttpStatus.CREATED).success(true).build();

		return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
	}

	@GetMapping("/image/{productId}")
	public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
		ProductDto productDto = productService.getById(productId);
		/// Logger.info("User image name: {}", category.getCoverImage());
		InputStream resource = fileService.getResourcse(imagePath, productDto.getProductImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());

	}
}
