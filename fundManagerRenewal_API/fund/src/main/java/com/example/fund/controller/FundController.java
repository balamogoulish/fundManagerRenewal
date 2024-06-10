package com.example.fund.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fund.mapper.FundMapper;
import com.example.fund.model.Fund;


@RestController
public class FundController {
	private FundMapper mapper;
	
	public FundController(FundMapper mapper) {
		this.mapper = mapper;
	}
	
	@GetMapping("/fund")
	public Fund getFund() {
		return mapper.getFund();
	}
	
	@GetMapping("/fund/all")
	public List<Fund> getFundList(){
		return mapper.getFundList();
	}
	
	@PutMapping("/fund")
	public void putFund(@RequestParam("fund_money") long fund_money, @RequestParam("fund_gain") long fund_gain, @RequestParam("fund_output") long fund_output, @RequestParam("least") long least) {
		mapper.insertFund(fund_money, fund_gain, fund_output, least);
	}
	
	@PostMapping("/fund/{fund_index}")
	public void postFund(@PathVariable("fund_index") long fund_index, @RequestParam("fund_money") long fund_money, @RequestParam("fund_gain") long fund_gain, @RequestParam("fund_output") long fund_output, @RequestParam("least") long least) {
		mapper.updateFund(fund_index, fund_money, fund_gain, fund_output, least);
	}
	
	@PostMapping("/fund/plus")
	public void postPlusLeast(@RequestParam("change") String change) {
		mapper.plusLeast(change);
	}
	
	@PostMapping("/fund/minus")
	public void postMinusLeast(@RequestParam("change") String change) {
		mapper.minusLeast(change);
	}

	
}
