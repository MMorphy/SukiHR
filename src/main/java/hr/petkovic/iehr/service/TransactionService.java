package hr.petkovic.iehr.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.Transaction;
import hr.petkovic.iehr.repo.TransactionRepo;

@Service
public class TransactionService {
	Logger logger = LoggerFactory.getLogger(TransactionService.class);

	private TransactionRepo transRepo;
	private UserService userSer;
	private TransactionTypeService typeSer;

	public TransactionService(TransactionRepo transR, UserService userService, TransactionTypeService typeService) {
		transRepo = transR;
		userSer = userService;
		typeSer = typeService;
	}

	public Transaction findTransactionById(Long id) {
		try {
			return transRepo.findById(id).get();
		} catch (Exception ex) {
			logger.error("Exception occured in findTransactionById", ex);
		}
		return null;
	}

	public List<Transaction> findAllTransactions() {
		return transRepo.findAll();
	}

	public List<Transaction> findAllTransactionsForUsername(String username) {
		return transRepo.findAllByCreatedBy_Username(username);
	}

	public Transaction saveTransaction(Transaction trans) {
		return transRepo.save(trans);
	}

	public Transaction saveIncomeWithLoggedInUserAndAddDebtRepay(Transaction trans, Float debt) {
		trans.setCreatedBy(
				userSer.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
		trans.setType(typeSer.getDefaultIncomeType());
		trans.getSite().setLastVisit(new Date());
		trans.getSite().setDebt(trans.getSite().getDebt() - debt);
		return this.saveTransaction(trans);
	}

	public Transaction saveExpenseWithLoggedInUser(Transaction trans) {
		trans.setCreatedBy(
				userSer.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
		return this.saveTransaction(trans);
	}

	public void deleteTrans(Transaction trans) {
		transRepo.delete(trans);
	}

	public void deleteTransById(Long id) {
		Transaction trans;
		trans = findTransactionById(id);
		if (trans != null) {
			deleteTrans(trans);
		} else {
			logger.warn("Trying to delete transaction which doesn't exist!");
		}
	}
}
