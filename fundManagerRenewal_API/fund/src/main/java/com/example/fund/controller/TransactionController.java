package com.example.fund.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fund.mapper.TransactionMapper;
import com.example.fund.model.Transaction;

@RestController
public class TransactionController {
	private TransactionMapper mapper;
	
	public TransactionController(TransactionMapper mapper) {
		this.mapper = mapper;
	}
	
	@GetMapping("/fund/transaction/{user_index_t}")
	public Transaction getTransaction(@PathVariable("user_index_t") long user_index_t) {
		return mapper.getTransaction(user_index_t);
	}
	
	@GetMapping("/fund/transaction/list/{user_index_t}")
	public List<Transaction> getTransactionList(@PathVariable("user_index_t") long user_index_t){
		return mapper.getTransactionList(user_index_t);
	}
	
	@PostMapping("/fund/transaction")
	public void postTransaction(@RequestParam("user_index_t") long user_index_t, @RequestParam("deposit") long deposit, @RequestParam("withdrawal") long withdrawal, @RequestParam("total_amount") long total_amount) {
		mapper.insertTransaction(user_index_t, deposit, withdrawal, total_amount);
	}
}
