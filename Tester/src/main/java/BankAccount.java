import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.database.DataScource;

public class BankAccount {

	private int account_number;
	private double balance;
	private String sql_balanceUpdate = "UPDATE accounts SET balance = ? where account_id = ? ;";
	private String sql_inqiry = "select account_id,balance from accounts where account_id= ? ;";
	private ReentrantLock lock;
	private Connection connection = null;
	private PreparedStatement statement = null;

	public BankAccount(int account_number, double balance) {
		this.account_number = account_number;
		this.balance = balance;
		this.lock = new ReentrantLock();
	}

	public boolean addDeposit(double deposit) {
		try {

			connection = DataScource.getConnection();

			if (lock.tryLock(1000, TimeUnit.MILLISECONDS) && connection != null) {
				balance += deposit;
				statement = connection.prepareStatement(sql_balanceUpdate);
				statement.setDouble(1, balance);
				statement.setInt(2, account_number);
				int rs = statement.executeUpdate();

				if (rs == 1) {
					return true;
				}

			} else {

				System.out.println("Problem with Lock");

			}

		} catch (InterruptedException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (statement != null) {

				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

				lock.unlock();
			}
		}

		return false;
	}

	public boolean withdraw(double withdraw) {

		if (balance > withdraw) {
			try {

				connection = DataScource.getConnection();

				if (lock.tryLock(1000, TimeUnit.MILLISECONDS) && connection != null) {
					balance -= withdraw;
					statement = connection.prepareStatement(sql_balanceUpdate);
					statement.setDouble(1, balance);
					statement.setInt(2, account_number);
					int rs = statement.executeUpdate();

					if (rs == 1) {
						return true;
					}

				} else {

					System.out.println("Problem with Lock");

				}

			} catch (InterruptedException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

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

	public boolean transfer_money(int accountNumber, double amount) {

		if (withdraw(amount)) {

			ResultSet rs = null;

			try {
				statement = connection.prepareStatement(sql_inqiry);
				statement.setInt(1, accountNumber);
				rs = statement.executeQuery();

				while (rs.next()) {
					int deposit_accountNumber = rs.getInt(1);
					double deposit_balance = rs.getDouble(2);
					BankAccount bankAccount = new BankAccount(deposit_accountNumber, deposit_balance);

					if (bankAccount.addDeposit(amount)) {
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

	public int getAccount_number() {
		return account_number;
	}

	public double getBalance() {
		return balance;
	}

}
