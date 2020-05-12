package com.jeremypunsalan.takehome.deliverycostcalculator.service;


public interface RulesEngineService {
	
	Object executeRuleEngine(String name, Object object, Boolean isStatic);

}
