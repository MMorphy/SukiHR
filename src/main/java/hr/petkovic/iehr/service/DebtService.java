package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.DTO.report.DebtDisplayDTO;
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

	public List<DebtDisplayDTO> findAllDebts() {
		List<Debt> debts = this.debtRepo.findAllNonZeroDebts();
		Collections.sort(debts, (a, b) -> a.getCreateDate().compareTo(b.getCreateDate()));
		List<DebtDisplayDTO> returnList = new ArrayList<>();
		Map<Long, Float> currentDebtMap = new HashMap<Long, Float>();
		Float currentDebt = 0f;
		for (Debt d : debts) {
			Long siteId = d.getTransaction().getSite().getId();
			if (d.getTransaction().getSite().getReleaseDate() != null) {
				continue;
			}
			if (currentDebtMap.containsKey(siteId)) {
				currentDebt = currentDebtMap.get(siteId) + d.getAmount();
			} else {
				currentDebt = d.getAmount();
			}
			currentDebtMap.put(siteId, currentDebt);
			returnList.add(new DebtDisplayDTO(d, currentDebt));
		}
		return returnList;
	}

	public List<DebtDisplayDTO> findAllDebtsForUsername(String username) {
		List<Debt> debts = this.debtRepo.findAllNonZeroDebtsForUsername(username);
		Collections.sort(debts, (a, b) -> a.getCreateDate().compareTo(b.getCreateDate()));
		List<DebtDisplayDTO> returnList = new ArrayList<>();
		Map<Long, Float> currentDebtMap = new HashMap<Long, Float>();
		Float currentDebt = 0f;
		for (Debt d : debts) {
			Long siteId = d.getTransaction().getSite().getId();
			if (d.getTransaction().getSite().getReleaseDate() != null) {
				continue;
			}
			if (currentDebtMap.containsKey(siteId)) {
				currentDebt = currentDebtMap.get(siteId) + d.getAmount();
			} else {
				currentDebt = d.getAmount();
			}
			currentDebtMap.put(siteId, currentDebt);
			returnList.add(new DebtDisplayDTO(d, currentDebt));
		}
		return returnList;
	}

	public List<DebtDisplayDTO> findAllDebtsForSiteId(Long id) {
		List<Debt> debts = this.debtRepo.findAllNonZeroDebtsForSiteId(id);
		Collections.sort(debts, (a, b) -> a.getCreateDate().compareTo(b.getCreateDate()));
		List<DebtDisplayDTO> returnList = new ArrayList<>();
		Float currentDebt = 0f;
		for (Debt d : debts) {
			currentDebt += d.getAmount();
			returnList.add(new DebtDisplayDTO(d, currentDebt));
		}
		return returnList;
	}

	public Float getDebtsForLoggedInUser() {
		if (SecurityContextHolder.getContext().getAuthentication().getName().equals("banka")) {
			return sumOfAllDebts();
		}
		/*
		 * for (GrantedAuthority ga :
		 * SecurityContextHolder.getContext().getAuthentication().getAuthorities()) { if
		 * (ga.toString().equals("ROLE_ADMIN")) { return sumOfAllDebts(); } }
		 */ return sumOfUserDebts(SecurityContextHolder.getContext().getAuthentication().getName());
	}

	private Float sumOfAllDebts() {
		Float sum = 0.0f;
		List<DebtDisplayDTO> debts = findAllDebts();
		for (DebtDisplayDTO d : debts) {
			sum += d.getDebt().getAmount();
		}
		return sum;
	}

	private Float sumOfUserDebts(String username) {
		Float sum = 0.0f;
		List<DebtDisplayDTO> debts = findAllDebtsForUsername(username);
		for (DebtDisplayDTO d : debts) {
			sum += d.getDebt().getAmount();
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
