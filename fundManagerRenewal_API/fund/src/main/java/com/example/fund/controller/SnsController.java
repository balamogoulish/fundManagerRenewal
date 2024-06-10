package com.example.fund.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fund.mapper.SnsMapper;
import com.example.fund.model.Sns;

@RestController
public class SnsController {
	
	private SnsMapper mapper;
	
	public SnsController(SnsMapper mapper) {
		this.mapper = mapper;
	}
	
	@GetMapping("/fund/sns")
	public Sns getSns(@RequestParam("sns_id") String sns_id, @RequestParam("sns_type") String sns_type) {
		return mapper.getSns(sns_id, sns_type);
	}
	
	@PostMapping("/fund/sns")
	public void postSns(@RequestParam("id") long id, @RequestParam("sns_id") String sns_id, @RequestParam("sns_type") String sns_type) {
		mapper.insertSns(id, sns_id, sns_type);
	}
}
