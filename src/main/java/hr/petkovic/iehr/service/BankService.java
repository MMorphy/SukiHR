package hr.petkovic.iehr.service;

import java.util.Date;
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

	public List<Transaction> findBankUserTransacations() {
		return transServ.findAllTransactionsForUsername("banka");
	}

	public Double findBankUserIncomeSum() {
		Double d = transServ.findBankUserIncomesSum();
		if (d == null)
			return 0d;
		else
			return d;
	}

	public Double findBankUserExpenseSum() {
		Double d = transServ.findBankUserExpensesSum();
		if (d == null)
			return 0d;
		else
			return d;
	}

	public Double getSum(Set<Transaction> set) {
		Double sum = 5019.7d;
		for (Transaction t : set) {
			if (t.getType().getSubType().equals("RAZDUZENJE")) {
				if (t.getCreateDate().after(new Date(2021, 12, 31)))
					sum += t.getAmount();
			} else {
				if (t.getCreateDate().after(new Date(2021, 12, 31)))
					sum -= t.getAmount();
			}
		}
		sum += findBankUserIncomeSum();
		sum -= findBankUserExpenseSum();
		sum += debtServ.getOutAgreedSum();
		sum -= debtServ.getOutPaymentsSum();
		sum -= debtServ.getInAgreedSum();
		sum += debtServ.getInPaymentsSum();

		return sum;
	}
}
