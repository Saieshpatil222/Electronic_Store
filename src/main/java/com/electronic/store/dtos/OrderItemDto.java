package com.electronic.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
	private int id;

	private int quantity;

	private int totalPrice;

	private ProductDto product;

	private OrderDto order;
}
