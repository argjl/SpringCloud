package com.springcloud.currency.repository;

import org.springframework.stereotype.Repository;

import com.springcloud.currency.model.ExchangeValue;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ExchangeValueRepository extends JpaRepository<ExchangeValue, Long> {
	
	ExchangeValue findByFromAndTo(String from, String to);

}
