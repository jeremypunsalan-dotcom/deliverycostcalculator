package com.jeremypunsalan.takehome.deliverycostcalculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.jeremypunsalan.takehome.deliverycostcalculator.constants.Constants;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.DeliveryRejectedException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ValidationException;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.Delivery;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.VoucherItem;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.validator.InputValidator;

@Component("DeliveryCostCalculatorService")
public class DeliveryCostCalculatorServiceImpl implements DeliveryCostCalculatorService {
	
	private String deliveryDomainName;
	
	@Autowired
	public void setDeliveryDomainName(@Value("${delivery.domain}") String name) {
		this.deliveryDomainName = name;
	}
	
	private String voucherUri;
	
	@Autowired
	public void setVoucherUri(@Value("${voucher.uri}") String voucherUri) {
		this.voucherUri = voucherUri;
	}

	private String defaultVoucherUriApiKey;
	
	@Autowired
	public void setDefaultVoucherUriApiKey(@Value("${voucher.defaultkey}") String defaultVoucherUriApiKey) {
		this.defaultVoucherUriApiKey = defaultVoucherUriApiKey;
	}
	
	private String numberDecimalPlaces;
	
	@Autowired
	public void setNumberDecimalPlaces(@Value("${cost.decimal}") String decimalPlaces) {
		this.numberDecimalPlaces = decimalPlaces;
	} 
	
	private RulesEngineService rulesEngineService;
	
	@Autowired
	public void setRuleService(RulesEngineService service) {
		this.rulesEngineService = service;
	}

	@Override
	public BigDecimal calculateDeliveryCost(Delivery delivery, Boolean isStatic) throws ValidationException, RestClientException, DeliveryRejectedException {

		Map<Integer, String> errors = InputValidator.validateInput(delivery);
		if(InputValidator.hasErrors(errors)) {
			throw new ValidationException(InputValidator.getErrorMessages(errors));  
		}
		
		delivery.setVolume(delivery.getHeight() * delivery.getLength() * delivery.getWidth());
		
		return calculateCost(delivery, isStatic);
	}
	
	private BigDecimal calculateCost(Delivery delivery, Boolean isStatic) throws DeliveryRejectedException, ValidationException, RestClientException   {
		
		System.out.println("Input: " + delivery);
		Delivery deliveryFact = (Delivery) rulesEngineService.executeRuleEngine(deliveryDomainName, delivery, isStatic);
		System.out.println("Output: " + deliveryFact);
		
		if(!Optional.ofNullable(deliveryFact.getCost()).isPresent()) 
			throw new DeliveryRejectedException(Constants.REJECTED_COST);
		
		if(Optional.ofNullable(deliveryFact.getVoucherCode()).isPresent()) {
			try {
				Double discount = getDiscountFromVoucherApi(deliveryFact);
				if(Optional.ofNullable(discount).isPresent()) {
					deliveryFact.setCost(deliveryFact.getCost() - discount);
					System.out.println("Output with discount: " + deliveryFact);
				}
			} catch(RestClientException exception) {
				System.out.println(exception.getMessage());
			}
			
		}
		
		BigDecimal cost = new BigDecimal(deliveryFact.getCost())
				.setScale(Integer.parseInt(numberDecimalPlaces), RoundingMode.HALF_EVEN);
		
		return cost;
	}
	
	@Override
	public BigDecimal getDiscountFromVoucherApi(String voucherCode, String key) throws ValidationException, RestClientException {
		
		Delivery delivery = new Delivery();
		delivery.setVoucherCode(voucherCode);
		delivery.setKey(key);
		
		BigDecimal discount = new BigDecimal(getDiscountFromVoucherApi(delivery))
				.setScale(Integer.parseInt(numberDecimalPlaces), RoundingMode.HALF_EVEN);
		
		return discount;
	}
	
	private Double getDiscountFromVoucherApi(Delivery delivery) throws ValidationException, RestClientException  {

		if(!Optional.ofNullable(delivery.getVoucherCode()).isPresent()
				|| Constants.BLANK.equals(delivery.getVoucherCode())) 
			throw new ValidationException(Constants.VOUCHER_CODE_EMPTY);
		
		final String uri = voucherUri + delivery.getVoucherCode();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
		        .queryParam(Constants.KEY, 
		        		Optional.ofNullable(delivery.getKey()).isPresent() 
		        		?  delivery.getKey()
		        				: defaultVoucherUriApiKey);

		RestTemplate restTemplate = new RestTemplateBuilder().build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		try {
			ResponseEntity<VoucherItem> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
					new HttpEntity<>(headers), VoucherItem.class);
			
			if (Optional.ofNullable(responseEntity).isPresent() && responseEntity.getStatusCode().is2xxSuccessful()) {
				VoucherItem response = responseEntity.getBody();
				delivery.setDiscount(new Double(response.getDiscount()));
				return delivery.getDiscount();
			} else {
		        return null;
		    }
		} catch(RestClientException ex) {
			throw ex;
		}
		
		
	}



	

}
