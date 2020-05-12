package com.jeremypunsalan.takehome.deliverycostcalculator.resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.jeremypunsalan.takehome.deliverycostcalculator.exception.DeliveryRejectedException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ValidationException;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.Delivery;
import com.jeremypunsalan.takehome.deliverycostcalculator.service.DeliveryCostCalculatorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Api(value="DeliveryCostCalculatorResource", description="Rest API for Cost Calculation")
@RestController
@CrossOrigin
@RequestMapping(value = "/rest/deliverycost")
public class DeliveryCostCalculatorResource {
	
	@Autowired
	@Qualifier("DeliveryCostCalculatorService")
	private DeliveryCostCalculatorService service;

	@ApiOperation(value = "Calculate the Cost of Delivery Item Based on Weight, Height, Width, Length", response = Double.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK, either a valid cost computation or Rejected"),
			@ApiResponse(code = 400, message = "Validation Exception"),
			@ApiResponse(code = 500, message = "Rest Client Exception from voucher API")
			
	})
	@PostMapping(value="/calculate")
	public Double calculateCost(@RequestBody final Delivery delivery) throws RestClientException, ValidationException, DeliveryRejectedException {
		
		return service.calculateDeliveryCost(delivery, Boolean.FALSE);
		
	}
	
}
