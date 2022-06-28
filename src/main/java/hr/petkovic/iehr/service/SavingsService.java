package hr.petkovic.iehr.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.DTO.SavingsDTO;
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

	public void deleteSavingById(Long id) {
		savRepo.deleteById(id);
	}

	public Double findBankSum() {
		Double sum = 0d;
		List<Saving> allSavings = findAllSavings();
		for (Saving s : allSavings) {
			sum += s.getAmountInHRK();
		}
		return sum;
	}

	public Saving increaseSaving(Saving sav, SavingsDTO addSaving) {
		sav.setAmountInCurrency(sav.getAmountInCurrency() + addSaving.getAmountInCurrency());
		sav.setAmountInHRK(sav.getAmountInHRK() + addSaving.getAmountInHRK());
		return saveSaving(sav);
	}

	public Saving decreaseSaving(Saving sav, SavingsDTO addSaving) {
		sav.setAmountInCurrency(sav.getAmountInCurrency() - addSaving.getAmountInCurrency());
		sav.setAmountInHRK(sav.getAmountInHRK() - addSaving.getAmountInHRK());
		return saveSaving(sav);
	}

}
