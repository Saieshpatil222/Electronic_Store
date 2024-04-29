package com.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electronic.store.entities.OrderItem;
@Repository
public interface OrderItemReposiotry extends JpaRepository<OrderItem, Integer>{
	
}
