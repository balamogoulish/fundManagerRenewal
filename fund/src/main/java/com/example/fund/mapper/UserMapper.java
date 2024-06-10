package com.example.fund.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.fund.model.User;

@Mapper
public interface UserMapper {
	
	//로그인
	@Select("SELECT * FROM user WHERE id=#{id} AND password=#{password}")
	User loginUser(@Param("id") String id, @Param("password") String password);
	
	//조회
	@Select("SELECT * FROM user WHERE user_index=#{user_index}")
	User getUser(@Param("user_index") long user_index);
	
	//아이디 중복 조회
	@Select("SELECT * FROM user WHERE id=#{id}")
	User getUserId(@Param("id") String id);
	
	//이메일을 통한 아이디 조회
	@Select("SELECT * FROM user WHERE email=#{email}")
	User getUserEmail(@Param("email") String email);
	
	//전체 조회
	@Select("SELECT * FROM user")
	List<User> getUserList();
	
	@Insert("INSERT INTO user (username, id, password, email, account) VALUES(#{username}, #{id}, #{password}, #{email}, #{account})")
	int insertUser(@Param("username") String username, @Param("id") String id, @Param("password") String password, @Param("email") String email, @Param("account") String account);
	
	//정보 수정
	@Update("UPDATE user SET username=#{username}, id=#{id}, email=#{email}, account=#{account} WHERE user_index=#{user_index}")
	int updateUser(@Param("user_index") long user_index, @Param("username") String username, @Param("id") String id, @Param("email") String email, @Param("account") String account);
	
	@Update("UPDATE user SET password=#{password} WHERE id=#{id}")
	int updatePw(@Param("id") String id, @Param("password") String password);
	
	@Delete("DELETE FROM user WHERE user_index=#{user_index}")
	int deleteUser(@Param("user_index") long user_index);
}
