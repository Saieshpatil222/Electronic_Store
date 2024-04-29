package com.electronic.store.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electronic.store.entities.Order;
import com.electronic.store.entities.User;

public interface OrderReposiotry extends JpaRepository<Order,String>{
	List<Order> findByUser(User user);
}
