package com.jeremypunsalan.takehome.deliverycostcalculator.config;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

import com.jeremypunsalan.takehome.deliverycostcalculator.exception.DeliveryEntityConversionException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.DeliveryRejectedException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ExceptionDetails;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ResourceNotFoundException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ValidationException;

@ControllerAdvice
public class DeliveryCostExceptionsHandler {
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> validationException(ValidationException ex, WebRequest request) {
		ExceptionDetails detail = new ExceptionDetails(new Date(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(detail, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ExceptionDetails detail = new ExceptionDetails(new Date(), HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DeliveryRejectedException.class)
	public ResponseEntity<?> deliveryRejectedException(DeliveryRejectedException ex, WebRequest request) {
		ExceptionDetails detail = new ExceptionDetails(new Date(), HttpStatus.OK.value(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(detail, HttpStatus.OK);
	}
	
	@ExceptionHandler(DeliveryEntityConversionException.class)
	public ResponseEntity<?> deliveryEntityConversionException(DeliveryEntityConversionException ex, WebRequest request) {
		ExceptionDetails detail = new ExceptionDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(detail, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<?> restClientException(DeliveryRejectedException ex, WebRequest request) {
		ExceptionDetails detail = new ExceptionDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(detail, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> exception(DeliveryRejectedException ex, WebRequest request) {
		ExceptionDetails detail = new ExceptionDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(detail, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
