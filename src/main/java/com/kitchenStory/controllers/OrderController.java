package com.kitchenStory.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kitchenStory.models.Orders;
import com.kitchenStory.repositories.OrderService;
import com.kitchenStory.repositories.UserService;


@RestController
@RequestMapping("api/v1/order")
public class OrderController {

	@Autowired
	OrderService orderService;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/getorders")
	public ResponseEntity<?> getOrders(@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,@RequestParam(required = false) String username){
		try {
			List<Orders> list = orderService.getEntityOrders(pageNo, pageSize, sortBy,username);
			return new ResponseEntity<List<Orders>>(list, new HttpHeaders(), HttpStatus.OK); 
		}
		catch(Exception ex) {
			return new ResponseEntity<String>("Unable to fetch orders", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@GetMapping("/getorder/{orderId}")
	public ResponseEntity<?> getOrder(@PathVariable("orderId") int id) {
		try {
			Optional<Orders> order = this.orderService.getEntityOrder(id);
			if(!(order.isPresent())) {
				return new ResponseEntity<String>("Orders does not exist with id " + id, new HttpHeaders(), HttpStatus.NOT_FOUND); 
			}
			else {
				return new ResponseEntity<Optional<Orders>>(order,new HttpHeaders(), HttpStatus.OK);
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage().toString());
			return new ResponseEntity<String>("Unable to fetch orders",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/addorder")
	public ResponseEntity<?> addOrder(@RequestBody(required = true) Orders addOrder){
		if(addOrder == null) {
			return new ResponseEntity<String>("Add Order request body cannot be empty", new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		try {
			this.orderService.addEntityOrder(addOrder);
			return new ResponseEntity<Orders>(addOrder, new HttpHeaders(), HttpStatus.CREATED);
		} 
		catch (Exception e) {
			return new ResponseEntity<String>("Unable to add orders",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/updateorder/{orderId}")
	public ResponseEntity<?> updateOrder(@PathVariable("orderId") int id, @RequestBody(required = true) Orders updateOrder,@RequestParam(required = false) String userName) {
		if(updateOrder == null) {
			return new ResponseEntity<String>("Update Order request body cannot be empty",new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		if(id != updateOrder.getId()) {
			return new ResponseEntity<String>("Id in request path and request body do not match",new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		try {
			boolean check = userService.checkAdmin(userName);
			if(check == true) {
				Optional<Orders> getOrder = this.orderService.getEntityOrder(id);
				if(!(getOrder.isPresent())) {
					return new ResponseEntity<String>("Order does not exist with id " + id,new HttpHeaders(), HttpStatus.NOT_FOUND);
				}
				this.orderService.updateEntityOrder(updateOrder);
				return new ResponseEntity<Orders>(new HttpHeaders(), HttpStatus.NO_CONTENT);
			}
			else {
				return new ResponseEntity<String>("Unauthorized Request",new HttpHeaders(), HttpStatus.UNAUTHORIZED);
			}
		}
		catch(Exception ex) {
			return new ResponseEntity<String>("Unable to update orders",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/deleteorder/{orderId}")
	public ResponseEntity<?> deleteOrder(@PathVariable("orderId") int id,@RequestParam(required = true) String userName){
		try {
			boolean check = userService.checkAdmin(userName);
			if(check == true) {
				Optional<Orders> getOrder = this.orderService.getEntityOrder(id);
				if(!(getOrder.isPresent())) {
					return new ResponseEntity<String>("Order does not exist with id " + id,new HttpHeaders(), HttpStatus.NOT_FOUND);
				}
				else {
					this.orderService.deleteEntityOrder(id);
					return new ResponseEntity<Orders>(new HttpHeaders(), HttpStatus.NO_CONTENT);
				}
			}
			else {
				return new ResponseEntity<String>("Unauthorized Request",new HttpHeaders(), HttpStatus.UNAUTHORIZED);
			}
		}
		catch(Exception ex) {
			return new ResponseEntity<String>("Unable to delete orders",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}