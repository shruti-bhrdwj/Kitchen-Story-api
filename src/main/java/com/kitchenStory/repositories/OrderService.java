package com.kitchenStory.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kitchenStory.interfaces.OrderRepository;
import com.kitchenStory.interfaces.UserRepository;
import com.kitchenStory.models.Orders;
import com.kitchenStory.models.Product;
import com.kitchenStory.models.User;

@Service
public class OrderService {
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	public List<Orders> getEntityOrders(Integer pageNo, Integer pageSize, String sortBy, String username) throws Exception{
		Page<Orders> pagedResult;
		Pageable paging = PageRequest.of(pageNo, pageSize,Sort.by(sortBy));
		User searchUser = null;
		try {
			pagedResult =  orderRepository.findAll(paging);
			if(username != null) {
				
				Iterable<User> user = userRepository.findAll();
				for(User u : user) {
					if(u.getUsername().equals(username)) {
						searchUser = u;
						break;
					}
				}
				pagedResult = orderRepository.findByUserContaining(searchUser, paging);
			}
			if(pagedResult.hasContent()) {
	            return pagedResult.getContent();
	        } else {
	            return new ArrayList<Orders>();
	        }
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			throw new Exception("Unable to retrieve orders "+ex.getMessage().toString());
		}
	}
	public Optional<Orders> getEntityOrder(int orderId) throws Exception{
		try {
			return this.orderRepository.findById(orderId);
		}
		catch(Exception ex){
			throw new Exception("Unable to retrieve order with id"+orderId+" "+ex.getMessage().toString());
		}
	}
	public void addEntityOrder(Orders addOrder) throws Exception {
		try {
			System.out.println(addOrder.getUser().getUsername());
			double total = 0.0;
			for( Product prod : addOrder.getProducts()) {
				System.out.print(prod.getPrice());
				total = total+ prod.getPrice();
			}
			System.out.println("total balance");
			System.out.println(total);
			addOrder.setTotalPrice(total);
			if(total <= addOrder.getUser().getWalletBalance()) {
				double balance = addOrder.getUser().getWalletBalance() - total;
				addOrder.getUser().setWalletBalance(balance);
				this.orderRepository.save(addOrder);
			}
			else {
				throw new Exception("Insufficient Balance");
			}	
		
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage().toString());
			throw new Exception("Unable to add order "+ex.getMessage().toString());
			
		}
	}
	
	public void updateEntityOrder(Orders updateOrder) throws Exception {
		try {
			this.orderRepository.save(updateOrder);
		}
		catch(Exception ex) {
			throw new Exception("Unable to update order "+ex.getMessage().toString());
		}
	}
	
	public void deleteEntityOrder(int orderId) throws Exception {
		try {
			this.orderRepository.deleteById(orderId);
		}
		catch(Exception ex) {
			throw new Exception("Unable to delete order "+ex.getMessage().toString());
		}
	}
}
