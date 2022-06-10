package com.kitchenStory.interfaces;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kitchenStory.models.Product;

public interface ProductRepository extends JpaRepository<Product,Integer>{
	Page<Product> findByNameContaining(String name, Pageable pageable);
	Page<Product> findByCategoryContaining(String category, Pageable pageable);
}
