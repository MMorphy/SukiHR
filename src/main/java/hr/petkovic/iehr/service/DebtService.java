package hr.petkovic.iehr.service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.Debt;
import hr.petkovic.iehr.repo.DebtRepo;

@Service
public class DebtService {

	private DebtRepo debtRepo;
	private TransactionService tServ;

	public DebtService(DebtRepo debtRepo, TransactionService transService) {
		this.debtRepo = debtRepo;
		this.tServ = transService;
	}

	public List<Debt> findAllDebts() {
		return this.debtRepo.findAllNonZeroDebts();
	}

	public List<Debt> findAllDebtsForUsername(String username) {
		return this.debtRepo.findAllNonZeroDebtsForUsername(username);
	}

	public List<Debt> findAllDebtsForSiteId(Long id){
		return this.debtRepo.findAllNonZeroDebtsForSiteId(id);
	}
	public Float getDebtsForLoggedInUser() {
		for (GrantedAuthority ga : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if (ga.toString().equals("ROLE_ADMIN")) {
				return sumOfAllDebts();
			}
		}
		return sumOfUserDebts(SecurityContextHolder.getContext().getAuthentication().getName());
	}

	private Float sumOfAllDebts() {
		Float sum = 0.0f;
		List<Debt> debts = findAllDebts();
		for (Debt d : debts) {
			sum += d.getAmount();
		}
		return sum;
	}

	private Float sumOfUserDebts(String username) {
		Float sum = 0.0f;
		List<Debt> debts = findAllDebtsForUsername(username);
		for (Debt d : debts) {
			sum += d.getAmount();
		}
		return sum;
	}

	public Debt findById(Long id) {
		return debtRepo.findById(id).get();
	}

	public Debt editDebt(Long id, Debt editDebt) {
		Debt oldDebt = findById(id);
		oldDebt.setAmount(editDebt.getAmount());
		return debtRepo.save(oldDebt);
	}

	public void deleteDebt(Long id) {
		tServ.detachDebtByDebtId(id);
		debtRepo.deleteById(id);
	}
}
