package hr.petkovic.iehr.service;

import java.util.List;

import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.FixedExpense;
import hr.petkovic.iehr.repo.FixedExpenseRepo;

@Service
public class FixedExpenseService {

	private FixedExpenseRepo fixedRepo;

	public FixedExpenseService(FixedExpenseRepo fixedExpenseRepo) {
		fixedRepo = fixedExpenseRepo;
	}

	public List<FixedExpense> getAllFixedExpenses() {
		return fixedRepo.findAll();
	}

	public FixedExpense getNewFixedExpense() {
		return new FixedExpense();
	}

	public FixedExpense saveFixedExpense(FixedExpense addFixExpense) {
		return fixedRepo.save(addFixExpense);
	}

	public FixedExpense findById(Long id) {
		try {
			return fixedRepo.findById(id).get();
		} catch (Exception ex) {
			return null;
		}
	}

	public FixedExpense editFixedExpense(Long id, FixedExpense editFixExpense) {
		FixedExpense exp = findById(id);
		exp.setAmount(editFixExpense.getAmount());
		exp.setName(editFixExpense.getName());
		return saveFixedExpense(exp);
	}

	public void deleteFixedExpense(Long id) {
		fixedRepo.deleteById(id);
	}
}
