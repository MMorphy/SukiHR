package hr.petkovic.iehr.service;

import java.util.List;

import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.Debt;
import hr.petkovic.iehr.repo.DebtRepo;

@Service
public class DebtService {

	private DebtRepo debtRepo;

	public DebtService(DebtRepo debtRepo) {
		this.debtRepo = debtRepo;
	}

	public List<Debt> findAllDebts() {
		return this.debtRepo.findAll();
	}

	public List<Debt> findAllDebtsForUsername(String username) {
		return this.debtRepo.findAllByTransaction_CreatedBy_Username(username);
	}
}
