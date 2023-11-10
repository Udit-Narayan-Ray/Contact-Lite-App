package com.contact.payload;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {
	
	private Integer userId;

	private String name;

	private String email;

	private String password;

	private String role;

	private String about;

	private boolean enabled;
	
	private Set<ContactDTO> contacts=new HashSet<>();

	
	public UserDTO() {
		// TODO Auto-generated constructor stub
	}


	public UserDTO(Integer userId, String name, String email, String password, String role, String about,
			boolean enabled, Set<ContactDTO> contacts) {
		super();
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.about = about;
		this.enabled = enabled;
		this.contacts = contacts;
	}


	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getAbout() {
		return about;
	}


	public void setAbout(String about) {
		this.about = about;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<ContactDTO> getContacts() {
		return contacts;
	}


	public void setContacts(Set<ContactDTO> contacts) {
		this.contacts = contacts;
	}


	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", name=" + name + ", email=" + email + ", password=" + password
				+ ", role=" + role + ", about=" + about + ", enabled=" + enabled + ", contacts=" + contacts + "]";
	}
	
}
