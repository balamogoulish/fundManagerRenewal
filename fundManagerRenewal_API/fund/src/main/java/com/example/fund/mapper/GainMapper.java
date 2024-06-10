package com.example.fund.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.fund.model.Gain;

@Mapper
public interface GainMapper {

	@Select("SELECT * FROM gain WHERE user_index_g=#{user_index_g} ORDER BY gain_time DESC LIMIT 1")
	Gain getGain(@Param("user_index_g") long user_index_g);
	
	@Select("SELECT * FROM gain WHERE user_index_g=#{user_index_g} ORDER BY gain_time DESC")
	List<Gain> getGainList(@Param("user_index_g") long user_index_g);
	
	@Insert("INSERT INTO gain (user_index_g, gain, principal) VALUES(#{user_index_g}, #{gain}, #{principal})")
	int insertGain(@Param("user_index_g") long user_index_g, @Param("gain") double gain, @Param("principal") long principal);
		
	@Update("UPDATE gain\r\n"
			+ "SET gain = #{gain}, principal = #{principal}\r\n"
			+ "WHERE user_index_g = #{user_index_g}\r\n"
			+ "AND gain_time = (\r\n"
			+ "    SELECT MAX(gain_time)\r\n"
			+ "    FROM (\r\n"
			+ "        SELECT gain_time\r\n"
			+ "        FROM gain\r\n"
			+ "        WHERE user_index_g = #{user_index_g}\r\n"
			+ "    ) AS subquery\r\n"
			+ ")\r\n"
			+ "")
	int updateGain(@Param("user_index_g") long user_index_g, @Param("gain") double gain, @Param("principal") long principal);
	
	@Delete("DELETE FROM gain WHERE gain_index=#{gain_index}")
	int deleteGain(@Param("gain_index") long gain_index);
}
