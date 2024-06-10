package com.example.fund.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.fund.model.Fund;

@Mapper
public interface FundMapper {
	
	@Select("SELECT * FROM fund ORDER BY fund_time DESC LIMIT 1")
	Fund getFund();
	
	@Select("SELECT * FROM fund")
	List<Fund> getFundList();
	
	@Insert("INSERT INTO fund (fund_money, fund_gain, fund_output,least) VALUES(#{fund_money}, #{fund_gain}, #{fund_output}, #{least})")
	int insertFund(@Param("fund_money") long fund_money, @Param("fund_gain") long fund_gain, @Param("fund_output") long fund_output, @Param("least") long least);
	
	@Update("UPDATE fund SET fund_money=#{fund_money}, fund_gain=#{fund_gain}, fund_output=#{fund_output}, least=#{least} WHERE fund_index=#{fund_index}")
	int updateFund(@Param("fund_index") long fund_index, @Param("fund_money") long fund_money, @Param("fund_gain") long fund_gain, @Param("fund_output") long fund_output, @Param("least") long least);
	
	@Update("UPDATE fund "
	        + "JOIN (SELECT MAX(fund_time) AS max_fund_time FROM fund) AS sub ON fund.fund_time = sub.max_fund_time "
	        + "SET fund.least = fund.least + #{change}")
	int plusLeast(@Param("change") String change);
	
	@Update("UPDATE fund "
	        + "JOIN (SELECT MAX(fund_time) AS max_fund_time FROM fund) AS sub ON fund.fund_time = sub.max_fund_time "
	        + "SET fund.least = fund.least - #{change}")
	int minusLeast(@Param("change") String change);
}
