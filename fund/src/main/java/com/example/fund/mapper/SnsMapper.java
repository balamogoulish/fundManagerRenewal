package com.example.fund.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.fund.model.Sns;

@Mapper
public interface SnsMapper {
	
	@Select("SELECT * FROM sns_info WHERE sns_id=#{sns_id} AND sns_type=#{sns_type}")
	Sns getSns(@Param("sns_id") String sns_id, @Param("sns_type") String sns_type);
	
	@Insert("INSERT INTO sns_info (id, sns_id, sns_type) VALUES(#{id}, #{sns_id}, #{sns_type})")
	int insertSns(@Param("id") long id, @Param("sns_id") String sns_id, @Param("sns_type") String sns_type);
}
