package com.jeremypunsalan.takehome.deliverycostcalculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;

import com.jeremypunsalan.takehome.deliverycostcalculator.config.PropertiesLoader;
import com.jeremypunsalan.takehome.deliverycostcalculator.constants.Constants;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.Delivery;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.DeliveryInput;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.DeliveryRules;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.validator.InputValidator;
import com.jeremypunsalan.takehome.deliverycostcalculator.service.DeliveryCostCalculatorServiceImpl;
import com.jeremypunsalan.takehome.deliverycostcalculator.service.RulesEngineServiceImpl;

@SpringBootTest
class DeliveryCostCalculatorApplicationTests {

	DeliveryCostCalculatorServiceImpl service = new DeliveryCostCalculatorServiceImpl();

	@BeforeEach
	void setup() {

		try {
			service.setRuleService(new RulesEngineServiceImpl());
			Properties property = PropertiesLoader.loadProperties(Constants.APPLICATION_PROPERTY_FILE);
			String domainName = property.getProperty(Constants.APP_PROPERTY_DELIVERY_DOMAIN);
			String voucherUri = property.getProperty("voucher.uri");
			String voucherKey = property.getProperty("voucher.defaultkey");
			service.setDeliveryDomainName(domainName);
			service.setVoucherUri(voucherUri);
			service.setDefaultVoucherUriApiKey(voucherKey);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	void testCalculateDeliveryCostService() throws Exception {

		final DeliveryInput deliveryFirstCase = new DeliveryInput();
		deliveryFirstCase.setHeight((double) 1);
		deliveryFirstCase.setLength((double) 1);
		deliveryFirstCase.setWidth((double) 1);
		deliveryFirstCase.setWeight((double) 55);

		// condition 1: greater than 50 -- expects exception
		Exception exception = assertThrows(Exception.class, () -> {
			service.calculateDeliveryCost(deliveryFirstCase, Boolean.TRUE);
		});
		assertTrue(exception.getMessage().contains(Constants.REJECTED_COST));

		// condition 2: greater than 10
		DeliveryInput delivery = new DeliveryInput();
		delivery.setWeight((double) 20);
		delivery.setHeight((double) 1);
		delivery.setLength((double) 1);
		delivery.setWidth((double) 1);
		assertEquals(new Double(400), service.calculateDeliveryCost(delivery, Boolean.TRUE));

		// condition 3: less than 1500 cm3
		delivery = new DeliveryInput();
		delivery.setWeight((double) 10);
		delivery.setHeight((double) 10);
		delivery.setLength((double) 10);
		delivery.setWidth((double) 10);
		assertEquals(new Double(30), service.calculateDeliveryCost(delivery, Boolean.TRUE));

		// condition 4: less than 2500 cm3
		delivery = new DeliveryInput();
		delivery.setWeight((double) 10);
		delivery.setHeight((double) 12);
		delivery.setLength((double) 12);
		delivery.setWidth((double) 12);
		assertEquals(new Double(69.12), service.calculateDeliveryCost(delivery, Boolean.TRUE));

		// condition 5: large parcel
		delivery = new DeliveryInput();
		delivery.setWeight((double) 10);
		delivery.setHeight((double) 15);
		delivery.setLength((double) 15);
		delivery.setWidth((double) 15);
		assertEquals(new Double(168.75), service.calculateDeliveryCost(delivery, Boolean.TRUE));

		// fetch discount from third party api and calculate cost - discount
		delivery.setVoucherCode("MYNT");
		assertEquals(new Double(156.5), service.calculateDeliveryCost(delivery, Boolean.TRUE));

	}

	@Test
	void testFetchDiscountFromVoucherService() throws Exception {

		// normal case with key
		Delivery delivery = new Delivery();
		delivery.setVoucherCode("GFI");
		delivery.setKey("apikey");
		assertEquals(new Double(7.5), service.fetchDiscountFromVoucher(delivery));

		// normal case without key
		delivery = new Delivery();
		delivery.setVoucherCode("MYNT");
		assertEquals(new Double(12.25), service.fetchDiscountFromVoucher(delivery));

		// incorrect voucher code
		final Delivery deliveryIncorrectCode = new Delivery();
		deliveryIncorrectCode.setVoucherCode("skdlks");
		assertThrows(RestClientException.class, () -> {
			service.fetchDiscountFromVoucher(deliveryIncorrectCode);
		});

		// incorrect api key
		final Delivery deliveryIncorrectKey = new Delivery();
		deliveryIncorrectKey.setVoucherCode("MYNT");
		deliveryIncorrectKey.setKey("dawdawdwad");
		assertThrows(RestClientException.class, () -> {
			service.fetchDiscountFromVoucher(deliveryIncorrectKey);
		});

		// no voucher code
		// null value
		final Delivery deliveryNullVoucherCode = new Delivery();
		Exception exception1 = assertThrows(Exception.class, () -> {
			service.fetchDiscountFromVoucher(deliveryNullVoucherCode);
		});
		assertTrue(exception1.getMessage().contains(Constants.VOUCHER_CODE_EMPTY));

		// empty value
		final Delivery deliveryBlankVoucherCode = new Delivery();
		deliveryBlankVoucherCode.setVoucherCode(Constants.BLANK);
		Exception exception2 = assertThrows(Exception.class, () -> {
			service.fetchDiscountFromVoucher(deliveryBlankVoucherCode);
		});
		assertTrue(exception2.getMessage().contains(Constants.VOUCHER_CODE_EMPTY));

	}

	@Test
	void testInputValidatorDelivery() {

		// Input for calculating costs
		DeliveryInput delivery = new DeliveryInput();

		// no input at all
		assertThrows(Exception.class, () -> {
			service.calculateDeliveryCost(delivery, Boolean.TRUE);
		});

		// some part only -- leaving weight null
		delivery.setHeight(new Double(1));
		delivery.setLength(new Double(1));
		delivery.setWidth(new Double(1));
		Exception ex1 = assertThrows(Exception.class, () -> {
			service.calculateDeliveryCost(delivery, Boolean.TRUE);
		});
		assertTrue(ex1.getMessage().contains("WEIGHT"));

	}

	@Test
	void testInputValidatorDeliveryRules() {

		// Input for rules CRUD
		DeliveryRules rules = new DeliveryRules();

		// no input at all - expected that errormap has 4 required error messages
		Map<Integer, String> errorMap1 = InputValidator.validateInput(rules);
		assertTrue(InputValidator.hasErrors(errorMap1));
		assertTrue(errorMap1.size() == 4);

		// only 1 field is missing
		rules.setConditionExpression("WEIGHT > 50");
		rules.setPriority(1);
		rules.setRuleName("Reject");
		Map<Integer, String> errorMap2 = InputValidator.validateInput(rules);
		assertTrue(InputValidator.hasErrors(errorMap2));
		assertTrue(errorMap2.size() == 1);
		assertTrue(errorMap2.get(1).contains("VARIABLECOSTEXPRESSION"));

		// if priority is less than 1
		rules = new DeliveryRules();
		rules.setConditionExpression("WEIGHT > 50");
		rules.setPriority(0);
		rules.setRuleName("Reject");
		rules.setVariableCostExpression("N/A");
		Map<Integer, String> errorMap3 = InputValidator.validateInput(rules);
		assertTrue(InputValidator.hasErrors(errorMap3));
		assertTrue(errorMap3.size() == 1);
		assertTrue(errorMap3.get(1).contains("PRIORITY"));

		// if condition is not in correct format
		rules = new DeliveryRules();
		rules.setConditionExpression("sadasda34dfadadwadwa asddasdsadwadwa");
		rules.setPriority(1);
		rules.setRuleName("Reject");
		rules.setVariableCostExpression("N/A");
		Map<Integer, String> errorMap4 = InputValidator.validateInput(rules);
		assertTrue(InputValidator.hasErrors(errorMap4));
		assertTrue(errorMap4.size() == 2);
		assertTrue(errorMap4.get(1).contains(Constants.CONDITION_INCORRECT_FORMAT));

		// if variablecostexpression is not in correct format
		rules = new DeliveryRules();
		rules.setConditionExpression("N/A");
		rules.setPriority(1);
		rules.setRuleName("Reject");
		rules.setVariableCostExpression("xDxasd32sdsadsa das34sazdsadsa 12");
		Map<Integer, String> errorMap5 = InputValidator.validateInput(rules);
		assertTrue(InputValidator.hasErrors(errorMap5));
		assertTrue(errorMap5.size() == 2);
		assertTrue(errorMap5.get(1).contains(Constants.ACTION_INCORRECT_FORMAT));

	}

}
