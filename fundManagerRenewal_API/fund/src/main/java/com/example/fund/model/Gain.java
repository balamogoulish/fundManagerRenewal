package com.example.fund.model;

public class Gain {
	private long gain_index;
	private long user_index_g;
	private double gain;
	private long principal;
	private String gain_time;
	
	public Gain(long gain_index, long user_index_g, double gain, long principal, String gain_time) {
		super();
		this.gain_index = gain_index;
		this.user_index_g = user_index_g;
		this.gain = gain;
		this.principal = principal;
		this.gain_time = gain_time;
	}

	public long getGain_index() {
		return gain_index;
	}

	public void setGain_index(long gain_index) {
		this.gain_index = gain_index;
	}

	public long getUser_index_g() {
		return user_index_g;
	}

	public void setUser_index_g(long user_index_g) {
		this.user_index_g = user_index_g;
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

	public long getPrincipal() {
		return principal;
	}

	public void setPrincipal(long principal) {
		this.principal = principal;
	}

	public String getGain_time() {
		return gain_time;
	}

	public void setGain_time(String gain_time) {
		this.gain_time = gain_time;
	}
	
	
}
