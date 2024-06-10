package com.example.fund.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.fund.model.Transaction;

@Mapper
public interface TransactionMapper {
	
	@Select("SELECT * FROM transaction WHERE user_index_t=#{user_index_t} ORDER BY transaction_time DESC LIMIT 1")
	Transaction getTransaction(@Param("user_index_t") long user_index_t);
	
	@Select("SELECT * FROM transaction WHERE user_index_t=#{user_index_t} ORDER BY transaction_time DESC")
	List<Transaction> getTransactionList(@Param("user_index_t") long user_index_t);

	@Insert("INSERT INTO transaction (user_index_t, deposit, withdrawal, total_amount) VALUES(#{user_index_t}, #{deposit}, #{withdrawal}, #{total_amount})")
	int insertTransaction(@Param("user_index_t") long user_index_t, @Param("deposit") long deposit, @Param("withdrawal") long withdrawal, @Param("total_amount") long total_amount);

}
