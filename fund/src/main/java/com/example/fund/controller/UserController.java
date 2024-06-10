package com.example.fund.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fund.model.User;
import com.example.fund.mapper.UserMapper;

@RestController
public class UserController {
	
	private UserMapper mapper;
	
	public UserController(UserMapper mapper) {
		this.mapper = mapper;
	}
	
	@GetMapping("/fund/user/{user_index}")
	public User getUser(@PathVariable("user_index") long user_index) {
		return mapper.getUser(user_index);
	}
	
	@GetMapping("/fund/user/{id}/{password}")
	public User loginUser(@PathVariable("id") String id,@PathVariable("password") String password) {
		return mapper.loginUser(id, password);
	}
	
	@GetMapping("/fund/user/id/{id}")
	public User getUserId(@PathVariable("id") String id) {
		return mapper.getUserId(id);
	}
	
	@GetMapping("/fund/user/all")
	public List<User> getUserList(){
		return mapper.getUserList();
	}
	
	@GetMapping("/fund/user/email/{email}")
	public User getUserEmail(@PathVariable("email") String email) {
		return mapper.getUserEmail(email);
	}
	
	@PostMapping("/fund/user")
	public User postUser(@RequestParam("username") String username, @RequestParam("id") String id,@RequestParam("password") String password,@RequestParam("email") String email,@RequestParam("account") String account) {
		mapper.insertUser(username, id, password, email, account);
		return mapper.getUserId(id);
	}
	
	@PutMapping("/fund/user/{user_index}")
	public void putUserProfile(@PathVariable("user_index") long user_index, @RequestParam("username") String username, @RequestParam("id") String id,@RequestParam("email") String email,@RequestParam("account") String account) {
		mapper.updateUser(user_index, username, id, email, account);
	}
	
	@PutMapping("/fund/user/pw/{id}")
	public void putPw(@PathVariable("id") String id, @RequestParam("password") String password) {
		mapper.updatePw(id, password);
	}
	
	@DeleteMapping("/fund/user/{user_index}")
	public void deleteUser(@PathVariable("user_index") long user_index) {
		mapper.deleteUser(user_index);
	}
	
	@Autowired
    RegisterMail registerMail;
    @PostMapping(value = "/fund/user/email")
    public String mailConfirm(@RequestParam("email") String email) throws Exception{
        String code = registerMail.sendSimpleMessage(email);
        System.out.println("사용자에게 발송한 인증코드 ==> " + code);

        return code;
    }
    
    @Autowired
    NewPwMail newPwMail;
    @PostMapping(value = "/fund/user/email/newPw")
    public String newPwMail(@RequestParam("email") String email) throws Exception{
        String code = newPwMail.sendSimpleMessage(email);
        System.out.println("사용자에게 발송한 재발급 비밀번호 ==> " + code);

        return code;
    }
	
}
