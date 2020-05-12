package com.jeremypunsalan.takehome.deliverycostcalculator.service;

import org.springframework.web.client.RestClientException;

import com.jeremypunsalan.takehome.deliverycostcalculator.exception.DeliveryRejectedException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ValidationException;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.Delivery;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.DeliveryInput;

public interface DeliveryCostCalculatorService {
	
	Double calculateDeliveryCost(DeliveryInput deliveryInput, Boolean isStatic) throws ValidationException, RestClientException, DeliveryRejectedException;
	
	Double fetchDiscountFromVoucher(Delivery delivery) throws ValidationException, RestClientException;

}
