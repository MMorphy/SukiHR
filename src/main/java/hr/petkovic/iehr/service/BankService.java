package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.Transaction;
import hr.petkovic.iehr.repo.BankUtilRepo;

@Service
public class BankService {

	private TransactionService transServ;
	private BankUtilRepo bankUtilRepo;
	private PersonalDebtService pdServ;
	private SavingsService saveServ;
	private ProjectService projServ;

	public BankService(TransactionService tService, BankUtilRepo bRepo, PersonalDebtService personalDebtService,
			SavingsService savingsService, ProjectService projectService) {
		transServ = tService;

		bankUtilRepo = bRepo;
		pdServ = personalDebtService;
		saveServ = savingsService;
		projServ = projectService;
	}

	public List<Transaction> findBankUserTransacations() {
		return transServ.findAllTransactionsForUsername("banka");
	}

	public Double getSum() {
		Float f = bankUtilRepo.findById(1L).get().getAmount();
		Double sum = f.doubleValue();
		// Bank special income
		sum += transServ.findBankUserIncomesSum();
		// User razduzenje
		sum += transServ.findBankIncomeSum();
		// Personal Debt income
		sum += pdServ.getAllBankIncome();
		// Personal Debt expense
		sum -= pdServ.getAllBankExpense();
		// Savings
		sum -= saveServ.findBankSum();
		// Projects
		sum -= projServ.findBankSum();
		// Bank expenses
		sum -= transServ.findBankExpenseSum();
		return sum;
	}

	public List<Transaction> getAllBankTransactions() {
		List<Transaction> transactions = new ArrayList<>();
		transactions.addAll(findBankUserTransacations());
		transactions.addAll(transServ.findAllBankIncomeTransactions());
		return transactions;
	}
}
