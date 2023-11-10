package com.contact.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.payload.Message;
import com.contact.payload.UserDTO;
import com.contact.service.UserService;
import com.contact.service.implementation.EmailSendService;

@Controller
public class forgotPasswordController {
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailSendService emailSendService;
	

	@GetMapping("/forgotPass")
	public String forgot(Model model,HttpSession session)
	{
		model.addAttribute("title","Forgot Password");
		
		
		return "getEmailForgot";
	}
	
	@PostMapping("/otp_sent")
	public String verify( @RequestParam("fpEmail") String email , Model model,HttpSession session)
	{
		model.addAttribute("title","Verify OTP");
		
		UserDTO userDTO = this.userService.loadByUsername(email);
		if(userDTO!=null)
		{
		
			String subject="Forgot Password";
			String body="OTP for change password of Contact Lite App";
			boolean status = this.emailSendService.sendEmail(email, subject, body, session);
			
			if(status)
			{
				session.setAttribute("message",new Message("Check OTP sent to your Email","alert-success"));
			}
			
			
		}
	
				
		return "verifyOTPForgot";
	}
	
	@PostMapping("/verify_otp")
	public String verify(@RequestParam("fpOTP")int fpOTP,Model model,HttpSession session)
	{
		model.addAttribute("title","Change Password");
		int otp=(int) session.getAttribute("otp");
//		int otp=5595;
		
		if(fpOTP!=otp)
		{
			session.setAttribute("message",new Message("OTP is Invalid", "alert-danger"));
			return "verifyOTPForgot";
		}
		
		
		return "forgotPassword";
	}
	
	
	@PostMapping("/change/forgot_password")
	public String change(@RequestParam("nfpass")String nfpass,@RequestParam("cnfpass")String cnfpass,
	Model model,HttpSession session)
	{
		model.addAttribute("title","Change Password");
		
		if(!nfpass.equals(cnfpass))
		{
			session.setAttribute("message",new Message("Password and Confirm Password isn't Matching", "alert-danger"));
			return "forgotPassword";
		}
	String email=(String)session.getAttribute("username");
		UserDTO userDTO = this.userService.loadByUsername(email);
		userDTO.setPassword(cnfpass);
		this.userService.saveUser(userDTO);
		
		session.removeAttribute("otp");
		session.removeAttribute("username");
		
		return "redirect:/login?change=Password Changed Successfully";
	}
	
	
}
