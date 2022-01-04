package hr.petkovic.iehr.service;

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
		return typeRepo.findBySubTypeIn(tUtil.getOperativeExpenses());
	}
}
