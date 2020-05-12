package com.jeremypunsalan.takehome.deliverycostcalculator.constants;

public interface Constants {
	
	static final String SPACE = " ";
	static final String BLANK = "";
	static final String SEMI_COLON = ";";
	static final String COLON = ":";
	static final String PERIOD = ".";
	static final String NEWLINE = "\n";
	static final String EQUALS = "=";
	static final String NULLSTRING = "null";
	static final String NASTRING = "N/A";
	static final String KEY = "key";
	static final String REGEX_VALID_NUM = "-?\\d+(\\.\\d+)?";
	static final String REGEX_VALID_ARITHMETIC_OPERATOR = "[+/*-]";
	static final String REGEX_VALID_CONDITIONAL_OPERATOR = "<|>|>=|<=|==|!=";
	
	static final String REQUIRED_FIELD_MSG = "Required field missing: ";
	static final String INPUT_NEGATIVE = "Input negative: ";
	static final String PRIORITY_GREATER_THAN_ZERO = "Priority should be greater than zero only: ";
	static final String CONDITION_INCORRECT_FORMAT = "Incorrect condition format";
	static final String ACTION_INCORRECT_FORMAT = "Incorrect variable cost expression format";
	static final String CONVERT_ENTITY_ERROR_MSG = "Incorrect converter method used: please use convertList() instead.";
	static final String CONVERT_LIST_ERROR_MSG = "Incorrect converter method used: please use convertEntity() instead.";
	static final String RULE_ID_NOT_FOUND = "Rule ID not found";
	static final String NO_RECORDS_FOUND = "No records found";
	static final String REJECTED_COST = "Cannot compute cost: condition rejected and N/A";
	static final String VOUCHER_CODE_EMPTY = "No voucher code specified";
	
	
	static final String RULE_GROUP_NAME = "RULE_GROUP";
	static final String RULE_GROUP_DESC = "RULE_DESC";
	
	
	static final String COST = "cost";
	
	static final String APPLICATION_PROPERTY_FILE = "application.properties";
	static final String APP_PROPERTY_DELIVERY_DOMAIN = "delivery.domain";

	
	

}
