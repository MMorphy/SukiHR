package hr.petkovic.iehr.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.Transaction;

@Service
public class BankService {

	private TransactionService transServ;
	private PersonalDebtService debtServ;

	public BankService(TransactionService tService, PersonalDebtService dService) {
		transServ = tService;
		debtServ = dService;
	}

	public List<Transaction> findAllBankTransactions() {
		return transServ.findAllBankTransactions();
	}

	public Double getSum(Set<Transaction> set) {
		Double sum = 0d;
		for (Transaction t : set) {
			if (t.getType().getSubType().equals("Razduzenje")) {
				sum += t.getAmount();
			} else {
				sum -= t.getAmount();
			}
		}
		sum -= debtServ.getOutPaymentsSum();
		sum += debtServ.getInPaymentsSum();
		return sum;
	}
}
