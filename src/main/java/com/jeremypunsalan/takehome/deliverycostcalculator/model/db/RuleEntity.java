package com.jeremypunsalan.takehome.deliverycostcalculator.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="rules")
public class RuleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ruleid")
	private Integer ruleId;
	
	@Column(name = "rulename")
	private String ruleName;
	
	@Column(name = "ruledescription")
	private String ruleDescription;
	
	@Column(name = "rulepriority")
	private Integer rulePriority;
	
	@Column(name = "rulecondition")
	private String ruleCondition;
	
	@Column(name = "ruleaction")
	private String ruleAction;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdate", updatable=false)
	private Date createDate;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedate")
	private Date updateDate;

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleDescription() {
		return ruleDescription;
	}

	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}

	public Integer getRulePriority() {
		return rulePriority;
	}

	public void setRulePriority(Integer rulePriority) {
		this.rulePriority = rulePriority;
	}

	public String getRuleCondition() {
		return ruleCondition;
	}

	public void setRuleCondition(String ruleCondition) {
		this.ruleCondition = ruleCondition;
	}

	public String getRuleAction() {
		return ruleAction;
	}

	public void setRuleAction(String ruleAction) {
		this.ruleAction = ruleAction;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "RuleEntity [ruleId=" + ruleId + ", ruleName=" + ruleName + ", ruleDescription=" + ruleDescription
				+ ", rulePriority=" + rulePriority + ", ruleCondition=" + ruleCondition + ", ruleAction=" + ruleAction
				+ ", createDate=" + createDate + ", updateDate=" + updateDate + "]";
	}

	
}
