package com.jeremypunsalan.takehome.deliverycostcalculator.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeremypunsalan.takehome.deliverycostcalculator.exception.DeliveryEntityConversionException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ResourceNotFoundException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ValidationException;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.DeliveryRules;
import com.jeremypunsalan.takehome.deliverycostcalculator.service.RulesCRUDService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="DeliveryCostRulesResource", description="Rest API for CRUD Operations of Delivery Rules")
@RestController
@CrossOrigin
@RequestMapping(value = "/rest/deliverycostrules")
public class DeliveryCostRulesResource {
	
	@Autowired
	@Qualifier("RulesCRUDService")
	private RulesCRUDService rulesCRUDService;
	
	@ApiOperation(value = "Create / Update Rules", response = DeliveryRules.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Validation Exception"),
			@ApiResponse(code = 404, message = "Resource Not Found Exception"),
			@ApiResponse(code = 500, message = "Internal Exception")
			
	})
	@PostMapping(value="/save")
	public DeliveryRules save(@RequestBody final DeliveryRules deliveryRules) throws ValidationException, DeliveryEntityConversionException, ResourceNotFoundException { 
		return rulesCRUDService.save(deliveryRules);
	}
	
	@ApiOperation(value = "Delete Rules", response = DeliveryRules.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Resource Not Found Exception"),
			@ApiResponse(code = 500, message = "Internal Exception")
			
	})
	@GetMapping(value="/delete/{id}")
	public DeliveryRules delete(@PathVariable("id") final Integer deliveryRuleId) throws ResourceNotFoundException, DeliveryEntityConversionException { 
		return rulesCRUDService.delete(deliveryRuleId);
	}
	
	@ApiOperation(value = "Retrieve All Rules in Priority Order", response = List.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Resource Not Found Exception"),
			@ApiResponse(code = 500, message = "Internal Exception")
			
	})
	@GetMapping(value="/retrieve/all")
	public List<DeliveryRules> getAllDeliveryRules() throws DeliveryEntityConversionException, ResourceNotFoundException  { 
		return rulesCRUDService.fetchAllDeliveryRules();
	}
	
	@ApiOperation(value = "Retrieve a Rule based on rule id", response = DeliveryRules.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Resource Not Found Exception"),
			@ApiResponse(code = 500, message = "Internal Exception")
			
	})
	@GetMapping(value="/retrieve/{id}")
	public DeliveryRules getDeliveryRuleById(@PathVariable("id") final Integer deliveryRuleId) throws ResourceNotFoundException, DeliveryEntityConversionException { 
		return rulesCRUDService.fetchDeliveryRulesById(deliveryRuleId);
	}
	
	

}
