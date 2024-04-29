package com.electronic.store.dtos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.electronic.store.entities.CartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

	private String cartId;

	private Date createdAt;

	private UserDto user;

	private List<CartItem> items = new ArrayList<>();
}
