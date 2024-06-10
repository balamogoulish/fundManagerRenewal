package com.example.fund.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fund.mapper.GainMapper;
import com.example.fund.model.Gain;

@RestController
public class GainController {
	private GainMapper mapper;
	
	public GainController(GainMapper mapper) {
		this.mapper = mapper;
	}
	
	@GetMapping("/fund/gain/{user_index_g}")
	public Gain getGain(@PathVariable("user_index_g") long user_index_g) {
		return mapper.getGain(user_index_g);
	}
	
	@GetMapping("/fund/gain/list/{user_index_g}")
	public List<Gain> getGainList(@PathVariable("user_index_g") long user_index_g){
		return mapper.getGainList(user_index_g);
	}
	
	@PostMapping("/fund/gain")
	public void postGain(@RequestParam("user_index_g") long user_index_g, @RequestParam("gain") double gain, @RequestParam("principal") long principal) {
		mapper.insertGain(user_index_g, gain, principal);
	}
	
	@PutMapping("/fund/gain/{user_index_g}")
	public void putGain(@PathVariable("user_index_g") long user_index_g, @RequestParam("gain") double gain, @RequestParam("principal") long principal) {
		mapper.updateGain(user_index_g, gain, principal);
	}
	
	@DeleteMapping("/fund/gain/{gain_index}")
	public void deleteGain(@PathVariable("gain_index") long gain_index) {
		mapper.deleteGain(gain_index);
	}
	
}
