package com.jeremypunsalan.takehome.deliverycostcalculator.service;

import java.util.List;

import com.jeremypunsalan.takehome.deliverycostcalculator.exception.DeliveryEntityConversionException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ResourceNotFoundException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ValidationException;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.DeliveryRules;

public interface RulesCRUDService {

	DeliveryRules save(DeliveryRules deliveryRules) throws ValidationException, DeliveryEntityConversionException, ResourceNotFoundException ;

	DeliveryRules delete(Integer deliveryRuleId) throws ResourceNotFoundException, DeliveryEntityConversionException;

	List<DeliveryRules> fetchAllDeliveryRules() throws DeliveryEntityConversionException, ResourceNotFoundException;

	DeliveryRules fetchDeliveryRulesById(Integer deliveryRuleId) throws ResourceNotFoundException, DeliveryEntityConversionException;

}
