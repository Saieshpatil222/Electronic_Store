package com.electronic.store.services;

import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.CartDto;

public interface CartService {
	// add items to cart
	// case 1: cart for user is not available we will create a cart and then add the
	// data in a cart
	// case2: cart available add the items in he cart
	
	CartDto addItemToCart(String userId, AddItemToCartRequest request);
	
	//remove item from  cart
	void removeItemFromCrat(String userId, int cartItem);
	
	//remove all items from cart
	void clearCart(String userId);
	
	CartDto getCartByUser(String userId);
}
