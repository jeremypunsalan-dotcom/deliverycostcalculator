package com.jeremypunsalan.takehome.deliverycostcalculator.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jeremypunsalan.takehome.deliverycostcalculator.config.PropertiesLoader;
import com.jeremypunsalan.takehome.deliverycostcalculator.constants.Constants;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.DeliveryEntityConversionException;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.db.RuleEntity;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.DeliveryRules;


public class RuleEntityToDeliveryRulesConverter {
	
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
	
	public RuleEntityToDeliveryRulesConverter(RuleEntity entity) {
		this.deliveryRules = new DeliveryRules();
		this.deliveryRulesList = null;
		this.ruleEntity = entity;
		this.ruleEntityList = null;
		
	}
	
	public RuleEntityToDeliveryRulesConverter(List<RuleEntity> list) {
		this.deliveryRulesList = new ArrayList<DeliveryRules>();
		this.deliveryRules = null;
		this.ruleEntityList = list;
		this.ruleEntity = null;
		
	}
	
	public RuleEntityToDeliveryRulesConverter convertEntity() throws DeliveryEntityConversionException {
		
		if(this.deliveryRules == null || this.ruleEntity == null) 
			throw new DeliveryEntityConversionException(Constants.CONVERT_ENTITY_ERROR_MSG);
		
		this.deliveryRules = convert(this.ruleEntity);
		
		return this;
		
	}
	
	public RuleEntityToDeliveryRulesConverter convertList() throws DeliveryEntityConversionException {
		
		if(this.deliveryRulesList == null || this.ruleEntityList == null) 
			throw new DeliveryEntityConversionException(Constants.CONVERT_LIST_ERROR_MSG);
		
		for(RuleEntity entity: this.ruleEntityList) {
			this.deliveryRulesList.add(convert(entity));
		}
		
		return this;
	}
	
	public DeliveryRules buildEntity() {
		return this.deliveryRules;
	}
	
	public List<DeliveryRules> buildList() {
		return this.deliveryRulesList;
	}
	
	
	
	private DeliveryRules convert(RuleEntity entity) throws DeliveryEntityConversionException {
		
		DeliveryRules deliveryRules = new DeliveryRules();
		
		deliveryRules.setRuleId(entity.getRuleId());
		deliveryRules.setRuleName(entity.getRuleName());
		deliveryRules.setPriority(entity.getRulePriority());
		
		String condition = entity.getRuleCondition();
		String domainPeriod = deliveryDomainName + Constants.PERIOD;
		if(condition.indexOf(domainPeriod) != -1) {
			condition = condition.replaceAll(domainPeriod, Constants.BLANK).toUpperCase();
		} else if(Boolean.TRUE.toString().equalsIgnoreCase(condition)) {
			condition = Constants.NASTRING;
		} else {
			throw new DeliveryEntityConversionException(Constants.CONDITION_INCORRECT_FORMAT);
		}
		
		deliveryRules.setConditionExpression(condition);
		
		String action = entity.getRuleAction();
		
		String domainPeriodCost = domainPeriod + Constants.COST + Constants.SPACE + Constants.EQUALS + Constants.SPACE;
		if(action.indexOf(domainPeriodCost) != -1 && action.indexOf(Constants.SEMI_COLON) != -1) {
			action = action.replace(domainPeriodCost, Constants.BLANK).replaceAll(domainPeriod, Constants.BLANK).replace(Constants.SEMI_COLON, Constants.BLANK).toUpperCase();
			if(Constants.NULLSTRING.equalsIgnoreCase(action)) action = Constants.NASTRING;
		} else {
			throw new DeliveryEntityConversionException(Constants.ACTION_INCORRECT_FORMAT);
		}
		deliveryRules.setVariableCostExpression(action);
		
		return deliveryRules;
		
	}

}
