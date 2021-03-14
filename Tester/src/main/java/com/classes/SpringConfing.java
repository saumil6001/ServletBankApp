package com.classes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfing {

	
	@Bean
	public BankAccount bankAccount() {
		return new BankAccount();
	}
	
	@Bean
	public Customer customer() {
		return new Customer(bankAccount());
	}
	
	
	
	
}
