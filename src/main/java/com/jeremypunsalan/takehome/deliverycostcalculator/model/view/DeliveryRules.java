package com.jeremypunsalan.takehome.deliverycostcalculator.model.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Delivery rules and conditions for applying the cost.")
public class DeliveryRules {
	
	@ApiModelProperty(value = "ID populated by DB. This is required if need to update an existing rule.", required = false)
	Integer ruleId;
	@ApiModelProperty(value = "Priority of the delivery rules. The value should be positive and non-zero.", required = true)
	Integer priority;
	@ApiModelProperty(value = "Rule name.", required = true)
	String ruleName;
	@ApiModelProperty(value = "Delivery rules condition. The format should be VARIABLE|NUMBER >\\|<\\|>=\\|<=\\|==\\|!= VARIABLE|NUMBER where VARIABLE is any of the following: (WEIGHT, HEIGHT, LENGTH, WIDTH, VOLUME). If no conditions it should be N/A.", required = true, example = "WEIGHT > 50")
	String conditionExpression;
	@ApiModelProperty(value = "Delivery rules cost formula. The format should be VARIABLE|NUMBER +\\|-\\|/\\|* VARIABLE|NUMBER where VARIABLE is any of the following: (WEIGHT, HEIGHT, LENGTH, WIDTH, VOLUME). If the rule is for REJECTION it should be N/A.", required = true, example = "WEIGHT * 20")
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
