package com.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electronic.store.entities.Category;

public interface CategoryRepository extends JpaRepository<Category,String>{

}
