package com.kitchenStory.interfaces;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.kitchenStory.models.Orders;
import com.kitchenStory.models.User;

public interface OrderRepository extends PagingAndSortingRepository<Orders,Integer>{
	Page<Orders> findByUserContaining(User user, Pageable pageable);
}
