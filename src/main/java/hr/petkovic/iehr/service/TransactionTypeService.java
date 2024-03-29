package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.TransactionType;
import hr.petkovic.iehr.repo.TransactionTypeRepo;
import hr.petkovic.iehr.util.TransactionTypeUtil;

@Service
public class TransactionTypeService {
	Logger logger = LoggerFactory.getLogger(SiteService.class);

	private TransactionTypeRepo typeRepo;
	private TransactionTypeUtil tUtil;

	public TransactionTypeService(TransactionTypeRepo transactionTypeRepo, TransactionTypeUtil typeUtil) {
		typeRepo = transactionTypeRepo;
		tUtil = typeUtil;
	}

	public TransactionType getDefaultIncomeType() {
		return typeRepo.findByMainTypeAndSubType("Ulaz", "Lokal").get();
	}

	public TransactionType getDefaultBankIncomeType() {
		return typeRepo.findByMainTypeAndSubType("Ulaz", "Banka").get();
	}

	public TransactionType getDefaultAdminIncomeType() {
		return typeRepo.findByMainTypeAndSubType("Ulaz", "Admin").get();
	}

	public List<TransactionType> getAllExpenseTypes() {
		return typeRepo.findAllByMainType("Izlaz");
	}

	public List<TransactionType> getPrivateExpenseTypes() {
		return typeRepo.findBySubTypeIn(tUtil.getPrivateExpenses());
	}

	public List<TransactionType> getBusinessExpenseTypes() {
		return typeRepo.findBySubTypeIn(tUtil.getBusinessExpenses());
	}

	public List<TransactionType> getOperativeExpenseTypes() {
		List<TransactionType> returnList = typeRepo.findBySubTypeIn(tUtil.getOperativeExpenses());
		TransactionType razduzenje = typeRepo.findByMainTypeAndSubType("Izlaz", "RAZDUZENJE").get();
		Collections.swap(returnList, returnList.indexOf(razduzenje), 0);
		return returnList;
	}
	public List<TransactionType> getRazduzenjeType() {
		List<TransactionType> returnList = new ArrayList<TransactionType>();
		TransactionType razduzenje = typeRepo.findByMainTypeAndSubType("Izlaz", "RAZDUZENJE").get();
		returnList.add(razduzenje);
		return returnList;
	}

	public TransactionType getDefaultPayType() {
		return typeRepo.findByMainTypeAndSubType("Izlaz", "Placa").get();
	}

	public TransactionType getDefaultFixedExpenseType() {
		return typeRepo.findByMainTypeAndSubType("Izlaz", "Fiksni Trosak").get();
	}
}
