package com.electronic.store.services.impl;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.CartDto;
import com.electronic.store.entities.Cart;
import com.electronic.store.entities.CartItem;
import com.electronic.store.entities.Product;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.BadApiRequestException;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.repositories.CartItemRepository;
import com.electronic.store.repositories.CartRepository;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Override
	public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
		int quantity = request.getQuantity();
		String productId = request.getProductId();

		if (quantity <= 0) {
			throw new BadApiRequestException("Request quantity is not valid!!");
		}

		// fetch the product

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product with given id not found!!"));
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id!!"));

		Cart cart = null;
		try {
			cart = cartRepository.findByUser(user).get();
		} catch (NoSuchElementException e) {
			cart = new Cart();
			cart.setCartId(UUID.randomUUID().toString());
			cart.setCreatedAt(new Date(0));
		}

		// perform cart operation

		AtomicReference<Boolean> updated = new AtomicReference<>(false);

		List<CartItem> items = cart.getItems();

		items = items.stream().map(item -> {
			if (item.getProduct().getProductId().equals(productId)) {
				// item already present in cart
				item.setQuantity(quantity);
				item.setTotalPrice(quantity * product.getPrice());
				updated.set(true);
			}
			return item;
		}).collect(Collectors.toList());
		//cart.setItems(updatedItems);

		if (!updated.get()) {
			// create items
			CartItem cartItems = CartItem.builder().quantity(quantity).totalPrice(quantity * product.getPrice())
					.cart(cart).product(product).build();
			cart.getItems().add(cartItems);
		}

		cart.setUser(user);

		Cart updatedCart = cartRepository.save(cart);
		return mapper.map(updatedCart, CartDto.class);
	}

	@Override
	public void removeItemFromCrat(String userId, int cartItem) {

		CartItem cartItem1 = cartItemRepository.findById(cartItem)
				.orElseThrow(() -> new ResourceNotFoundException("cart not found with given id!!"));
		cartItemRepository.delete(cartItem1);
	}

	@Override
	public void clearCart(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id!!"));

		Cart cart = cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("cart with given id is not found !!"));
		cart.getItems().clear();
		cartRepository.save(cart);
	}

	@Override
	public CartDto getCartByUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id!!"));

		Cart cart = cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("cart with given id is not found !!"));
		
		return mapper.map(cart, CartDto.class);
	}

}
