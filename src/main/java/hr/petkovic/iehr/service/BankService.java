package hr.petkovic.iehr.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.Transaction;

@Service
public class BankService {

	private TransactionService transServ;

	public BankService(TransactionService tService) {
		transServ = tService;
	}

	public List<Transaction> findAllBankTransactions(){
		return transServ.findAllBankTransactions();
	}

	public Float getSum(Set<Transaction> set) {
		Float sum = 0F;
		for (Transaction t : set) {
			if (t.getType().getSubType().equals("Razduzenje")) {
				sum+=t.getAmount();
			}
			else {
				sum-=t.getAmount();
			}
		}
		return sum;
	}
}
