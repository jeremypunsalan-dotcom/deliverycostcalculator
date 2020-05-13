package com.jeremypunsalan.takehome.deliverycostcalculator.resource;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.jeremypunsalan.takehome.deliverycostcalculator.exception.DeliveryRejectedException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ValidationException;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.Delivery;
import com.jeremypunsalan.takehome.deliverycostcalculator.service.DeliveryCostCalculatorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

	@ApiOperation(value = "Calculate the Cost of Delivery Item Based on Weight, Height, Width, Length", response = BigDecimal.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK, either a valid cost computation or Rejected"),
			@ApiResponse(code = 400, message = "Validation Exception"),
			@ApiResponse(code = 500, message = "Rest Client Exception from voucher API")
			
	})
	@PostMapping(value="/calculate")
	public BigDecimal calculateCost(@RequestBody final Delivery delivery) throws RestClientException, ValidationException, DeliveryRejectedException {
		
		return service.calculateDeliveryCost(delivery, Boolean.FALSE);
		
	}
	
	@ApiOperation(value = "Gets the discount from Voucher API given the voucher code and key (optional).", response = BigDecimal.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Validation Exception"),
			@ApiResponse(code = 500, message = "Rest Client Exception from voucher API")
			
	})
	
	@GetMapping(value="/voucher/{voucherCode}")
	public BigDecimal getDiscountFromVoucherAPI(
			@ApiParam(value = "Voucher code to be redeemed", name = "voucherCode", required = true) 
			@PathVariable("voucherCode") 
			final String voucherCode, 
			@ApiParam(value = "API key to identify the application connecting to the API. You may use apikey for the value.", name = "key", required = false) 
			@RequestParam(name = "key", required = false) 
			final String key) throws RestClientException, ValidationException {
		
		return service.getDiscountFromVoucherApi(voucherCode, key);
		
	}
	
}
