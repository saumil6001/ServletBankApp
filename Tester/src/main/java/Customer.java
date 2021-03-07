

public class Customer {

	private BankAccount bank_account;
	private String username;
	private String name;
	private String emailId;
	private String phone_number;

	public Customer(BankAccount bank_account, String username, String name, String emailId, String phone_number) {
		this.bank_account = bank_account;
		this.username = username;
		this.name = name;
		this.emailId = emailId;
		this.phone_number = phone_number;
	}

	
	public BankAccount getBank_account() {
		return bank_account;
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

	public String getPhone_number() {
		return phone_number;
	}

	
	
}
