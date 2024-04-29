package com.electronic.store.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.electronic.store.entities.User;
import com.electronic.store.repositories.UserRepository;

@Service 
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userReposiotry;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userReposiotry.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User with given email not found!!!!"));
		
		return user;
	}

}
