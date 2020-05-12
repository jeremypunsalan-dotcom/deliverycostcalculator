package com.jeremypunsalan.takehome.deliverycostcalculator.model.view.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Optional;

import com.jeremypunsalan.takehome.deliverycostcalculator.constants.Constants;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.Delivery;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.DeliveryRules;

public class InputValidator {

	public static Map<Integer, String> validateInput(Delivery delivery) {

		Integer index = 1;
		Map<Integer, String> errorMap = new HashMap<Integer, String>();

		// check required fields
		if (!Optional.ofNullable(delivery.getHeight()).isPresent()) {
			errorMap.put(index++, Constants.REQUIRED_FIELD_MSG + DeliveryMeasurement.HEIGHT.toString());
		}
		if (!Optional.ofNullable(delivery.getLength()).isPresent()) {
			errorMap.put(index++, Constants.REQUIRED_FIELD_MSG + DeliveryMeasurement.LENGTH.toString());
		}
		if (!Optional.ofNullable(delivery.getWidth()).isPresent()) {
			errorMap.put(index++, Constants.REQUIRED_FIELD_MSG + DeliveryMeasurement.WIDTH.toString());
		}
		if (!Optional.ofNullable(delivery.getWeight()).isPresent()) {
			errorMap.put(index++, Constants.REQUIRED_FIELD_MSG + DeliveryMeasurement.WEIGHT.toString());
		}

		// check if measurements are positive
		if (Optional.ofNullable(delivery.getHeight()).isPresent() && delivery.getHeight().compareTo(0.0) < 0) {
			errorMap.put(index++, Constants.INPUT_NEGATIVE + DeliveryMeasurement.HEIGHT.toString());
		}

		if (Optional.ofNullable(delivery.getLength()).isPresent() && delivery.getLength().compareTo(0.0) < 0) {
			errorMap.put(index++, Constants.INPUT_NEGATIVE + DeliveryMeasurement.LENGTH.toString());
		}

		if (Optional.ofNullable(delivery.getWidth()).isPresent() && delivery.getWidth().compareTo(0.0) < 0) {
			errorMap.put(index++, Constants.INPUT_NEGATIVE + DeliveryMeasurement.WIDTH.toString());
		}

		if (Optional.ofNullable(delivery.getWeight()).isPresent() && delivery.getWeight().compareTo(0.0) < 0) {
			errorMap.put(index++, Constants.INPUT_NEGATIVE + DeliveryMeasurement.WEIGHT.toString());
		}

		return errorMap;

	}

	public static Map<Integer, String> validateInput(DeliveryRules deliveryRules) {

		Integer index = 1;
		Map<Integer, String> errorMap = new HashMap<Integer, String>();

		// check required fields
		if (!Optional.ofNullable(deliveryRules.getRuleName()).isPresent()) {
			errorMap.put(index++, Constants.REQUIRED_FIELD_MSG + DeliveryRulesRequiredFields.RULENAME.toString());
		}
		if (!Optional.ofNullable(deliveryRules.getPriority()).isPresent()) {
			errorMap.put(index++, Constants.REQUIRED_FIELD_MSG + DeliveryRulesRequiredFields.PRIORITY.toString());
		}
		if (!Optional.ofNullable(deliveryRules.getConditionExpression()).isPresent()) {
			errorMap.put(index++,
					Constants.REQUIRED_FIELD_MSG + DeliveryRulesRequiredFields.CONDITIONEXPRESSION.toString());
		}
		if (!Optional.ofNullable(deliveryRules.getVariableCostExpression()).isPresent()) {
			errorMap.put(index++,
					Constants.REQUIRED_FIELD_MSG + DeliveryRulesRequiredFields.VARIABLECOSTEXPRESSION.toString());
		}

		// check priority is greater than zero
		if (Optional.ofNullable(deliveryRules.getPriority()).isPresent() && deliveryRules.getPriority() < 1) {
			errorMap.put(index++,
					Constants.PRIORITY_GREATER_THAN_ZERO + DeliveryRulesRequiredFields.PRIORITY.toString());
		}

		// check condition
		if (Optional.ofNullable(deliveryRules.getConditionExpression()).isPresent()) {
			if (!deliveryRules.getConditionExpression().contains(Constants.SPACE)) {
				if(!Constants.NASTRING.equalsIgnoreCase(deliveryRules.getConditionExpression())) {
					errorMap.put(index++, Constants.CONDITION_INCORRECT_FORMAT);
				}
			
			} else {
				for (String condition : deliveryRules.getConditionExpression().split(Constants.SPACE)) {
					if (isValidDeliveryParameter(condition) || isValidConditionOperator(condition)
							|| isValidNumber(condition))
						continue;
					else {
						errorMap.put(index++, Constants.CONDITION_INCORRECT_FORMAT);
					}

				}
			}
		}

		// check variable expression
		if (Optional.ofNullable(deliveryRules.getVariableCostExpression()).isPresent()) {
			if (!deliveryRules.getVariableCostExpression().contains(Constants.SPACE)) {
				if (!Constants.NASTRING.equalsIgnoreCase(deliveryRules.getVariableCostExpression())
						&& !isValidNumber(deliveryRules.getVariableCostExpression())) {
					errorMap.put(index++, Constants.ACTION_INCORRECT_FORMAT);

				}
			} else {
				for (String action : deliveryRules.getVariableCostExpression().split(Constants.SPACE)) {
					if (isValidDeliveryParameter(action) || isValidArithmethicOperator(action) || isValidNumber(action))
						continue;
					else {
						errorMap.put(index++, Constants.ACTION_INCORRECT_FORMAT);
					}
				}
			}
		}

		return errorMap;

	}

	public static boolean hasErrors(Map<Integer, String> errorMap) {
		return errorMap.size() > 0;
	}

	public static String getErrorMessages(Map<Integer, String> errorMap) {
		List<String> errors = new ArrayList<String>();
		for (Entry<Integer, String> error : errorMap.entrySet()) {
			errors.add(error.getValue());
		}
		return errors.stream().collect(Collectors.joining(Constants.SEMI_COLON + Constants.SPACE));
	}

	public static boolean isValidNumber(String subString) {
		return subString.matches(Constants.REGEX_VALID_NUM);
	}

	public static boolean isValidArithmethicOperator(String subString) {
		return subString.matches(Constants.REGEX_VALID_ARITHMETIC_OPERATOR);
	}

	public static boolean isValidConditionOperator(String subString) {
		return subString.matches(Constants.REGEX_VALID_CONDITIONAL_OPERATOR);
	}

	public static boolean isValidDeliveryParameter(String subString) {
		return Arrays.stream(DeliveryMeasurement.values())
				.anyMatch(measurement -> measurement.toString().equalsIgnoreCase(subString));
	}

}

enum DeliveryMeasurement {
	WEIGHT, HEIGHT, WIDTH, LENGTH, VOLUME
}

enum DeliveryRulesRequiredFields {
	RULENAME, PRIORITY, CONDITIONEXPRESSION, VARIABLECOSTEXPRESSION
}
