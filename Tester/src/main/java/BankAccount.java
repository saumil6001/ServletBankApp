import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {


	private int account_number;
	private String account_hloder;
	private double balance;
	

	private ReentrantLock lock;

	public BankAccount( int account_number, double balance,String accountHolder ) {
		this.account_number = account_number;
		this.balance = balance;
		this.account_hloder=accountHolder;
		this.lock = new ReentrantLock();
	}


	public boolean addDeposit(double deposit) {

		try {
			if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
				balance +=deposit;
				return true;
			}
		} catch (InterruptedException e) {
			System.out.println("No lock Found");
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return false;
	}

	
	
	public boolean withdraw(double withdraw) {
	
		if(balance>withdraw) {
			try {
				lock.tryLock(1000,TimeUnit.MILLISECONDS);
				balance -=withdraw;
				return true;	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
		
		return false;
	}
	
	
	
	
	
	
	
	
	public String getAccount_hloder() {
		return account_hloder;
	}


	public int getAccount_number() {
		return account_number;
	}

	public double getBalance() {
		return balance;
	}

}
