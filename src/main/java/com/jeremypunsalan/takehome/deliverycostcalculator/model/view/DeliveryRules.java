package com.jeremypunsalan.takehome.deliverycostcalculator.model.view;

public class DeliveryRules {
	
	Integer ruleId;
	Integer priority;
	String ruleName;
	String conditionExpression;
	String variableCostExpression;
	
	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getConditionExpression() {
		return conditionExpression;
	}
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	public String getVariableCostExpression() {
		return variableCostExpression;
	}
	public void setVariableCostExpression(String variableCostExpression) {
		this.variableCostExpression = variableCostExpression;
	}
	@Override
	public String toString() {
		return "DeliveryRules [ruleId=" + ruleId + ", priority=" + priority + ", ruleName=" + ruleName
				+ ", conditionExpression=" + conditionExpression + ", variableCostExpression=" + variableCostExpression
				+ "]";
	}
	

}
