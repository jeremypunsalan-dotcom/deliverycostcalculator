package com.jeremypunsalan.takehome.deliverycostcalculator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeremypunsalan.takehome.deliverycostcalculator.model.db.RuleEntity;

public interface RuleEntityRepository extends JpaRepository<RuleEntity, Integer> {

	List<RuleEntity> findAllByOrderByRulePriorityAsc();
	
}
