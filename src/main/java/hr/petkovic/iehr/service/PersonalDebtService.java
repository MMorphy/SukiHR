package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import hr.petkovic.iehr.DTO.PersonalDebtDatabaseDTO;
import hr.petkovic.iehr.DTO.PersonalDebtUIDTO;
import hr.petkovic.iehr.entity.PersonalDebt;
import hr.petkovic.iehr.entity.PersonalDebtPayments;
import hr.petkovic.iehr.entity.PersonalDebtType;
import hr.petkovic.iehr.repo.PersonalDebtPaymentsRepo;
import hr.petkovic.iehr.repo.PersonalDebtRepo;
import hr.petkovic.iehr.repo.PersonalDebtTypeRepo;

@Service
public class PersonalDebtService {

	PersonalDebtRepo dRepo;
	PersonalDebtPaymentsRepo pRepo;
	PersonalDebtTypeRepo tRepo;

	public PersonalDebtService(PersonalDebtRepo debtRepo, PersonalDebtPaymentsRepo payRepo,
			PersonalDebtTypeRepo typeRepo) {
		dRepo = debtRepo;
		pRepo = payRepo;
		tRepo = typeRepo;
	}

	public List<PersonalDebtUIDTO> getActivePersonalDebts() {
		List<PersonalDebtUIDTO> returnList = new ArrayList<>();
		for (PersonalDebtDatabaseDTO dto : dRepo.findAllActiveDebts()) {
			PersonalDebt debt = dRepo.findById(dto.getId()).get();
			if (dto.getOutstandingAmount() == null) {
				dto.setOutstandingAmount(0D);
			} else if (dto.getOutstandingAmount().compareTo(debt.getAmount().doubleValue()) >= 0) {
				continue;
			}
			returnList.add(new PersonalDebtUIDTO(debt, dto.getOutstandingAmount()));
		}
		return returnList;
	}

	public List<PersonalDebtUIDTO> getActivePersonalDebtsToMe() {
		List<PersonalDebtUIDTO> returnList = new ArrayList<>();
		for (PersonalDebtDatabaseDTO dto : dRepo.findAllActiveDebtsByType(getDebtsToMeType())) {
			PersonalDebt debt = dRepo.findById(dto.getId()).get();
			if (dto.getOutstandingAmount() == null) {
				dto.setOutstandingAmount(0D);
			} else if (dto.getOutstandingAmount().compareTo(debt.getAmount().doubleValue()) >= 0) {
				continue;
			}
			returnList.add(new PersonalDebtUIDTO(debt, dto.getOutstandingAmount()));
		}
		return returnList;
	}

	public List<PersonalDebtUIDTO> getMyActivePersonalDebts() {
		List<PersonalDebtUIDTO> returnList = new ArrayList<>();
		for (PersonalDebtDatabaseDTO dto : dRepo.findAllActiveDebtsByType(getMyDebtType())) {
			PersonalDebt debt = dRepo.findById(dto.getId()).get();
			if (dto.getOutstandingAmount() == null) {
				dto.setOutstandingAmount(0D);
			} else if (dto.getOutstandingAmount().compareTo(debt.getAmount().doubleValue()) >= 0) {
				continue;
			}
			returnList.add(new PersonalDebtUIDTO(debt, dto.getOutstandingAmount()));
		}
		return returnList;
	}

	public Double getTotalOutstanding(List<PersonalDebtUIDTO> list) {
		Double sum = 0d;
		for (PersonalDebtUIDTO d : list) {
			sum += d.getOutstandingAmount();
		}
		return sum;
	}

	public Double getTotalAgreed(List<PersonalDebtUIDTO> list) {
		Double sum = 0d;
		for (PersonalDebtUIDTO d : list) {
			sum += d.getDebt().getAmount();
		}
		return sum;
	}

	public Double getTotalPaid(List<PersonalDebtUIDTO> list) {
		Double sum = 0d;
		for (PersonalDebtUIDTO d : list) {
			for (PersonalDebtPayments p : d.getDebt().getPayments()) {
				sum += p.getAmount();
			}
		}
		return sum;
	}

	public List<PersonalDebt> getAllPersonalDebts() {
		return dRepo.findAll();
	}

	public PersonalDebt getNewPersonalDebt() {
		return new PersonalDebt();
	}

	public PersonalDebtPayments getNewPayment() {
		return new PersonalDebtPayments();
	}

	public PersonalDebt saveDebt(PersonalDebt addDebt) {
		return dRepo.save(addDebt);
	}

	public void deletePersonalDebtById(Long id) {
		dRepo.deleteById(id);
	}

	public PersonalDebt findById(Long id) {
		try {
			return dRepo.findById(id).get();
		} catch (Exception ex) {
			return null;
		}
	}

	public PersonalDebt editDebt(Long id, PersonalDebt editDebt) {
		PersonalDebt pd = findById(id);
		pd.setAmount(editDebt.getAmount());
		pd.setUser(editDebt.getUser());
		pd.setDescription(editDebt.getDescription());
		return dRepo.save(pd);

	}

	public Double getOutstandingDebtForId(Long id) {
		Double outstandingAmount;
		outstandingAmount = dRepo.getOutstandingAmountForId(id);
		if (outstandingAmount == null) {
			outstandingAmount = 0d;
		}
		return outstandingAmount;
	}

	public List<PersonalDebtPayments> getPaymentsForId(Long id) {
		return pRepo.findAllByDebt_Id(id);
	}

	public PersonalDebtPayments addPayment(Long id, PersonalDebtPayments addPayment) {
		PersonalDebt debt = findById(id);
		addPayment.setDebt(debt);
		addPayment.setId(null);
		// debt.addPayment(addPayment);
		return pRepo.save(addPayment);
	}

	public void deletePayment(Long id) {
		pRepo.deleteById(id);
	}

	public PersonalDebtPayments findPaymentById(Long id) {
		try {
			return pRepo.findById(id).get();
		} catch (Exception ex) {
			return null;
		}
	}

	public PersonalDebtPayments editPayment(Long id, PersonalDebtPayments editPayment) {
		PersonalDebtPayments payments = findPaymentById(id);
		payments.setAmount(editPayment.getAmount());
		payments.setDescription(editPayment.getDescription());
		return pRepo.save(payments);
	}

	public PersonalDebtType getMyDebtType() {
		try {
			return tRepo.findByType("Moji Dugovi").get();
		} catch (Exception ex) {
			return null;
		}
	}

	public PersonalDebtType getDebtsToMeType() {
		try {
			return tRepo.findByType("Dugovi prema meni").get();
		} catch (Exception ex) {
			return null;
		}
	}

	public List<PersonalDebtType> getPersonalDebtTypes() {
		return tRepo.findAll();
	}

	public List<PersonalDebt> getAllMyDebts() {
		return dRepo.findByType(getMyDebtType());
	}

	public List<PersonalDebt> getAllDebtsToMe() {
		return dRepo.findByType(getDebtsToMeType());
	}

	public Double getInPaymentsSum() {
		Double sum = 0d;
		List<PersonalDebt> debts = getAllDebtsToMe();
		for (PersonalDebt d : debts) {
			for (PersonalDebtPayments p : d.getPayments()) {
				sum += p.getAmount();
			}
		}
		return sum;
	}

	public Double getOutPaymentsSum() {
		Double sum = 0d;
		List<PersonalDebt> debts = getAllMyDebts();
		for (PersonalDebt d : debts) {
			for (PersonalDebtPayments p : d.getPayments()) {
				sum += p.getAmount();
			}
		}
		return sum;
	}
}
