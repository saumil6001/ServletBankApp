import java.sql.Connection;

public class Customer {
	
	private BankAccount bank_account;
	private String username;
	private int user_id;
	private Connection connnection;


	public Customer(String name,int user_id2, Connection connnection, BankAccount bankAccount) {
		this.username=name;
		this.connnection=connnection;
		this.user_id=user_id2;
		this.bank_account=bankAccount;
	}


	public BankAccount getBank_account() {
		return bank_account;
	}


	public Connection getConnnection() {
		return connnection;
	}


	public int getUser_id() {
		return user_id;
	}

		
	public String getUsername() {
		return username;
	}
	
}
