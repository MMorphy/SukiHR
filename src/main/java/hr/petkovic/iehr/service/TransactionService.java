package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.DTO.SiteWithTotalDebtDTO;
import hr.petkovic.iehr.DTO.UserWithTotalDebtDTO;
import hr.petkovic.iehr.entity.Debt;
import hr.petkovic.iehr.entity.Site;
import hr.petkovic.iehr.entity.Transaction;
import hr.petkovic.iehr.entity.User;
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

	public Transaction saveIncomeWithLoggedInUserAndAddDebtRepay(Transaction trans, Debt debt) {
		User u = userSer.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		trans.setCreatedBy(u);
		userSer.updateSaldoAndSave(trans.getAmount(), true);
		trans.setType(typeSer.getDefaultIncomeType());
		trans.getSite().setLastVisit(new Date());
		return this.saveTransaction(trans);
	}

	public Transaction saveExpenseWithLoggedInUser(Transaction trans) {
		User u = userSer.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		trans.setCreatedBy(u);
		userSer.updateSaldoAndSave(trans.getAmount(), false);
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

	public List<SiteWithTotalDebtDTO> findAllSitesWithDebt() {
		return transRepo.findAllSitesAndDebt();
	}

	public List<SiteWithTotalDebtDTO> findAllSitesWithDebtForUsername(String username) {
		return transRepo.findAllSitesAndDebtCreatedBy(username);
	}

	public List<SiteWithTotalDebtDTO> makeFullSiteList(List<Site> sitesWithoutDebt,
			List<SiteWithTotalDebtDTO> sitesWithDebt) {
		List<Site> newSites = new ArrayList<Site>();
		for (SiteWithTotalDebtDTO siteD : sitesWithDebt) {
			newSites.add(siteD.getSite());
		}
		sitesWithoutDebt.removeAll(newSites);
		for (Site s : sitesWithoutDebt) {
			sitesWithDebt.add(new SiteWithTotalDebtDTO(s, 0d));
		}
		return sitesWithDebt;
	}

	public List<UserWithTotalDebtDTO> findAllUsersAndDebt(){
		return transRepo.findAllDebtForUser();
	}
}
