package hr.petkovic.iehr.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.petkovic.iehr.DTO.PersonalDebtDatabaseDTO;
import hr.petkovic.iehr.DTO.PersonalDebtUIDTO;
import hr.petkovic.iehr.DTO.report.PersonalDebtPaymentsDTO;
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

	public List<PersonalDebtUIDTO> getPersonalDebtsForBank() {
		List<PersonalDebtUIDTO> returnList = new ArrayList<>();
		for (PersonalDebtDatabaseDTO dto : dRepo.findAllActiveDebts()) {
			PersonalDebt debt = dRepo.findById(dto.getId()).get();
			if (dto.getPaidAmount() == null) {
				dto.setPaidAmount(0D);
			}
			returnList.add(new PersonalDebtUIDTO(debt, dto.getPaidAmount()));
		}
		return returnList;
	}

	public List<PersonalDebtUIDTO> getActivePersonalDebtsToMe() {
		List<PersonalDebtUIDTO> returnList = new ArrayList<>();
		for (PersonalDebtDatabaseDTO dto : dRepo.findAllActiveDebtsByType(getDebtsToMeType())) {
			PersonalDebt debt = dRepo.findById(dto.getId()).get();

			if (dto.getPaidAmount() == null) {
				dto.setPaidAmount(0D);
			} else {
				BigDecimal left = BigDecimal.valueOf(dto.getPaidAmount());
				left = left.setScale(2, BigDecimal.ROUND_HALF_DOWN);
				BigDecimal right = BigDecimal.valueOf(debt.getAmount().doubleValue());
				right = right.setScale(2, BigDecimal.ROUND_HALF_DOWN);
				if (left.compareTo(right) >= 0) {
					continue;
				}
			}
			returnList.add(new PersonalDebtUIDTO(debt, dto.getPaidAmount()));
		}
		return returnList;
	}

	public List<PersonalDebtUIDTO> getResolvedPersonalDebtsToMe() {
		List<PersonalDebtUIDTO> returnList = new ArrayList<>();
		for (PersonalDebtDatabaseDTO dto : dRepo.findAllActiveDebtsByType(getDebtsToMeType())) {
			PersonalDebt debt = dRepo.findById(dto.getId()).get();
			if (dto.getPaidAmount() == null) {
				continue;
			} else {
				BigDecimal left = BigDecimal.valueOf(dto.getPaidAmount());
				left = left.setScale(2, BigDecimal.ROUND_HALF_DOWN);
				BigDecimal right = BigDecimal.valueOf(debt.getAmount().doubleValue());
				right = right.setScale(2, BigDecimal.ROUND_HALF_DOWN);
				if (left.compareTo(right) < 0) {
					continue;
				}
			}

			returnList.add(new PersonalDebtUIDTO(debt, dto.getPaidAmount()));
		}
		return returnList;
	}

	public List<PersonalDebtUIDTO> getMyActivePersonalDebts() {
		List<PersonalDebtUIDTO> returnList = new ArrayList<>();
		for (PersonalDebtDatabaseDTO dto : dRepo.findAllActiveDebtsByType(getMyDebtType())) {
			PersonalDebt debt = dRepo.findById(dto.getId()).get();
			if (dto.getPaidAmount() == null) {
				dto.setPaidAmount(0D);
			} else {
				BigDecimal left = BigDecimal.valueOf(dto.getPaidAmount());
				left = left.setScale(2, BigDecimal.ROUND_HALF_DOWN);
				BigDecimal right = BigDecimal.valueOf(debt.getAmount().doubleValue());
				right = right.setScale(2, BigDecimal.ROUND_HALF_DOWN);
				if (left.compareTo(right) >= 0) {
					continue;
				}
			}
			returnList.add(new PersonalDebtUIDTO(debt, dto.getPaidAmount()));
		}
		return returnList;
	}

	public List<PersonalDebtUIDTO> getMyResolvedPersonalDebts() {
		List<PersonalDebtUIDTO> returnList = new ArrayList<>();
		for (PersonalDebtDatabaseDTO dto : dRepo.findAllActiveDebtsByType(getMyDebtType())) {
			PersonalDebt debt = dRepo.findById(dto.getId()).get();
			if (dto.getPaidAmount() == null) {
				continue;
			} else {
				BigDecimal left = BigDecimal.valueOf(dto.getPaidAmount());
				left = left.setScale(2, BigDecimal.ROUND_HALF_DOWN);
				BigDecimal right = BigDecimal.valueOf(debt.getAmount().doubleValue());
				right = right.setScale(2, BigDecimal.ROUND_HALF_DOWN);
				if (left.compareTo(right) < 0) {
					continue;
				}
			}
			returnList.add(new PersonalDebtUIDTO(debt, dto.getPaidAmount()));
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

	public PersonalDebt getNewPersonalDebtToMe() {
		PersonalDebt pd = new PersonalDebt();
		pd.setType(tRepo.findByType("Dugovi prema meni").get());
		return pd;
	}

	public PersonalDebt getNewMyPersonalDebt() {
		PersonalDebt pd = new PersonalDebt();
		pd.setType(tRepo.findByType("Moji Dugovi").get());
		return pd;
	}

	public PersonalDebtPayments getNewPayment() {
		return new PersonalDebtPayments();
	}

	public PersonalDebt saveDebt(PersonalDebt addDebt) {
		return dRepo.save(addDebt);
	}

	@Transactional
	public void deletePersonalDebtById(Long id) {
		pRepo.deleteAllByDebt_Id(id);
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

	public List<PersonalDebtPaymentsDTO> getPaymentsForId(Long id) {
		List<PersonalDebtPayments> debts = pRepo.findAllByDebt_Id(id);
		List<PersonalDebtPaymentsDTO> returnList = new ArrayList<PersonalDebtPaymentsDTO>();
		Float currentDebt = dRepo.findById(id).get().getAmount();
		for (PersonalDebtPayments d : debts) {
			currentDebt -= d.getAmount();
			returnList.add(new PersonalDebtPaymentsDTO(d, currentDebt));
		}
		return returnList;
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

	public Double getInPaymentsSumForYear(Integer year) {
		Double sum = 0d;
		List<PersonalDebt> debts = getAllDebtsToMe();
		for (PersonalDebt d : debts) {
			for (PersonalDebtPayments p : d.getPayments()) {
				if (p.getPaymentDate().getYear() == (year - 1900)) {
					sum += p.getAmount();
				}
			}
		}
		return sum;
	}

	public Double getInPaymentsSumForYearAndMonth(Integer year, Integer month) {
		Double sum = 0d;
		List<PersonalDebt> debts = getAllDebtsToMe();
		for (PersonalDebt d : debts) {
			for (PersonalDebtPayments p : d.getPayments()) {
				if (p.getPaymentDate().getYear() == (year - 1900) && p.getPaymentDate().getMonth() == (month - 1)) {
					sum += p.getAmount();
				}
			}
		}
		return sum;
	}

	public Double getInAgreedSum() {
		Double sum = 0d;
		List<PersonalDebt> debts = getAllDebtsToMe();
		for (PersonalDebt d : debts) {
			sum += d.getAmount();
		}
		return sum;
	}

	public Double getInAgreedSumForYear(Integer year) {
		Double sum = 0d;
		List<PersonalDebt> debts = getAllDebtsToMe();
		for (PersonalDebt d : debts) {
			if (d.getCreateDate().getYear() == (year - 1900)) {
				sum += d.getAmount();
			}
		}
		return sum;
	}

	public Double getInAgreedSumForYearAndMonth(Integer year, Integer month) {
		Double sum = 0d;
		List<PersonalDebt> debts = getAllDebtsToMe();
		for (PersonalDebt d : debts) {
			if (d.getCreateDate().getYear() == (year - 1900) && d.getCreateDate().getMonth() == (month - 1)) {
				sum += d.getAmount();
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

	public Double getOutPaymentsSumForYear(Integer year) {
		Double sum = 0d;
		List<PersonalDebt> debts = getAllMyDebts();
		for (PersonalDebt d : debts) {
			for (PersonalDebtPayments p : d.getPayments()) {
				if (p.getPaymentDate().getYear() == (year - 1900)) {
					sum += p.getAmount();
				}
			}
		}
		return sum;
	}

	public Double getOutPaymentsSumForYearAndMonth(Integer year, Integer month) {
		Double sum = 0d;
		List<PersonalDebt> debts = getAllMyDebts();
		for (PersonalDebt d : debts) {
			for (PersonalDebtPayments p : d.getPayments()) {
				if (p.getPaymentDate().getYear() == (year - 1900) && p.getPaymentDate().getMonth() == (month - 1)) {
					sum += p.getAmount();
				}
			}
		}
		return sum;
	}

	public Double getOutAgreedSum() {
		Double sum = 0d;
		List<PersonalDebt> debts = getAllMyDebts();
		for (PersonalDebt d : debts) {
			sum += d.getAmount();
		}
		return sum;
	}

	public Double getOutAgreedSumForYear(Integer year) {
		Double sum = 0d;
		List<PersonalDebt> debts = getAllMyDebts();
		for (PersonalDebt d : debts) {
			if (d.getCreateDate().getYear() == (year - 1900)) {
				sum += d.getAmount();
			}
		}
		return sum;
	}

	public Double getOutAgreedSumForYearAndMonth(Integer year, Integer month) {
		Double sum = 0d;
		List<PersonalDebt> debts = getAllMyDebts();
		for (PersonalDebt d : debts) {
			if (d.getCreateDate().getYear() == (year - 1900) && d.getCreateDate().getMonth() == (month - 1)) {
				sum += d.getAmount();
			}
		}
		return sum;
	}

	public Double getAllBankIncome() {
		Double sum = 0d;
		sum += getOutAgreedSum();
		sum += getInPaymentsSum();
		return sum;
	}

	public Double getAllBankIncomeForYear(Integer year) {
		Double sum = 0d;
		sum += getOutAgreedSumForYear(year);
		sum += getInPaymentsSumForYear(year);
		return sum;
	}

	public Double getAllBankIncomeForYearAndMonth(Integer year, Integer month) {
		Double sum = 0d;
		sum += getOutAgreedSumForYearAndMonth(year, month);
		sum += getInPaymentsSumForYearAndMonth(year, month);
		return sum;
	}

	public Double getAllBankExpense() {
		Double sum = 0d;
		sum += getInAgreedSum();
		sum += getOutPaymentsSum();
		return sum;
	}

	public Double getAllBankExpenseForYear(Integer year) {
		Double sum = 0d;
		sum += getInAgreedSumForYear(year);
		sum += getOutPaymentsSumForYear(year);
		return sum;
	}

	public Double getAllBankExpenseForYearMonth(Integer year, Integer month) {
		Double sum = 0d;
		sum += getInAgreedSumForYearAndMonth(year, month);
		sum += getOutPaymentsSumForYearAndMonth(year, month);
		return sum;
	}

	public PersonalDebt setMyDebt(PersonalDebt addDebt) {
		addDebt.setType(tRepo.findByType("Moji Dugovi").get());
		return addDebt;
	}

	public PersonalDebt setDebtTome(PersonalDebt addDebt) {
		addDebt.setType(tRepo.findByType("Dugovi prema meni").get());
		return addDebt;
	}

	/*
	 * public PersonalDebt getNewPersonalDebtToMe() { PersonalDebt pd = new
	 * PersonalDebt(); pd.setType(tRepo.findByType("Dugovi prema meni").get());
	 * return pd; }
	 * 
	 * public PersonalDebt getNewMyPersonalDebt() { PersonalDebt pd = new
	 * PersonalDebt(); pd.setType(tRepo.findByType("Moji Dugovi").get()); return pd;
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
}
