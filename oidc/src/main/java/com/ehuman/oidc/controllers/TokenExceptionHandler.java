package com.ehuman.oidc.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TokenExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public String exceptionHandles() {
		return "error";
	}
	

}
