package com.electronic.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.ApiResponseMessage;
import com.electronic.store.dtos.CartDto;
import com.electronic.store.services.CartService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/carts")
//@CrossOrigin("*")
@Tag(name = "CartController", description = "This is cart api for cart operation!!")
@SecurityRequirement(name = "scheme1")
public class CartController {

	@Autowired
	private CartService cartService;

	@PostMapping("/{userId}")
	public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request,
			@PathVariable String userId) {
		CartDto cart = cartService.addItemToCart(userId, request);
		return new ResponseEntity<>(cart, HttpStatus.OK);
	}

	@DeleteMapping("/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId,
			@PathVariable int itemId) {
		cartService.removeItemFromCrat(userId, itemId);
		ApiResponseMessage response = ApiResponseMessage.builder().message("Item is removed!!").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId) {
		cartService.clearCart(userId);
		ApiResponseMessage response = ApiResponseMessage.builder().message("Item is removed!!").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
		CartDto cart = cartService.getCartByUser(userId);
		return new ResponseEntity<>(cart, HttpStatus.OK);
	}
}
