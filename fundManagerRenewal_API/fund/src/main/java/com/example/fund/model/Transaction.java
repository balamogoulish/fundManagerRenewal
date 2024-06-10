package com.example.fund.model;

public class Transaction {
	private long transaction_index;
	private long user_index_t;
	private long deposit;
	private long withdrawal;
	private long total_amount;
	private String transaction_time;
	
	public Transaction(long transaction_index, long user_index_t, long deposit, long withdrawal, long total_amount,
			String transaction_time) {
		super();
		this.transaction_index = transaction_index;
		this.user_index_t = user_index_t;
		this.deposit = deposit;
		this.withdrawal = withdrawal;
		this.total_amount = total_amount;
		this.transaction_time = transaction_time;
	}

	public long getTransaction_index() {
		return transaction_index;
	}

	public void setTransaction_index(long transaction_index) {
		this.transaction_index = transaction_index;
	}

	public long getUser_index_t() {
		return user_index_t;
	}

	public void setUser_index_t(long user_index_t) {
		this.user_index_t = user_index_t;
	}

	public long getDeposit() {
		return deposit;
	}

	public void setDeposit(long deposit) {
		this.deposit = deposit;
	}

	public long getWithdrawal() {
		return withdrawal;
	}

	public void setWithdrawal(long withdrawal) {
		this.withdrawal = withdrawal;
	}

	public long getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(long total_amount) {
		this.total_amount = total_amount;
	}

	public String getTransaction_time() {
		return transaction_time;
	}

	public void setTransaction_time(String transaction_time) {
		this.transaction_time = transaction_time;
	}
	
}
