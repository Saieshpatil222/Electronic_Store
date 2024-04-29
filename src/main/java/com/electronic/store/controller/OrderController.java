package com.electronic.store.controller;

import java.util.List;

import com.electronic.store.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.electronic.store.services.OrderService;
import com.electronic.store.dtos.ApiResponseMessage;
import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
//@CrossOrigin("*")
@SecurityRequirement(name = "scheme1")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request) {
		OrderDto order = orderService.createOrder(request);
		return new ResponseEntity<>(order, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {
		orderService.removeOrder(orderId);
		ApiResponseMessage respone = ApiResponseMessage.builder().status(HttpStatus.OK).message("order is removed!!")
				.success(true).build();
		return new ResponseEntity<>(respone, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<OrderDto>> getOrderOfUser(@PathVariable String userId) {
		List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
		return new ResponseEntity<>(ordersOfUser, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<PageableResponse<OrderDto>> getAllOrderOfUser(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
		PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@PutMapping("update/{orderId}")
	public ResponseEntity<OrderDto> updateOrder(@PathVariable String orderId, @RequestBody CreateOrderRequest orderRequest){
		OrderDto orderDto = orderService.updateOrder(orderId,orderRequest);
		return new ResponseEntity<>(orderDto,HttpStatus.OK);
	}
}
