package com.jeremypunsalan.takehome.deliverycostcalculator.builder;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.mvel.MVELRule;
import org.jeasy.rules.support.ActivationRuleGroup;


public class RulesBuilder {
	
	private final Rules rules;
	private final ActivationRuleGroup activationRuleGroup;

	public RulesBuilder(String groupName, String groupDesc) {
		rules = new Rules();
		activationRuleGroup = new ActivationRuleGroup(groupName, groupDesc);
	}
	
	public RulesBuilder addRule(String name, String description, Integer priority, String condition, String action) {
		Rule rule = new MVELRule()
				.name(name)
				.description(description)
				.priority(priority)
				.when(condition)
				.then(action);
		activationRuleGroup.addRule(rule);
		return this;
	}
	
	public Rules build() {
		rules.register(activationRuleGroup);
		return rules;
	}

}
