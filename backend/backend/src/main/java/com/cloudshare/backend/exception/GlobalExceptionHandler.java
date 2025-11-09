package com.cloudshare.backend.exception;

import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.dao.DuplicateKeyException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<?> handleDuplicateEmailException(DuplicateKeyException e){
		
		Map<String, Object> data = new HashMap<>();
		data.put("status", HttpStatus.CONFLICT);
		data.put("message", "Email already exisits");
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(data);
	}
}
