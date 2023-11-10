package com.contact.service;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.contact.entities.Contact;
import com.contact.payload.ContactDTO;

public interface ContactService {
	
	ContactDTO saveContact(ContactDTO contactDTO,String email);
	
	ContactDTO findContact(Integer contactId);
	
	void updateContact(ContactDTO contactDTO,MultipartFile imageFile,HttpSession session, Principal principal);
	
	void deleteContact(Integer contactId,String email);
	
	Page<ContactDTO> findContactsByUser(String email,Integer page);
	
	String changePassword(String oldPass,String newPass,String cnewPass,String username);
	
	List<Contact> search(String nameKeyword,String email);
}
