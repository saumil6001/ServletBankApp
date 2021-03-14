package com.classes;

public class Customer {

	private BankAccount bankAccount;
	private String username;
	private String name;
	private String emailId;
	private String phoneNumber;

	public Customer(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	
	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

}
