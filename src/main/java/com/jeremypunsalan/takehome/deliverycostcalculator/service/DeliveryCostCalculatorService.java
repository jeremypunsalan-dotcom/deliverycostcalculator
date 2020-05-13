package com.jeremypunsalan.takehome.deliverycostcalculator.service;

import java.math.BigDecimal;

import org.springframework.web.client.RestClientException;

import com.jeremypunsalan.takehome.deliverycostcalculator.exception.DeliveryRejectedException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ValidationException;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.Delivery;


public interface DeliveryCostCalculatorService {
	
	BigDecimal calculateDeliveryCost(Delivery delivery, Boolean isStatic) throws ValidationException, RestClientException, DeliveryRejectedException;
	
	BigDecimal getDiscountFromVoucherApi(String voucherCode, String key) throws ValidationException, RestClientException;

}
