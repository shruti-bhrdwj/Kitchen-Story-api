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

import com.kitchenStory.models.Product;
import com.kitchenStory.repositories.ProductService;
import com.kitchenStory.repositories.UserService;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	UserService userService ;
	
	@GetMapping("/getproducts")
	public ResponseEntity<?> getProducts(@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,@RequestParam(required = false) String name,@RequestParam(required = false) String category){
	
		try {
			List<Product> list = productService.getEntityProducts(pageNo, pageSize, sortBy, name, category);
			return new ResponseEntity<List<Product>>(list, new HttpHeaders(), HttpStatus.OK); 
		}
		catch(Exception ex) {
			return new ResponseEntity<String>("Unable to fetch products", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@GetMapping("/getproduct/{productId}")
	public ResponseEntity<?> getProduct(@PathVariable("productId") int id, @RequestParam(required = false) String userName) {
		try {
			Optional<Product> product = this.productService.getEntityProduct(id);
			if(!(product.isPresent() )) {
				return new ResponseEntity<String>("Products does not exist with id " + id, new HttpHeaders(), HttpStatus.NOT_FOUND); 
			}
			else {
				return new ResponseEntity<Optional<Product>>(product,new HttpHeaders(), HttpStatus.OK);
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage().toString());
			return new ResponseEntity<String>("Unable to fetch products",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping("/addproduct")
	public ResponseEntity<?> addProduct(@RequestBody(required = true) Product addProduct, @RequestParam(required = true) String userName){
		if(addProduct == null) {
			return new ResponseEntity<String>("Add Product request body cannot be empty", new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		try {
			boolean check = userService.checkAdmin(userName);
			if(check == true) {
				this.productService.addEntityProduct(addProduct);
				return new ResponseEntity<Product>(addProduct, new HttpHeaders(), HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<String>("Unauthorized Request",new HttpHeaders(), HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Unable to add products",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/updateproduct/{productId}")
	public ResponseEntity<?> updateProduct(@PathVariable("productId") int id, @RequestBody(required = false) Product updateProduct,@RequestParam(required = true) String userName) {
		if(updateProduct == null) {
			return new ResponseEntity<String>("Update Product request body cannot be empty",new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		if(id != updateProduct.getId()) {
			return new ResponseEntity<String>("Id in request path and request body do not match",new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		try {
			boolean check = userService.checkAdmin(userName);
			if(check == true) {
				Optional<Product> getProduct = this.productService.getEntityProduct(id);
				if(!(getProduct.isPresent())) {
					return new ResponseEntity<String>("Product does not exist with id " + id,new HttpHeaders(), HttpStatus.NOT_FOUND);
				}
				this.productService.updateEntityProduct(updateProduct);
				return new ResponseEntity<Product>(new HttpHeaders(), HttpStatus.NO_CONTENT);
			}
			else {
				return new ResponseEntity<String>("Unauthorized Request",new HttpHeaders(), HttpStatus.UNAUTHORIZED);
			}
		}
		catch(Exception ex) {
			return new ResponseEntity<String>("Unable to update products",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/deleteproduct/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable("productId") int id,@RequestParam(required = false) String userName){
		try {
			boolean check = userService.checkAdmin(userName);
			if(check == true) {
				Optional<Product> getProduct= this.productService.getEntityProduct(id);
				if(!(getProduct.isPresent())) {
					return new ResponseEntity<String>("Product does not exist with id " + id,new HttpHeaders(), HttpStatus.NOT_FOUND);
				}
				else {
					this.productService.deleteEntityProduct(id);
					return new ResponseEntity<Product>(new HttpHeaders(), HttpStatus.NO_CONTENT);
				}
			}
			else {
				return new ResponseEntity<String>("Unauthorized Request",new HttpHeaders(), HttpStatus.UNAUTHORIZED);
			}
		}
		catch(Exception ex) {
			return new ResponseEntity<String>("Unable to delete products",new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
