package com.jeremypunsalan.takehome.deliverycostcalculator.service;

import java.util.List;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeremypunsalan.takehome.deliverycostcalculator.builder.RulesBuilder;
import com.jeremypunsalan.takehome.deliverycostcalculator.constants.Constants;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.db.RuleEntity;
import com.jeremypunsalan.takehome.deliverycostcalculator.repository.RuleEntityRepository;

@Component("RulesEngineService")
public class RulesEngineServiceImpl implements RulesEngineService {
	

	
	private RuleEntityRepository ruleEntityRepository;
	
	@Autowired
	public void setRuleEntityRepository(RuleEntityRepository repository) {
		this.ruleEntityRepository = repository;
	}
	
	
	@Override
	public Object executeRuleEngine(String name, Object object, Boolean isStatic) {

		Facts fact = new Facts();
		fact.put(name, object);
		Rules rules = null; 
		if(isStatic) rules = createStaticRules();
		else rules = getRulesFromStore();
		RulesEngine engine = new DefaultRulesEngine();
		engine.fire(rules, fact);
		
		return fact.get(name);
	}

	private Rules getRulesFromStore() {
		
		List<RuleEntity> rulesList = ruleEntityRepository.findAllByOrderByRulePriorityAsc();
		
		RulesBuilder builder = new RulesBuilder(Constants.RULE_GROUP_NAME, Constants.RULE_GROUP_DESC);
		
		for(RuleEntity entity: rulesList) {
			builder.addRule(String.valueOf(entity.getRuleId()) + Constants.COLON + entity.getRuleName(), 
					entity.getRuleDescription(), 
					entity.getRulePriority(), 
					entity.getRuleCondition(), 
					entity.getRuleAction());
		}
		
		return builder.build();
		
	}

	private Rules createStaticRules() {

		return new RulesBuilder("Delivery Rules", "Delivery Rules")
				.addRule("Reject", "Reject", 1, "delivery.weight > 50", "delivery.cost = null;")
				.addRule("Heavy Parcel", "Heavy Parcel", 2, "delivery.weight > 10",
						"delivery.cost = delivery.weight * 20;")
				.addRule("Small Parcel", "Volume is less than 1500 cm3", 3, "delivery.volume < 1500",
						"delivery.cost = delivery.volume * 0.03;")
				.addRule("Medium Parcel", "Volume is less than 2500 cm3", 4, "delivery.volume < 2500",
						"delivery.cost = delivery.volume * 0.04;")
				.addRule("Large Parcel", "Large Parcel", 5, "true", "delivery.cost = delivery.volume * 0.05;")
				.build();

	}

}
