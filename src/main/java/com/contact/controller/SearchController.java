package com.contact.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contact.entities.Contact;
import com.contact.service.ContactService;

@RestController

@RequestMapping("/search")
public class SearchController {

	@Autowired
	private ContactService contactService;
	
	@GetMapping("/{nameKeyword}")
	public ResponseEntity<?> search(@PathVariable("nameKeyword")String nameKeyword,Principal principal)
	{
		List<Contact> searchedList = this.contactService.search(nameKeyword, principal.getName());
		
		return ResponseEntity.ok(searchedList);
	}
	
}
