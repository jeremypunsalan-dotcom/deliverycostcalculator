package com.jeremypunsalan.takehome.deliverycostcalculator.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeremypunsalan.takehome.deliverycostcalculator.builder.DeliveryRulesToRuleEntityConverter;
import com.jeremypunsalan.takehome.deliverycostcalculator.builder.RuleEntityToDeliveryRulesConverter;
import com.jeremypunsalan.takehome.deliverycostcalculator.constants.Constants;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.DeliveryEntityConversionException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ResourceNotFoundException;
import com.jeremypunsalan.takehome.deliverycostcalculator.exception.ValidationException;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.db.RuleEntity;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.DeliveryRules;
import com.jeremypunsalan.takehome.deliverycostcalculator.model.view.validator.InputValidator;
import com.jeremypunsalan.takehome.deliverycostcalculator.repository.RuleEntityRepository;

@Component("RulesCRUDService")
public class RulesCRUDServiceImpl implements RulesCRUDService {

	private RuleEntityRepository ruleEntityRepository;

	@Autowired
	public void setRuleEntityRepository(RuleEntityRepository repository) {
		this.ruleEntityRepository = repository;
	}

	@Override
	public DeliveryRules save(DeliveryRules deliveryRules) throws ValidationException, DeliveryEntityConversionException, ResourceNotFoundException  {

		Map<Integer, String> errors = InputValidator.validateInput(deliveryRules);
		if(InputValidator.hasErrors(errors)) {
			throw new ValidationException(InputValidator.getErrorMessages(errors)); 
		}
		
		RuleEntity ruleInput = new DeliveryRulesToRuleEntityConverter(deliveryRules).convertEntity().buildEntity();
		List<RuleEntity> rulesList = ruleEntityRepository.findAllByOrderByRulePriorityAsc();
		System.out.println(ruleInput);
		System.out.println(rulesList);
		if (!Optional.ofNullable(ruleInput.getRuleId()).isPresent()) {
			rulesList = this.updateRuleList(ruleInput, rulesList);
			ruleInput = ruleEntityRepository.save(ruleInput);
		} else if (rulesList.stream().anyMatch(entity -> entity.getRuleId().equals(deliveryRules.getRuleId()))) {
			rulesList = this.removeRuleFromList(ruleInput, rulesList);
			rulesList = this.insertRuleToList(ruleInput, rulesList);
		} else {
			throw new ResourceNotFoundException(Constants.RULE_ID_NOT_FOUND);
		}

		List<RuleEntity> savedRulesList = ruleEntityRepository.saveAll(rulesList);
		System.out.println(ruleInput);
		System.out.println(savedRulesList);
		return new RuleEntityToDeliveryRulesConverter(ruleInput).convertEntity().buildEntity();
	
	}
	
	@Override
	public DeliveryRules fetchDeliveryRulesById(Integer deliveryRuleId) throws ResourceNotFoundException, DeliveryEntityConversionException  {
		RuleEntity entity = ruleEntityRepository.findById(deliveryRuleId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.RULE_ID_NOT_FOUND) );
		return new RuleEntityToDeliveryRulesConverter(entity)
				.convertEntity()
				.buildEntity();
	}
	
	@Override
	public List<DeliveryRules> fetchAllDeliveryRules() throws DeliveryEntityConversionException, ResourceNotFoundException {
		
		List<RuleEntity> list = ruleEntityRepository.findAllByOrderByRulePriorityAsc();
		List<DeliveryRules> deliveryRulesList = null;
		if(Optional.ofNullable(list).isPresent()) {
			deliveryRulesList = new RuleEntityToDeliveryRulesConverter(list)
					.convertList()
					.buildList();
			
		} else {
			throw new ResourceNotFoundException(Constants.NO_RECORDS_FOUND);
		}
		
		return deliveryRulesList;
	}
	
	@Override
	public DeliveryRules delete(Integer deliveryRuleId) throws ResourceNotFoundException, DeliveryEntityConversionException {
		RuleEntity entity = ruleEntityRepository.findById(deliveryRuleId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.RULE_ID_NOT_FOUND));
		List<RuleEntity> rulesList = this.removeRuleFromList(entity, ruleEntityRepository.findAllByOrderByRulePriorityAsc());
		ruleEntityRepository.delete(entity);
		ruleEntityRepository.saveAll(rulesList);
		return new RuleEntityToDeliveryRulesConverter(entity).convertEntity().buildEntity();
	}

	private List<RuleEntity> insertRuleToList(RuleEntity ruleInput, List<RuleEntity> rulesList) {

		Integer leastPriorityValue = rulesList.stream().max(Comparator.comparing(RuleEntity::getRulePriority)).get()
				.getRulePriority();

		if (ruleInput.getRulePriority() > leastPriorityValue) {
			ruleInput.setRulePriority(leastPriorityValue + 1);
			rulesList.add(ruleInput);
			
		} else {

			Queue<RuleEntity> queue = new LinkedList<RuleEntity>(rulesList);
			rulesList = new ArrayList<RuleEntity>();
			boolean isIncrement = false;
			while (!queue.isEmpty()) {
				RuleEntity ruleEntity = queue.poll();
				if (!isIncrement && ruleInput.getRulePriority() <= ruleEntity.getRulePriority()) {
					rulesList.add(ruleInput);
					isIncrement = true;
				}
				if (isIncrement) {
					ruleEntity.setRulePriority(ruleEntity.getRulePriority() + 1);
				}
				rulesList.add(ruleEntity);

			}

		}

		return rulesList;

	}

	private List<RuleEntity> updateRuleList(RuleEntity ruleInput, List<RuleEntity> rulesList) {
		Integer leastPriorityValue = rulesList.stream().max(Comparator.comparing(RuleEntity::getRulePriority)).get()
				.getRulePriority();

		if (ruleInput.getRulePriority() > leastPriorityValue) {
			ruleInput.setRulePriority(leastPriorityValue + 1);
		} else {
			ListIterator<RuleEntity> iterator = rulesList.listIterator();
			while (iterator.hasNext()) {
				RuleEntity ruleEntity = iterator.next();
				if (ruleInput.getRulePriority() <= ruleEntity.getRulePriority()) {
					ruleEntity.setRulePriority(ruleEntity.getRulePriority() + 1);
					iterator.set(ruleEntity);
				}
			}
		}

		return rulesList;

	}

	private List<RuleEntity> removeRuleFromList(RuleEntity ruleInput, List<RuleEntity> rulesList) {

		Queue<RuleEntity> queue = new LinkedList<RuleEntity>(rulesList);
		rulesList = new ArrayList<RuleEntity>();

		boolean isDecrement = false;
		while (!queue.isEmpty()) {
			RuleEntity ruleEntity = queue.poll();
			if (!isDecrement && ruleInput.getRuleId().equals(ruleEntity.getRuleId())) {
				isDecrement = true;
				continue;
			}
			if (isDecrement) {
				ruleEntity.setRulePriority(ruleEntity.getRulePriority() - 1);
			}
			rulesList.add(ruleEntity);
		}

		return rulesList;

	}



}
