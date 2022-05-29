package hr.petkovic.iehr.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.Saving;
import hr.petkovic.iehr.repo.SavingsRepo;

@Service
public class SavingsService {

	Logger logger = LoggerFactory.getLogger(SavingsService.class);

	private SavingsRepo savRepo;

	public SavingsService(SavingsRepo savingsRepository) {
		this.savRepo = savingsRepository;
	}

	public Saving getNewSaving() {
		return new Saving();
	}

	public Saving findSavingById(Long id) {
		try {
			return savRepo.findById(id).get();
		} catch (Exception ex) {
			logger.error("Getting Savings with id=" + id + " which doesn't exist!");
		}
		return null;
	}

	public List<Saving> findAllSavings() {
		return savRepo.findAll();
	}

	public Saving saveSaving(Saving addSaving) {
		return savRepo.save(addSaving);
	}

	public Saving editSaving(Long id, Saving editSaving) {
		Saving oldSaving = findSavingById(id);
		if (oldSaving != null) {
			oldSaving.setAmount(editSaving.getAmount());
			return savRepo.save(oldSaving);
		}
		return null;
	}

	public void deleteSavingById(Long id) {
		savRepo.deleteById(id);
	}

	public Double findBankSum() {
		Double sum = 0d;
		List<Saving> allSavings = findAllSavings();
		for (Saving s : allSavings) {
			sum += s.getAmount();
		}
		return sum;
	}
}
