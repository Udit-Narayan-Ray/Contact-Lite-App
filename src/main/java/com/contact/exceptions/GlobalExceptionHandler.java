package com.contact.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.contact.payload.Message;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Message> resouceNotFoundException(ResourceNotFoundException e)
	{
		return new ResponseEntity<Message>(new Message(e.getMessage(),"Error"),HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({org.hibernate.exception.ConstraintViolationException.class})
	public ResponseEntity<Message> ConstrainViolationException(Exception e)
	{
		
		
		return new ResponseEntity<Message>(new Message("Check Email","Email"),HttpStatus.BAD_REQUEST);
	}
}
