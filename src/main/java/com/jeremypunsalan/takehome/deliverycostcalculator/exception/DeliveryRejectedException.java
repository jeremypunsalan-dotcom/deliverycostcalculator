package com.jeremypunsalan.takehome.deliverycostcalculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK)
public class DeliveryRejectedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DeliveryRejectedException(String message) {
		super(message);
	}

}
