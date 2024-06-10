package com.example.fund.model;

public class Fund {
	private long fund_index;
	private long fund_money;
	private long fund_gain;
	private long fund_output;
	private long least;
	private String fund_time;
	
	public Fund(long fund_index, long fund_money, long fund_gain, long fund_output, long least, String fund_time) {
		super();
		this.fund_index = fund_index;
		this.fund_money = fund_money;
		this.fund_gain = fund_gain;
		this.fund_output = fund_output;
		this.least = least;
		this.fund_time = fund_time;
	}

	public long getFund_index() {
		return fund_index;
	}

	public void setFund_index(long fund_index) {
		this.fund_index = fund_index;
	}

	public long getFund_money() {
		return fund_money;
	}

	public void setFund_money(long fund_money) {
		this.fund_money = fund_money;
	}

	public long getFund_gain() {
		return fund_gain;
	}

	public void setFund_gain(long fund_gain) {
		this.fund_gain = fund_gain;
	}

	public long getFund_output() {
		return fund_output;
	}

	public void setFund_output(long fund_output) {
		this.fund_output = fund_output;
	}

	public long getLeast() {
		return least;
	}

	public void setLeast(long least) {
		this.least = least;
	}

	public String getFund_time() {
		return fund_time;
	}

	public void setFund_time(String fund_time) {
		this.fund_time = fund_time;
	}
	
	
}
