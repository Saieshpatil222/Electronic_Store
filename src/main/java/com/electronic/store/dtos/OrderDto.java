package com.electronic.store.dtos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

	private String orderId;

	private String orderStatus="PENDING";

	private String paymentStatus="NOTPAID";

	private int orderAmount;

	private String billingAddress;

	private String billingPhone;

	private String billingName;

	private Date orderedDate = new Date(7);

	private Date deliveredDate;

//	private UserDto user;

	private List<OrderItemDto> orderItems = new ArrayList<>();
}
