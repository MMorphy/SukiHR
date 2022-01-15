package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.Transaction;
import hr.petkovic.iehr.repo.BankUtilRepo;
import hr.petkovic.iehr.util.TimeUtil;
import hr.petkovic.iehr.util.TransactionTypeUtil;

@Service
public class BankService {

	private TransactionService transServ;
	private PersonalDebtService debtServ;
	private TransactionTypeUtil util;
	private TimeUtil timeUtil;
	private BankUtilRepo bankUtilRepo;

	public BankService(TransactionService tService, PersonalDebtService dService, TransactionTypeUtil u, TimeUtil time,
			BankUtilRepo bRepo) {
		transServ = tService;
		debtServ = dService;
		util = u;
		timeUtil = time;
		bankUtilRepo = bRepo;
	}

	public List<Transaction> findAllBankTransactions() {
		List<Transaction> rList = transServ.findAllBankTransactions();
		rList = filterOutAdminPrivate(rList);
		rList = filterOutAdminIncome(rList);
		rList = filterOutAdminBusiness(rList);
		rList = filterOutAdminOperative(rList);
		return rList;
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

	public Double getSumFiltered(Set<Transaction> set) {
		Float f = bankUtilRepo.findById(1L).get().getAmount();
		Double sum = f.doubleValue();
		for (Transaction t : set) {
			if (t.getCreateDate().after(timeUtil.getCurrentYearBreakpointDate())) {
				if (t.getType().getSubType().equals("RAZDUZENJE")) {
					sum += t.getAmount();
				} else if (t.getType().getSubType().equals("Banka")) {
					sum += t.getAmount();
				} else {
					sum -= t.getAmount();
				}
			}
		}
		sum += debtServ.getOutAgreedSum();
		sum -= debtServ.getOutPaymentsSum();
		sum -= debtServ.getInAgreedSum();
		sum += debtServ.getInPaymentsSum();

		return sum;
	}

	public Double getSumUnfiltered(Set<Transaction> set) {
		Float f = bankUtilRepo.findById(1L).get().getAmount();
		Double sum = f.doubleValue();
		for (Transaction t : set) {
			if (t.getCreateDate().after(timeUtil.getStartPointYearBreakpointDate())) {
				if (t.getType().getSubType().equals("RAZDUZENJE")) {
					sum += t.getAmount();
				} else if (t.getType().getSubType().equals("Banka")) {
					sum += t.getAmount();
				} else {
					sum -= t.getAmount();
				}
			}
		}
		sum += debtServ.getOutAgreedSum();
		sum -= debtServ.getOutPaymentsSum();
		sum -= debtServ.getInAgreedSum();
		sum += debtServ.getInPaymentsSum();

		return sum;
	}

	public List<Transaction> filterOutAdminPrivate(List<Transaction> bankTransactions) {
		List<Transaction> rList = new ArrayList<Transaction>();
		for (Transaction t : bankTransactions) {
			if (!util.isPrivate(t.getType())) {
				rList.add(t);
			}
		}
		return rList;
	}

	public List<Transaction> filterOutAdminIncome(List<Transaction> bankTransactions) {
		List<Transaction> rList = new ArrayList<Transaction>();
		for (Transaction t : bankTransactions) {
			if (!util.isIncome(t.getType())) {
				rList.add(t);
			}
		}
		return rList;
	}

	public List<Transaction> filterOutAdminBusiness(List<Transaction> bankTransactions) {
		List<Transaction> rList = new ArrayList<Transaction>();
		for (Transaction t : bankTransactions) {
			if (!util.isBusiness(t.getType())) {
				rList.add(t);
			}
		}
		return rList;
	}

	public List<Transaction> filterOutAdminOperative(List<Transaction> bankTransactions) {
		List<Transaction> rList = new ArrayList<Transaction>();
		for (Transaction t : bankTransactions) {
			if (util.isRazduzenje(t.getType())) {
				rList.add(t);
			}
			if (!util.isOperative(t.getType())) {
				rList.add(t);
			}
		}
		return rList;
	}

	public Set<Transaction> filterOutOldYear(Set<Transaction> set) {
		Set<Transaction> rSet = new HashSet<>();
		for (Transaction t : set) {
			if (t.getCreateDate().after(timeUtil.getCurrentYearBreakpointDate())) {
				rSet.add(t);
			}
		}
		return rSet;
	}

	public boolean isAdmin(String username) {
		return transServ.isAdmin(username);
	}

	public boolean isBank(String username) {
		return transServ.isOnlyBank(username);
	}
}
