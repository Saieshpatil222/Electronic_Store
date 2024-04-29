package com.electronic.store.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {

	@NotBlank(message="Cart id is required")
	private String cartId;

	@NotBlank(message="userId is required")
	private String userId;

	private String orderStatus = "PENDING";

	private String paymentStatus = "NOTPAID";

	@NotBlank(message="Address is required")
	private String billingAddress;

	@NotBlank(message="Phone no is required")
	private String billingPhone;

	@NotBlank(message="Name is required")
	private String billingName;

}
