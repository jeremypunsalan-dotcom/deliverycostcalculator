package com.jeremypunsalan.takehome.deliverycostcalculator.exception;

import java.util.Date;

public class ExceptionDetails {

	private Date timestamp;
	private Integer status;
	private String message;
	private String details;

	public ExceptionDetails(Date timestamp, Integer status, String message, String details) {
	  super();
	  this.timestamp = timestamp;
	  this.status = status;
	  this.message = message;
	  this.details = details;
	 }

	public Date getTimestamp() {
		return timestamp;
	}
	
	public Integer getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

}
