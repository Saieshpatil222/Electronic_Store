package com.electronic.store.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electronic.store.entities.Category;
import com.electronic.store.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

	Page<Product> findByTitleContaining(String subTitle, Pageable pageable);

	Page<Product> findByLiveTrue(Pageable pageable);

	Page<Product> findByCategory(Category category, Pageable paeable);

	// Optional<Product> findById(int productId);
}
