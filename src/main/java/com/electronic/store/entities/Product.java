package com.electronic.store.entities;

//import java.sql.Date;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
	@Id
	private String productId;

	private String title;

	@Column(length = 1000)
	private String description;

	private int price;

	private int discountedPrice;

	private int quantity;

	@Column(name = "added_date", columnDefinition = "DATETIME")
	private Date addedDate;

	private boolean live;

	private boolean stock;

	private String productImageName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private Category category;
}
