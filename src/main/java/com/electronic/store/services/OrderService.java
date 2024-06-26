package com.electronic.store.services;

import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface OrderService {
    // create order
    OrderDto createOrder(CreateOrderRequest request);

    // remove order
    void removeOrder(String orderId);

    // getorders of user
    List<OrderDto> getOrdersOfUser(String userId);

    // get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    OrderDto updateOrder(String orderId, CreateOrderRequest orderRequest);
}
