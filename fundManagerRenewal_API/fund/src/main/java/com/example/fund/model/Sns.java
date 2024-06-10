package com.example.fund.model;

public class Sns {
	private long id;
	private String sns_id;
	private String sns_type;
	private String sns_connect_date;
	
	public Sns(long id, String sns_id, String sns_type, String sns_connect_date) {
		super();
		this.id = id;
		this.sns_id = sns_id;
		this.sns_type = sns_type;
		this.sns_connect_date = sns_connect_date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSns_id() {
		return sns_id;
	}

	public void setSns_id(String sns_id) {
		this.sns_id = sns_id;
	}

	public String getSns_type() {
		return sns_type;
	}

	public void setSns_type(String sns_type) {
		this.sns_type = sns_type;
	}

	public String getSns_connect_date() {
		return sns_connect_date;
	}

	public void setSns_connect_date(String sns_connect_date) {
		this.sns_connect_date = sns_connect_date;
	}
	
	
}
