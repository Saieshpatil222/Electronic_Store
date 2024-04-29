package com.electronic.store.services.impl;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.electronic.store.services.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.electronic.store.services.OrderService;
import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.entities.Cart;
import com.electronic.store.entities.CartItem;
import com.electronic.store.entities.Order;
import com.electronic.store.entities.OrderItem;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.BadApiRequestException;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.CartRepository;
import com.electronic.store.repositories.OrderReposiotry;
import com.electronic.store.repositories.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderReposiotry orderRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CartRepository cartReposiotry;

	@Override
	public OrderDto createOrder(CreateOrderRequest orderDto) {
		String userId = orderDto.getUserId();
		String cartId = orderDto.getCartId();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id!!"));
		// fetch cart

		Cart cart = cartReposiotry.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart with given id is not found!!"));

		List<CartItem> cartItems = cart.getItems();

		if (cartItems.size() <= 0) {
			throw new BadApiRequestException("Invalid number of items in cart!!");
		}

		Order order = Order.builder().billingName(orderDto.getBillingName()).billingPhone(orderDto.getBillingPhone())
				.billingAddress(orderDto.getBillingAddress()).orderedDate(new Date(7)).deliveredDate(null)
				.paymentStatus(orderDto.getPaymentStatus()).orderStatus(orderDto.getOrderStatus())
				.orderId(UUID.randomUUID().toString()).user(user).build();

		AtomicReference<Integer> orderAmount = new AtomicReference<>(0);

		List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
			OrderItem orderItem = OrderItem.builder().quantity(cartItem.getQuantity()).product(cartItem.getProduct())
					.totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice()).order(order)
					.build();
			orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
			return orderItem; // Corrected here
		}).collect(Collectors.toList());

		order.setOrderItems(orderItems);
		order.setOrderAmount(orderAmount.get());

		cart.getItems().clear();
		cartReposiotry.save(cart);
		Order savedOrder = orderRepository.save(order);
		return modelMapper.map(savedOrder, OrderDto.class);
	}

	@Override
	public void removeOrder(String orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order is not found!!"));
		orderRepository.delete(order);
	}

	@Override
	public List<OrderDto> getOrdersOfUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id!!"));
		List<Order> orders = orderRepository.findByUser(user);
		List<OrderDto> orderDto = orders.stream().map(order -> modelMapper.map(order, OrderDto.class))
				.collect(Collectors.toList());
		return orderDto;
	}

	@Override
	public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Order> page = orderRepository.findAll(pageable);
		return Helper.getPagableResponse(page,OrderDto.class);
	}

	@Override
	public OrderDto updateOrder(String orderId, CreateOrderRequest orderRequest) {
		String orderStatus = orderRequest.getOrderStatus();
		String paymentStatus = orderRequest.getPaymentStatus();
		Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order with given Id not found"));

		order.setOrderStatus(orderStatus);
		order.setPaymentStatus(paymentStatus);
		order.setDeliveredDate(new Date(System.currentTimeMillis()));

		Order save = orderRepository.save(order);
		return modelMapper.map(save,OrderDto.class);
	}

}
