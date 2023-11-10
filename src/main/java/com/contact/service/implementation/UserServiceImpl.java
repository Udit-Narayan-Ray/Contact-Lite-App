package com.contact.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.contact.entities.User;
import com.contact.exceptions.ResourceNotFoundException;
import com.contact.payload.UserDTO;
import com.contact.repository.UserRepo;
import com.contact.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDTO saveUser(UserDTO userDTO) {
		userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		userDTO.setRole("ROLE_USER");
		userDTO.setEnabled(true);
		User user = this.modelMapper.map(userDTO, User.class);
		
		User savedUser = this.userRepo.save(user);
		
		return this.modelMapper.map(savedUser, UserDTO.class);
	}

	@Override
	public UserDTO loadByUsername(String username) {
			
		User user = this.userRepo.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException("User",0, username));
		
		return this.modelMapper.map(user,UserDTO.class);
	}
	
	@Override
	public void deleteContact(int userId) {
		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User",userId,"userId"));
		this.userRepo.delete(user);
	}
	
	
}
