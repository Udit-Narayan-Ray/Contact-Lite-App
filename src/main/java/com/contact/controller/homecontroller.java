package com.contact.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.payload.Message;
import com.contact.payload.UserDTO;
import com.contact.service.UserService;
import com.contact.service.implementation.EmailSendService;

@Controller
public class homecontroller {

	@Autowired
	private EmailSendService emailSendService;

	@Autowired
	private UserService userService;

	@ModelAttribute
	public void addUserToAllModel(Model model, Principal principal) {
		if (principal != null && principal.getName() != null) {
			UserDTO userDTO = this.userService.loadByUsername(principal.getName());
			model.addAttribute("user", userDTO);
		}
	}

	@RequestMapping("/")
	public String getH(Model model, HttpSession session) {
		model.addAttribute("title", "Home");
		
		//remove all session message
		session.removeAttribute("message");
		
		return "home";
	}

	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About");

		return "redirect:/";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register");

		// we have to pass a object of user to store all user credentials from user into
		// that object
		model.addAttribute("userDTO", new UserDTO());

		return "signup";
	}

	@RequestMapping("/login")
	public String login(Model model, Principal principal,HttpSession session) {
		model.addAttribute("title", "Sign In");

		return "login";
	}

	@PostMapping("/verify_account")
	public String verifyEmail(@ModelAttribute("userDTO") UserDTO userDTO,
			@RequestParam(value = "confirm", defaultValue = "false") boolean confirm, Model model,
			HttpSession session) {

		model.addAttribute("title","Account Verification");
		
		if(!confirm)
		{
			model.addAttribute("userDTO", userDTO);
			session.setAttribute("message", new Message("!! You have not checked the details box !!", "alert-danger"));
			
			return "signup";
		}
		
		//delete the session attribute if already populated and have not been verified the otp and trying again
		session.removeAttribute("otp");
		session.removeAttribute("username");
		session.removeAttribute("registeringUser");
		
		String subject="Account Verification";
		String body="OTP for Email Verification of Contact Lite App";
		
		this.emailSendService.sendEmail(userDTO.getEmail(), subject, body, session);

		//set the user to the session after all the thing passed
		session.setAttribute("registeringUser", userDTO);

		return "verifyEmail";
	}

	@PostMapping("/verify_email_otp")
	public String verifyEmail(@RequestParam("vOTP")int vOTP,Model model,HttpSession session) {
		
		model.addAttribute("title","Verify OTP");
		
		int otp=(int)session.getAttribute("otp");
		
		if(otp==vOTP)
		{
			UserDTO userDTO = (UserDTO) session.getAttribute("registeringUser");
			
			if(register(userDTO, model, session))
			{
				return "signup";
			}
					
		}
		
		session.setAttribute("message",new Message("Invalid OTP", "alert-danger"));
		
		return "verifyEmail";
	}
	
	
//	registering function to be called after email verfication
	public boolean register(UserDTO userDTO ,Model model,HttpSession session) {

		boolean status=false;

		if(userDTO!=null)
		{
			status=true;
			try {
				
				this.userService.saveUser(userDTO);
	
				session.setAttribute("message", new Message("!! Successfully Register !!", "alert-success"));
	
				// below is for giving new object if successfully register for new fill up
				model.addAttribute("userDTO", new UserDTO());
	
			} catch (Exception e) {
	
				// below is for populating the field in front end with passed input
				model.addAttribute("userDTO", userDTO);
				session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			}
		}

		session.removeAttribute("otp");
		session.removeAttribute("username");
		session.removeAttribute("registeringUser");
		
		return status;
	}
}
