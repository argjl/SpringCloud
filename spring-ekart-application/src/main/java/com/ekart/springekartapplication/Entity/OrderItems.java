package com.ekart.springekartapplication.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "order_items")
@Data
@RequiredArgsConstructor
public class OrderItems {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.REMOVE) // Enable cascade delete
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Product product;

	private int quantity;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

}
