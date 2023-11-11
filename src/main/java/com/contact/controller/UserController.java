package com.contact.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contact.payload.ContactDTO;
import com.contact.payload.Message;
import com.contact.payload.UserDTO;
import com.contact.service.ContactService;
import com.contact.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ContactService contactService;

	// TO extract the user and add to all model
	@ModelAttribute
	public void addUserToAllModel(Model model, Principal principal) {
		
			UserDTO userDTO = this.userService.loadByUsername(principal.getName());
			model.addAttribute("user", userDTO);
		
	}

	// for retrieving the profile page of user
	@RequestMapping("/profile")
	public String getDashBoard(Model model) {

		model.addAttribute("title", "User Dashboard");
		
		return "normal/user_dashboard";
	}
	
	//settings
	@GetMapping("/setting")
	public String setting(Model model)
	{
		model.addAttribute("title","Setting Page");
		
		return "setting";
	}

	// retrieving page of adding contact for the user
	@RequestMapping("/add_contact")
	public String Contact(Model model) {
		
		model.addAttribute("title", "ADD CONTACT");
		model.addAttribute("contactDTO", new ContactDTO());

		return "normal/add_contact";
	}

	// saving contact for the user
	@PostMapping("/process_contact")
	public String addContact(@ModelAttribute("contactDTO") ContactDTO contactDTO,
			@RequestParam("contactImage") MultipartFile imageFile, Model model, Principal principal,
			HttpSession session) throws Exception {

		String user = principal.getName();

		if (user == null) {
			throw new Exception("User Not Found");
		}
		
		// to check if the image is uploaded not empty
		if (!imageFile.isEmpty()) {
			contactDTO.setImage(imageFile.getOriginalFilename());

			// Fetch the location here it is:- static/img
			File savingPath = new ClassPathResource("static/img").getFile();

			// saving to same location
			Path savedPath = Paths.get(savingPath.getAbsolutePath() + File.separator + imageFile.getOriginalFilename());

			// copy the content from file to saving file
			Files.copy(imageFile.getInputStream(), savedPath, StandardCopyOption.REPLACE_EXISTING);
		} else {
			contactDTO.setImage("default_pp.jpg");
		}
		
		try {

			contactService.saveContact(contactDTO, user);

			session.setAttribute("message", new Message("Successfully Contact saved", "alert-success"));
			model.addAttribute("contactDTO", new ContactDTO());
		} catch (Exception e) {
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			model.addAttribute("contactDTO", contactDTO);
		}

		return "normal/add_contact";
	}

	// for showing all contacts of the user
	@GetMapping("/view_contact/{page}")
	public String viewContact(@PathVariable("page") Integer page, Model model, Principal principal) {

		model.addAttribute("title", "Contact Lists");

		String email = principal.getName();

		Page<ContactDTO> contactDTOs = this.contactService.findContactsByUser(email, page);

		model.addAttribute("contactDTOs", contactDTOs);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contactDTOs.getTotalPages());

		return "normal/view_contact";
	}

	// for showing particular contact details
	@GetMapping("/show_contact_details/{cid}")
	public String showContact(@PathVariable("cid") Integer contactId, Model model) {

		ContactDTO contact = this.contactService.findContact(contactId);

		model.addAttribute("contact", contact);

		return "normal/show_1_contact";
	}

	// for deleting the contact
	@GetMapping("/delete_contact/{cid}")
	public String deleteContact(@PathVariable("cid") Integer contactId, Model model, HttpSession session,
			Principal principal) throws IOException {

				
		this.contactService.deleteContact(contactId, principal.getName());

		session.setAttribute("message", new Message("Contact Deleted Successfully", "alert-success"));

		return "redirect:/user/view_contact/0";
	}

	@GetMapping("/update_contact/{cid}")
	public String updateContact(@PathVariable("cid") Integer contactId, Model model) {

		model.addAttribute("title", "Update Contact");
		ContactDTO contactDTO = this.contactService.findContact(contactId);
		model.addAttribute("updateContact", contactDTO);

		return "/normal/update_contact";
	}

	@PostMapping("/update_contact")
	public String updateContact(@ModelAttribute ContactDTO updateContact, @RequestParam("cId") Integer contactId,
			@RequestParam("contactImage") MultipartFile imageFile,Model model, HttpSession session, Principal principal) {

		
			updateContact.setContactId(contactId);
			
			ContactDTO contact = this.contactService.findContact(contactId);
			if(updateContact.getDescription().length()==0)
			{
				updateContact.setDescription(contact.getDescription());
			}
		
			this.contactService.updateContact(updateContact,imageFile,session,principal);
			
			session.setAttribute("message",new Message("Contact Updated Successfully", "alert-success"));
			
		

		return "redirect:/user/view_contact/0";
	}

	
	//change password
	@PostMapping("/changePass")
	public String changePassword(@RequestParam("opass")String oldPass, @RequestParam("npass")String newPass
			,@RequestParam("cnpass")String cnewPass,Model model, Principal principal, HttpSession session) {
		
	String result=this.contactService.changePassword(oldPass, newPass, cnewPass, principal.getName());
		
	if(result.equals("old"))
	{
		session.setAttribute("message", new Message("Old Password didn't Matched","alert-danger"));
	}
	else if(result.equals("new")) {
		session.setAttribute("message", new Message("New Password and Confirm Password didn't Matched","alert-danger"));

	}
	else if(result.equals("success"))
	{
		session.setAttribute("message", new Message("Password Updated Successfully","alert-success"));
	}
	
		return "redirect:/user/setting";
		
	}
	
	@GetMapping("/delete_account/{uid}")
	public String deleteUser(@PathVariable("uid")int userId,Model model,HttpSession session)
	{
		
		this.userService.deleteContact(userId);
		model.addAttribute("title","Account Deletion");
		
		//logout the user from spring security to avoid resource not found exception
		
		return "redirect:/logout?";
	}
}
