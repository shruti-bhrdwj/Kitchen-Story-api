package com.kitchenStory.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.kitchenStory.models.Cart;
import com.kitchenStory.models.User;


public interface CartRepository extends PagingAndSortingRepository<Cart,Integer>{
	Page<Cart> findByUserContaining(User user, Pageable pageable);
}
