package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.DTO.SavingsDTO;
import hr.petkovic.iehr.DTO.SavingsDatabaseDTO;
import hr.petkovic.iehr.DTO.SavingsUIDTO;
import hr.petkovic.iehr.entity.Saving;
import hr.petkovic.iehr.entity.SavingsPayment;
import hr.petkovic.iehr.repo.SavingPaymentsRepo;
import hr.petkovic.iehr.repo.SavingsRepo;

@Service
public class SavingsService {

	Logger logger = LoggerFactory.getLogger(SavingsService.class);

	private SavingsRepo savRepo;
	private SavingPaymentsRepo payRepo;

	public SavingsService(SavingsRepo savingsRepository, SavingPaymentsRepo paymentRepo) {
		this.savRepo = savingsRepository;
		this.payRepo = paymentRepo;
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

	public List<SavingsDatabaseDTO> findAllSavingsDBDTOs() {
		return savRepo.findAllSavingsDBDTOs();
	}

	public List<SavingsUIDTO> findAllDisplayDTOS() {
		List<SavingsUIDTO> returnList = new ArrayList<SavingsUIDTO>();
		List<SavingsDatabaseDTO> dtos = findAllSavingsDBDTOs();
		for (SavingsDatabaseDTO dto : dtos) {
			returnList.add(new SavingsUIDTO(findSavingById(dto.getId()), dto.getLocalTotal(), dto.getCurrencyTotal()));
		}
		return returnList;
	}

	public Saving saveSaving(Saving addSaving) {
		return savRepo.save(addSaving);
	}

	public void deleteSavingById(Long id) {
		savRepo.deleteById(id);
	}

	public Double findBankSum() {
		Double sum = 0d;
		List<SavingsDatabaseDTO> allSavings = findAllSavingsDBDTOs();
		if (!allSavings.isEmpty()) {
			for (SavingsDatabaseDTO s : allSavings) {
				if (s.getLocalTotal() != null) {
					sum += s.getLocalTotal();
				}
			}
		}
		return sum;
	}

	public SavingsPayment savePayment(SavingsPayment payment) {
		return payRepo.save(payment);
	}

	public SavingsPayment increaseSaving(Saving sav, SavingsDTO addSaving) {
		SavingsPayment sp = new SavingsPayment();
		sp.setAmountInCurrency(addSaving.getAmountInCurrency());
		sp.setAmountInHRK(addSaving.getAmountInHRK());
		sp.setNote(addSaving.getNote());
		sp.setSaving(sav);
		sp.setCreateDate(new Date());
		return savePayment(sp);
	}

	public SavingsPayment decreaseSaving(Saving sav, SavingsDTO addSaving) {
		SavingsPayment sp = new SavingsPayment();
		sp.setAmountInCurrency(addSaving.getAmountInCurrency()* -1f);
		sp.setAmountInHRK(addSaving.getAmountInHRK() * -1f);
		sp.setNote(addSaving.getNote());
		sp.setSaving(sav);
		sp.setCreateDate(new Date());
		return savePayment(sp);
	}

	public List<SavingsPayment> findAllSavingPayments() {
		return payRepo.findAll();
	}

	public List<SavingsPayment> getAllPaymentsForSavingsId(Long id) {
		return payRepo.findAllBySaving_Id(id);
	}

	public String getSignForId(Long id) {
		Saving sav = findSavingById(id);
		return sav.getSign();
	}
}
