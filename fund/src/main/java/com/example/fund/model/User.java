package com.example.fund.model;

public class User {
	private long user_index;
	private String username;
	private String id;
	private String password;
	private String email;
	private String account;
	private String register_time;
	public User(long user_index, String username, String id, String password, String email, String account,
			String register_time) {
		super();
		this.user_index = user_index;
		this.username = username;
		this.id = id;
		this.password = password;
		this.email = email;
		this.account = account;
		this.register_time = register_time;
	}
	public long getUser_index() {
		return user_index;
	}
	public void setUser_index(long user_index) {
		this.user_index = user_index;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getRegister_time() {
		return register_time;
	}
	public void setRegister_time(String register_time) {
		this.register_time = register_time;
	}
	
	
}
