package com.contact.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.contact.entities.Contact;
import com.contact.entities.User;

public interface ContactRepo extends JpaRepository<Contact,Integer>{

//	@Query("from contacts as c where c.user.userId =:userId")
	Page<Contact> findContactByUser(User user,Pageable pageable);
	
	
	//this is for search function to have name and show contact from user's contact list
	List<Contact> findByNameContainingAndUser(String nameKeyword,User user);
}
