package com.contact.service;

import com.contact.payload.UserDTO;

public interface UserService{
	UserDTO saveUser(UserDTO userDTO);
	
	UserDTO loadByUsername(String username);
	
	void deleteContact(int userId);
}
