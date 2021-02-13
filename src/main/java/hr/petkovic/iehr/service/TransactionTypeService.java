package hr.petkovic.iehr.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.TransactionType;
import hr.petkovic.iehr.repo.TransactionTypeRepo;

@Service
public class TransactionTypeService {
	Logger logger = LoggerFactory.getLogger(SiteService.class);

	private TransactionTypeRepo typeRepo;

	public TransactionTypeService(TransactionTypeRepo transactionTypeRepo) {
		typeRepo = transactionTypeRepo;
	}

	public TransactionType getDefaultIncomeType() {
		return typeRepo.findByMainTypeAndSubType("Ulaz", "Lokal").get();
	}

	public List<TransactionType> getAllExpenseTypes() {
		return typeRepo.findAllByMainType("Izlaz");
	}
}
