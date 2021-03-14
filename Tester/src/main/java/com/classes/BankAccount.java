package com.classes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.database.DataScource;

public class BankAccount {

	private int accountNumber;
	private double balance;
	private String sql_balanceUpdate = "UPDATE accounts SET balance = ? where account_id = ? ;";
	private String sql_inqiry = "select account_id,balance from accounts where account_id= ? ;";
	private ReentrantLock lock = new ReentrantLock();
	private Connection connection = null;
	private PreparedStatement statement = null;

	
	
	
	public BankAccount() {
		
	}

	public boolean addDeposit(double deposit) {

		ResultSet rs1=null;

		try {

			connection = DataScource.getConnection();
			
			if (lock.tryLock(1000, TimeUnit.MILLISECONDS) ) {
				
				
				statement = connection.prepareStatement(sql_inqiry);
				statement.setInt(1, accountNumber);
				rs1 = statement.executeQuery();

				while (rs1.next()) {
					accountNumber = rs1.getInt(1);
					balance = rs1.getDouble(2);
				}
				
				balance += deposit;
				statement = connection.prepareStatement(sql_balanceUpdate);
				statement.setDouble(1, balance);
				statement.setInt(2, accountNumber);
				int rs = statement.executeUpdate();

				if (rs == 1) {					
					return true;
				}

			} else {

				System.out.println("Problem with Lock");

			}

		} catch (InterruptedException | SQLException e) {
			
			e.printStackTrace();
		} finally {


			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			if (statement != null) {

				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

				
			}
			

			lock.unlock();
			
		}

		return false;
	}

	public boolean withdraw(double withdraw) {

		ResultSet rs1=null;
		
		if (balance > withdraw) {
			try {

				connection = DataScource.getConnection();

				if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
					
					statement = connection.prepareStatement(sql_inqiry);
					statement.setInt(1, accountNumber);
					rs1 = statement.executeQuery();


					while (rs1.next()) {
						accountNumber = rs1.getInt(1);
						balance = rs1.getDouble(2);
					}

					
					
					
					
					balance -= withdraw;
					statement = connection.prepareStatement(sql_balanceUpdate);
					statement.setDouble(1, balance);
					statement.setInt(2, accountNumber);
					int rs = statement.executeUpdate();

					if (rs == 1) {
						return true;
					}

				} else {

					System.out.println("Problem with Lock");

				}

			} catch (InterruptedException | SQLException e) {
				e.printStackTrace();
			} finally {

				if (rs1 != null) {
					try {
						rs1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}

				
				
				if (statement != null) {

					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

					lock.unlock();
				}
			}
		}
		return false;
	}

	public boolean transferMoney(int accountNumber, double amount) {

		if (withdraw(amount)) {

			ResultSet rs = null;

			try {
				statement = connection.prepareStatement(sql_inqiry);
				statement.setInt(1, accountNumber);
				rs = statement.executeQuery();

				while (rs.next()) {
					int deposit_accountNumber = rs.getInt(1);
					double deposit_balance = rs.getDouble(2);
					BankAccount transfer_bankAccount = new BankAccount();
					transfer_bankAccount.setAccountNumber(deposit_accountNumber);
					transfer_bankAccount.setBalance(deposit_balance);
					if (transfer_bankAccount.addDeposit(amount)) {
						return true;
					} else {
						this.addDeposit(amount);
					}

				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}

				if (statement != null) {

					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}

			}
		}
		return false;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public double getBalance() {
		return balance;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	
	
	
}
