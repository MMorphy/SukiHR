package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.DTO.SiteWithTotalDebtDTO;
import hr.petkovic.iehr.DTO.UserWithSum;
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

	public List<Transaction> findNonNullTransactions() {
		return transRepo.findAllWithValues();
	}

	public List<Transaction> findAllTransactionsForUsername(String username) {
		return transRepo.findAllByCreatedBy_Username(username);
	}

	public List<Transaction> findNonNullTransactionsForUsername(String username) {
		return transRepo.findAllWithValuesForUser(username);
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

	public Transaction editTransaction(Long oldTransId, Transaction newTransaction) {
		Transaction oldTransaction = findTransactionById(oldTransId);
		if (oldTransaction == null) {
			logger.error("Trying to edit transaction which doesn't exists. Trans id = " + oldTransId.toString());
		}
		if (oldTransaction.getSite() == null && newTransaction.getSite() == null) {
			return editExpense(oldTransaction, newTransaction);
		} else if (oldTransaction.getSite() != null && newTransaction.getSite() != null) {
			return editIncome(oldTransaction, newTransaction);
		} else {
			logger.error("Transaction type mismatch during edit!");
			return null;
		}
	}

	public Transaction editExpense(Transaction oldTrans, Transaction newTrans) {
		if (checkIfDifferent(oldTrans, newTrans)) {
			userSer.updateSaldoAndSave(oldTrans.getAmount(), true);
			oldTrans.setAmount(newTrans.getAmount());
			oldTrans.setType(newTrans.getType());
			oldTrans.setDescription(newTrans.getDescription());
			return saveExpenseWithLoggedInUser(oldTrans);
		} else {
			return oldTrans;
		}
	}

	public Transaction editIncome(Transaction oldTrans, Transaction newTrans) {
		if (checkIfDifferent(oldTrans, newTrans)) {
			userSer.updateSaldoAndSave(oldTrans.getAmount(), false);
			oldTrans.setAmount(newTrans.getAmount());
			oldTrans.setDescription(newTrans.getDescription());
			if (!equalsFloat(oldTrans.getDebt().getAmount(), newTrans.getDebt().getAmount())) {
				oldTrans.getDebt().setAmount(newTrans.getDebt().getAmount());
			}
			if (!oldTrans.getSite().equals(newTrans.getSite())) {
				oldTrans.setSite(newTrans.getSite());
			}
			return saveIncomeWithLoggedInUserAndAddDebtRepay(oldTrans, oldTrans.getDebt());
		} else {
			return oldTrans;
		}
	}

	public boolean checkIfDifferent(Transaction oldT, Transaction newT) {
		if (oldT.getSite() == null) {
			if (!equalsFloat(oldT.getAmount(), newT.getAmount()) || !oldT.getDescription().equals(newT.getDescription())
					|| !oldT.getType().equals(newT.getType())) {
				return true;
			}
			return false;
		} else {
			if (!equalsFloat(oldT.getAmount(), newT.getAmount()) || !oldT.getDescription().equals(newT.getDescription())
					|| !oldT.getSite().equals(newT.getSite())
					|| !equalsFloat(oldT.getDebt().getAmount(), newT.getDebt().getAmount())) {
				return true;
			}
			return false;
		}
	}

	public boolean equalsFloat(Float first, Float second) {
		Float epsilon = 0.000000001F;
		if (Math.abs((first - second)) < epsilon) {
			return true;
		}
		return false;
	}

	public List<SiteWithTotalDebtDTO> findAllSitesWithDebt() {
		return transRepo.findAllSitesAndDebt();
	}

	public List<SiteWithTotalDebtDTO> findAllSitesWithDebtForUsername(String username) {
		return transRepo.findAllSitesAndDebtCreatedBy(username);
	}

	public List<SiteWithTotalDebtDTO> makeFullSiteListActive(List<Site> sitesWithoutDebt,
			List<SiteWithTotalDebtDTO> sitesWithDebt) {
		List<Site> newSites = new ArrayList<Site>();
		for (SiteWithTotalDebtDTO siteD : sitesWithDebt) {
			newSites.add(siteD.getSite());
		}
		sitesWithoutDebt.removeAll(newSites);
		for (Site s : sitesWithoutDebt) {
			sitesWithDebt.add(new SiteWithTotalDebtDTO(s, 0d));
		}
		List<SiteWithTotalDebtDTO> listToReturn = new ArrayList<>();
		for (SiteWithTotalDebtDTO s : sitesWithDebt) {
			if (s.getSite().getReleaseDate() == null) {
				listToReturn.add(s);
			}
		}
		return listToReturn;
	}

	public List<SiteWithTotalDebtDTO> makeFullSiteListInactive(List<Site> sitesWithoutDebt,
			List<SiteWithTotalDebtDTO> sitesWithDebt) {
		List<Site> newSites = new ArrayList<Site>();
		for (SiteWithTotalDebtDTO siteD : sitesWithDebt) {
			newSites.add(siteD.getSite());
		}
		sitesWithoutDebt.removeAll(newSites);
		for (Site s : sitesWithoutDebt) {
			sitesWithDebt.add(new SiteWithTotalDebtDTO(s, 0d));
		}
		List<SiteWithTotalDebtDTO> listToReturn = new ArrayList<>();
		for (SiteWithTotalDebtDTO s : sitesWithDebt) {
			if (s.getSite().getReleaseDate() != null) {
				listToReturn.add(s);
			}
		}
		return listToReturn;
	}

	public List<UserWithTotalDebtDTO> findAllUsersAndDebt() {
		return transRepo.findAllDebtForUser();
	}

	public List<Transaction> findAllBankTransactions() {
		return transRepo.findAllByCreatedBy_Roles_NameOrType_SubType("ROLE_ADMIN", "Razduzenje");
	}

	public List<UserWithSum> updateSaldoForUserDTOs(List<UserWithTotalDebtDTO> users) {
		List<UserWithSum> rList = new ArrayList<>();
		for (UserWithTotalDebtDTO user : users) {
			rList.add(new UserWithSum(user, getSaldoForUser(user.getUser())));
		}
		return rList;
	}

	private Double getSaldoForUser(User user) {
		int userRole = getUserRole(user);
		// Normal user
		if (userRole == 1) {
			return getTransactionDifference(user);
		} else {
			// Summary saldo
			return getSummarySaldo();
		}
	}

	private int getUserRole(User user) {
		return user.getRoles().size();
	}

	private Double getTransactionDifference(User user) {
		Optional<Double> incomes = Optional
				.ofNullable(transRepo.findAllTransactionsOfMainTypeForUsername(user.getUsername(), "Ulaz"));
		Optional<Double> expenses = Optional
				.ofNullable(transRepo.findAllTransactionsOfMainTypeForUsername(user.getUsername(), "Izlaz"));
		return incomes.orElse(0d) - expenses.orElse(0d);
	}

	public Double getSaldoForLoggedInUser() {
		User user = userSer.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		return getSaldoForUser(user);
	}

	public Double getSummarySaldo() {
		Double sum = 0d;
		List<User> users = userSer.findAllEnabledUsers();
		for (User u : users) {
			if (getUserRole(u) == 1) {
				sum += getTransactionDifference(u);
			}
		}
		return sum;
	}

	public boolean isAdmin(String username) {
		User u = userSer.findUserByUsername(username);
		if (u == null) {
			return false;
		} else if (getUserRole(u) == 3) {
			return true;
		} else {
			return false;
		}
	}
}
