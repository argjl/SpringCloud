package com.ekart.springekartapplication.Controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ekart.springekartapplication.Entity.Product;
import com.ekart.springekartapplication.Entity.Seller;
import com.ekart.springekartapplication.Repository.SellerRepository;
import com.ekart.springekartapplication.Entity.Order;
import com.ekart.springekartapplication.Service.ProductService;
import com.ekart.springekartapplication.Service.SellerDashboardService;

@RestController
@RequestMapping("/seller/dashboard")
public class SellerDashboardController {

	Logger logger = LoggerFactory.getLogger(SellerDashboardController.class);

	@Autowired
	private SellerDashboardService sellerDashboardService;

	@Autowired
	private ProductService productService;
	
	
	@Autowired
	private SellerRepository sellerRepository;

	/**
	 * Get all products added by the seller
	 *
	 * @param authentication the seller's authentication object
	 * @return the list of products added by the seller
	 */
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getSellerProducts() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		logger.info("Entered the /Products API");
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			Seller seller = sellerDashboardService.findSellerByUsername(username); // Implement a method to find seller
																					// by username
			List<Product> products = productService.getProductsBySellerId(seller.getId());
			logger.info("Product Details: {}", products);
			return ResponseEntity.ok(products);

		}
		logger.info("Unauthorized access attempt.");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	/**
	 * Add or update a product for the seller
	 *
	 * @param product        the product to add or update
	 * @param authentication the seller's authentication object
	 * @return the added or updated product
	 */
	@PostMapping("/addOrUpdateProduct")
	public ResponseEntity<Product> addOrUpdateProduct(@RequestBody Product product, Authentication authentication) {
		Seller seller = (Seller) authentication.getPrincipal(); // Assuming seller is logged in
		product.setSeller(seller); // Set the seller as the owner of the product
		Product savedProduct = sellerDashboardService.addOrUpdateProduct(product);
		return ResponseEntity.ok(savedProduct);
	}

	/**
	 * Get all orders associated with the seller's products
	 *
	 * @param authentication the seller's authentication object
	 * @return the list of orders for the seller
	 */
	@GetMapping("/orders")
	public ResponseEntity<List<Order>> getSellerOrders() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
	    // Check if the user is authenticated and of the right type
	    if (authentication != null && authentication.isAuthenticated()) {
	        Object principal = authentication.getPrincipal();
	        
	        if (principal instanceof UserDetails) {
	            UserDetails userDetails = (UserDetails) principal;

	            // Assuming you have a way to fetch a Seller by username or other attributes
	            Optional<Seller> sellerOptional = sellerRepository.findByUsername(userDetails.getUsername());
	            Seller seller = sellerOptional.orElseThrow(() -> new UsernameNotFoundException("Seller not found"));

	            // Now you can use the Seller object
	            List<Order> orders=sellerDashboardService.getSellerOrders(seller.getId());
	            return ResponseEntity.ok(orders);
	        }
	    }
	    
	    throw new AccessDeniedException("User not authenticated or not a seller.");
	}

	/**
	 * Delete a product based on seller ID and product ID
	 *
	 * @param productId      the ID of the product to delete
	 * @param authentication the seller's authentication object
	 * @return a response indicating the result of the delete operation
	 */
	@DeleteMapping("/deleteProduct/{productId}")
	public ResponseEntity<String> deleteProductBySeller(@PathVariable Long productId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		logger.info("Entered the /Products API");
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			Seller seller = sellerDashboardService.findSellerByUsername(username); // Assuming seller is logged in
			sellerDashboardService.deleteProductBySeller(productId, seller.getId());
			return ResponseEntity.ok("Product deleted successfully");
	}
		logger.info("Unauthorized access attempt.");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
}
	}
