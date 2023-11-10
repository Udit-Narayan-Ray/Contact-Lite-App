package com.contact.service.implementation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.contact.entities.Contact;
import com.contact.entities.User;
import com.contact.exceptions.ResourceNotFoundException;
import com.contact.payload.ContactDTO;
import com.contact.payload.UserDTO;
import com.contact.repository.ContactRepo;
import com.contact.repository.UserRepo;
import com.contact.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ContactRepo contactRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public ContactDTO saveContact(ContactDTO contactDTO, String email) {

		User user = this.userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", 0, email));
		Contact contact = this.modelMapper.map(contactDTO, Contact.class);
		contact.setUser(user);

		Contact savedContact = this.contactRepo.save(contact);

		return this.modelMapper.map(savedContact, ContactDTO.class);
	}

	@Override
	public Page<ContactDTO> findContactsByUser(String email, Integer  page) {
		
		User user = this.userRepo.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User", 0,email));
		
		Pageable pageable=PageRequest.of(page, 1);
		Page<Contact> contactPage = this.contactRepo.findContactByUser(user,pageable);

		//		List<ContactDTO> contactDTOs = contacts.stream().map(c -> this.modelMapper.map(c,ContactDTO.class)).collect(Collectors.toList());
		
		Page<ContactDTO> contactDTOPage=this.mapEntityPageIntoDtoPage(contactPage,ContactDTO.class);
		
		return contactDTOPage;
	}
	
	
	//this method will convert the entity  page to dto page help from exposing the entity
	public <D, T> Page<D> mapEntityPageIntoDtoPage(Page<T> contact, Class<D> contactDTO) {
		
	    return contact.map(page -> this.modelMapper.map(page, contactDTO));
	}

	@Override
	public ContactDTO findContact(Integer contactId) {

		Contact contact = this.contactRepo.findById(contactId).orElseThrow(()-> new ResourceNotFoundException("Contact", contactId, "Contact_Id"));
		
		
		return this.modelMapper.map(contact, ContactDTO.class);
	}

	@Override
	public void deleteContact(Integer contactId,String email) {

		Contact contact = this.contactRepo.findById(contactId).orElseThrow(()-> new  ResourceNotFoundException("Contact", contactId,"contact_id"));
		User user = this.userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User",0, email));
		//now we if remove the contact from set or list and due to ORPHAN REMOVAL as TRUE contact will be deleted
		
		try {
			
			if(!contact.getImage().equals("default_pp.jpg"))
			{
				
				// Fetch the location here it is:- static/img
				File deletingPath = new ClassPathResource("static/img").getFile();
				File deletePath=new File(deletingPath, contact.getImage());
				deletePath.delete();
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		// this is for break the link of contact with user otherwise it won't get deleted
		//		contact.setUser(null);
		
		//		this.contactRepo.delete(contact);
		
		
		
		//due to cascading used in user deleting from user side 
		
		user.getContacts().remove(contact);
		this.userRepo.save(user);
	}

	@Override
	public  void updateContact(ContactDTO contactDTO, MultipartFile imageFile,HttpSession session,Principal principal) {

		Contact originalContact = this.contactRepo.findById(contactDTO.getContactId()).orElseThrow(()-> new ResourceNotFoundException("Contact", contactDTO.getContactId(), "Contact_id"));
		User user = this.userRepo.findByEmail(principal.getName()).orElseThrow(()-> new ResourceNotFoundException("User",0, "email"+principal.getName()));
		
		contactDTO.setUser(this.modelMapper.map(user,UserDTO.class));
		if(!imageFile.isEmpty())
		{
			try {
			//delete the old image from contact
			
			if(!originalContact.getImage().equals("default_pp.jpg"))
			{
				
				// Fetch the location here it is:- static/img
				File deletingPath;
				
					deletingPath = new ClassPathResource("static/img").getFile();
					File deletePath=new File(deletingPath, originalContact.getImage());
					
					//update the new image to the contact if previous image is deleted
					deletePath.delete();			
			}
			contactDTO.setImage(imageFile.getOriginalFilename());

			// Fetch the location :- static/img
			File savingPath = new ClassPathResource("static/img").getFile();

			// saving to same location
			Path savedPath = Paths.get(savingPath.getAbsolutePath() + File.separator + imageFile.getOriginalFilename());

			// copy the content from file to saving file
			Files.copy(imageFile.getInputStream(), savedPath, StandardCopyOption.REPLACE_EXISTING);
			
			
			
		} catch (Exception e) {
			session.setAttribute(e.getMessage(),"alert-danger");
		}
		
		}
		else {

			contactDTO.setImage(originalContact.getImage());
		}
		
		Contact contact = this.modelMapper.map(contactDTO, Contact.class);
		this.contactRepo.save(contact);
		
	}

	
	@Override
	public String changePassword(String oldPass, String newPass,String cnewPass, String username) {
		
		User user = this.userRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User", 0, "email"+username));
		if(passwordEncoder.matches(oldPass,user.getPassword()))
		{
			System.out.println(oldPass);
			if(newPass.equals(cnewPass))
			{
				user.setPassword(this.passwordEncoder.encode(cnewPass));
				this.userRepo.save(user);
				
				return "success";
			}
			else {
				return "new";
			}
		}
		else {
			return "old";
		}	
	}
	
	//search
	@Override
	public List<Contact> search(String nameKeyword, String email) {

		User user = this.userRepo.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User", 0, "email"));
		List<Contact> searchedContacts = this.contactRepo.findByNameContainingAndUser(nameKeyword, user);
		
		return searchedContacts;
	} 
	

}
