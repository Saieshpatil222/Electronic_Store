package com.electronic.store.exceptions;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.electronic.store.dtos.ApiResponseMessage;

import ch.qos.logback.classic.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {

		logger.info("Exception Handler invoked!!");
		ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND)
				.success(true).build();
		return new ResponseEntity(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BadApiRequestException.class)
	public ResponseEntity<ApiResponseMessage> handelBadApiRequest(BadApiRequestException ex) {

		logger.info("Bad Api Request!!");
		ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST)
				.success(true).build();
		return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
	}
}
