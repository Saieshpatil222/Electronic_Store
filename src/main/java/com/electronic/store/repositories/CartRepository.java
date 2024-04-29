package com.electronic.store.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.electronic.store.entities.Cart;
import com.electronic.store.entities.User;

@Service
public interface CartRepository extends JpaRepository<Cart,String>{
	Optional<Cart> findByUser(User user);
}
