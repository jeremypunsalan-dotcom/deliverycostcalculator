package com.jeremypunsalan.takehome.deliverycostcalculator.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.jeremypunsalan.takehome.deliverycostcalculator.config.PropertiesLoader;
import com.jeremypunsalan.takehome.deliverycostcalculator.constants.Constants;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.DeliveryEntityConversionException;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.db.RuleEntity;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.DeliveryRules;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.validator.InputValidator;

public class DeliveryRulesToRuleEntityConverter {

	private DeliveryRules deliveryRules;
	private List<DeliveryRules> deliveryRulesList;
	private RuleEntity ruleEntity;
	private List<RuleEntity> ruleEntityList;
	private static String deliveryDomainName;

	static {

		try {
			Properties property = PropertiesLoader.loadProperties(Constants.APPLICATION_PROPERTY_FILE);
			deliveryDomainName = property.getProperty(Constants.APP_PROPERTY_DELIVERY_DOMAIN);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public DeliveryRulesToRuleEntityConverter(DeliveryRules deliveryRules) {
		this.deliveryRules = deliveryRules;
		this.deliveryRulesList = null;
		this.ruleEntity = new RuleEntity();
		this.ruleEntityList = null;
	}

	public DeliveryRulesToRuleEntityConverter(List<DeliveryRules> list) {
		this.deliveryRulesList = list;
		this.deliveryRules = null;
		this.ruleEntityList = new ArrayList<RuleEntity>();
		this.ruleEntity = null;

	}

	public DeliveryRulesToRuleEntityConverter convertEntity() throws DeliveryEntityConversionException {

		if (this.deliveryRules == null || this.ruleEntity == null)
			throw new DeliveryEntityConversionException(Constants.CONVERT_ENTITY_ERROR_MSG);

		this.ruleEntity = convert(this.deliveryRules);

		return this;

	}

	public DeliveryRulesToRuleEntityConverter convertList() throws DeliveryEntityConversionException {

		if (this.deliveryRulesList == null || this.ruleEntityList == null)
			throw new DeliveryEntityConversionException(Constants.CONVERT_LIST_ERROR_MSG);

		for (DeliveryRules deliveryRules : this.deliveryRulesList) {
			this.ruleEntityList.add(convert(deliveryRules));
		}

		return this;
	}

	public RuleEntity buildEntity() {
		return this.ruleEntity;
	}

	public List<RuleEntity> buildList() {
		return this.ruleEntityList;
	}

	private RuleEntity convert(DeliveryRules deliveryRules) throws DeliveryEntityConversionException {

		RuleEntity entity = new RuleEntity();

		entity.setRuleId(deliveryRules.getRuleId());
		entity.setRuleName(deliveryRules.getRuleName());
		entity.setRulePriority(deliveryRules.getPriority());

		List<String> conditions = new ArrayList<String>();
		if (!deliveryRules.getConditionExpression().contains(Constants.SPACE)
				&& Constants.NASTRING.equalsIgnoreCase(deliveryRules.getConditionExpression())) {
			conditions.add(Boolean.TRUE.toString());
		} else {
			for (String subString : deliveryRules.getConditionExpression().split(Constants.SPACE)) {
				if (InputValidator.isValidDeliveryParameter(subString)) {
					subString = deliveryDomainName + Constants.PERIOD + subString;
				}
				conditions.add(subString.toLowerCase());
			}
		}

		entity.setRuleCondition(conditions.stream().collect(Collectors.joining(Constants.SPACE)));

		List<String> actions = new ArrayList<String>();
		actions.add(deliveryDomainName + Constants.PERIOD + Constants.COST + Constants.SPACE + Constants.EQUALS);

		if (!deliveryRules.getVariableCostExpression().contains(Constants.SPACE)) {
			if(Constants.NASTRING.equalsIgnoreCase(deliveryRules.getVariableCostExpression())) {
				actions.add(Constants.NULLSTRING);
			} else {
				actions.add(deliveryRules.getVariableCostExpression().toLowerCase());
			}
		} else {
			for (String subString : deliveryRules.getVariableCostExpression().split(Constants.SPACE)) {
				if (InputValidator.isValidDeliveryParameter(subString)) {
					subString = deliveryDomainName + Constants.PERIOD + subString.toLowerCase();
				} 
				actions.add(subString.toLowerCase());
			}
		}

		entity.setRuleAction(actions.stream().collect(Collectors.joining(Constants.SPACE)) + Constants.SEMI_COLON);
		entity.setRuleDescription(deliveryRules.getRuleName());

		return entity;

	}

}
