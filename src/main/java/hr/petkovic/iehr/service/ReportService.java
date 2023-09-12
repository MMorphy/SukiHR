package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.DTO.report.InOutMonthReportDTO;
import hr.petkovic.iehr.DTO.report.InOutYearReportDTO;
import hr.petkovic.iehr.DTO.report.ReportingBaseDTO;
import hr.petkovic.iehr.DTO.report.SiteMonthReportDTO;
import hr.petkovic.iehr.DTO.report.SiteTotalReportDTO;
import hr.petkovic.iehr.DTO.report.SiteYearReportDTO;
import hr.petkovic.iehr.DTO.report.TransactionMonthReportDTO;
import hr.petkovic.iehr.DTO.report.TransactionTotalReportDTO;
import hr.petkovic.iehr.DTO.report.TransactionYearReportDTO;
import hr.petkovic.iehr.DTO.report.UserMonthReportDTO;
import hr.petkovic.iehr.DTO.report.UserTotalReportDTO;
import hr.petkovic.iehr.DTO.report.UserYearReportDTO;
import hr.petkovic.iehr.entity.User;

@Service
public class ReportService {

	private TransactionService tServ;
	private UserService uServ;
	private ProjectService pServ;
	private PersonalDebtService debServ;
	private SavingsService sServ;

	public ReportService(TransactionService transactionService, UserService userService, ProjectService projectService,
			PersonalDebtService personalDebtService, SavingsService savingsService) {
		tServ = transactionService;
		uServ = userService;
		pServ = projectService;
		debServ = personalDebtService;
		sServ = savingsService;
	}

	public List<UserTotalReportDTO> getUserTotals() {
		List<UserTotalReportDTO> returnList = new ArrayList<>();
		List<User> users = uServ.findAllEnabledUsers();
		for (User u : users) {
			returnList.addAll(tServ.findUserTotalReport(u));
		}
		return returnList;
	}

	public List<UserYearReportDTO> getUserYear() {
		List<UserYearReportDTO> returnList = new ArrayList<>();
		List<User> users = uServ.findAllEnabledUsers();
		for (User u : users) {
			returnList.addAll(tServ.findUserYearReport(u));
		}
		return returnList;
	}

	public List<UserMonthReportDTO> getUserMonth() {
		List<UserMonthReportDTO> returnList = new ArrayList<>();
		List<User> users = uServ.findAllEnabledUsers();
		for (User u : users) {
			returnList.addAll(tServ.findUserMonthReport(u));
		}
		return returnList;
	}

	public List<SiteTotalReportDTO> getSiteTotals() {
		return tServ.findSiteTotalReport();
	}

	public List<SiteYearReportDTO> getSiteYear() {
		return tServ.findSiteYearReport();
	}

	public List<SiteMonthReportDTO> getSiteMonth() {
		return tServ.findSiteMonthReport();
	}

	public List<TransactionTotalReportDTO> getTransactionTotals() {
		List<TransactionTotalReportDTO> returnList = tServ.findTransactionTotalReport();
		for (TransactionTotalReportDTO dto : returnList) {
			dto.setCategory(tServ.determineCategory(dto.getType()));
		}
		return returnList;
	}

	public List<TransactionYearReportDTO> getTransactionYear() {
		List<TransactionYearReportDTO> returnList = tServ.findTransactionYearReport();
		for (TransactionYearReportDTO dto : returnList) {
			dto.setCategory(tServ.determineCategory(dto.getType()));
		}
		return returnList;
	}

	public List<TransactionMonthReportDTO> getTransactionMonth() {
		List<TransactionMonthReportDTO> returnList = tServ.findTransactionMonthReport();
		for (TransactionMonthReportDTO dto : returnList) {
			dto.setCategory(tServ.determineCategory(dto.getType()));
		}
		return returnList;
	}

	public List<InOutYearReportDTO> getInOutYear() {
		List<InOutYearReportDTO> returnList = new ArrayList<>();
		List<TransactionYearReportDTO> allTrans = getTransactionYear();
		returnList.addAll(getIncomesYear(allTrans));
		returnList.addAll(getExpensesYear(allTrans));
		return returnList;
	}

	private List<InOutYearReportDTO> getIncomesYear(List<TransactionYearReportDTO> in) {
		Map<Integer, Double> yearSumMap = new HashMap<Integer, Double>();
		// Transactions
		for (TransactionYearReportDTO dto : in) {
			if (dto.getCategory().equals("Ulaz")) {
				if (yearSumMap.containsKey(dto.getYear())) {
					Double sum = dto.getTotal();
					sum += yearSumMap.get(dto.getYear());
					yearSumMap.put(dto.getYear(), sum);
				} else {
					yearSumMap.put(dto.getYear(), dto.getTotal());
				}
			}
		}

		for (Integer year : yearSumMap.keySet()) {
			Double oldSum = yearSumMap.get(year);
			oldSum += debServ.getAllBankIncomeForYear(year);
			yearSumMap.put(year, oldSum);
		}

		List<InOutYearReportDTO> returnList = new ArrayList<>();
		for (Integer key : yearSumMap.keySet()) {
			returnList.add(new InOutYearReportDTO("Ulaz", yearSumMap.get(key), key));
		}
		return returnList;
	}

	private List<InOutYearReportDTO> getExpensesYear(List<TransactionYearReportDTO> in) {
		Map<Integer, Double> yearSumMap = new HashMap<Integer, Double>();
		for (TransactionYearReportDTO dto : in) {
			if ((!dto.getCategory().equals("Ulaz"))
					&& (!dto.getType().equals("RAZDUZENJE") && (!dto.getCategory().equals("Privatni")))) {
				if (yearSumMap.containsKey(dto.getYear())) {
					Double sum = dto.getTotal();
					sum += yearSumMap.get(dto.getYear());
					yearSumMap.put(dto.getYear(), sum);
				} else {
					yearSumMap.put(dto.getYear(), dto.getTotal());
				}
			}
		}

		for (Integer year : yearSumMap.keySet()) {
			Double oldSum = yearSumMap.get(year);
			oldSum += debServ.getAllBankExpenseForYear(year);
			oldSum += pServ.getProjectsReportTotalForYear(year).getTotal();
			oldSum += sServ.findBankSumForYear(year);
			yearSumMap.put(year, oldSum);
		}

		List<InOutYearReportDTO> returnList = new ArrayList<>();
		for (Integer key : yearSumMap.keySet()) {
			returnList.add(new InOutYearReportDTO("Izlaz", yearSumMap.get(key), key));
		}
		return returnList;
	}

	public List<ReportingBaseDTO> getInOutTotals() {
		List<ReportingBaseDTO> returnList = new ArrayList<>();
		List<TransactionTotalReportDTO> allTrans = getTransactionTotals();
		returnList.add(getIncomesTotal(allTrans));
		returnList.add(getExpensesTotal(allTrans));
		return returnList;
	}

	public List<ReportingBaseDTO> getInOutTotalSubtotals(String type) {
		List<ReportingBaseDTO> returnList = new ArrayList<>();
		List<TransactionTotalReportDTO> allTrans = getTransactionTotals();
		if (type.equals("Ulaz")) {
			returnList.addAll(getIncomesTotalSubtotals(allTrans));
		} else {
			returnList.addAll(getExpensesTotalSubtotals(allTrans)); // TODO
		}
		return returnList;
	}

	public List<ReportingBaseDTO> getInOutYearSubtotals(String type, Integer year) {
		List<ReportingBaseDTO> returnList = new ArrayList<>();
		List<TransactionYearReportDTO> allTrans = getTransactionYear();
		if (type.equals("Ulaz")) {
			returnList.addAll(getIncomesYearSubtotals(allTrans, year));
		} else {
			returnList.addAll(getExpensesYearSubtotals(allTrans, year));
		}
		return returnList;
	}

	public List<ReportingBaseDTO> getInOutMonthSubtotals(String type, Integer year, Integer month) {
		List<ReportingBaseDTO> returnList = new ArrayList<>();
		List<TransactionMonthReportDTO> allTrans = getTransactionMonth();
		if (type.equals("Ulaz")) {
			returnList.addAll(getIncomesMonthSubtotals(allTrans, year, month));
		} else {
			returnList.addAll(getExpensesMonthSubtotals(allTrans, year, month));
		}
		return returnList;
	}

	private ReportingBaseDTO getIncomesTotal(List<TransactionTotalReportDTO> in) {
		Double sum = 0d;
		for (TransactionTotalReportDTO dto : in) {
			if (dto.getCategory().equals("Ulaz")) {
				sum += dto.getTotal();
			}
		}
		sum += debServ.getAllBankIncome();
		return new ReportingBaseDTO("Ulaz", sum);
	}

	private List<ReportingBaseDTO> getIncomesTotalSubtotals(List<TransactionTotalReportDTO> in) {
		List<ReportingBaseDTO> returnList = new ArrayList<>();
		// Transactions
		for (TransactionTotalReportDTO dto : in) {
			if (dto.getCategory().equals("Ulaz")) {
				returnList.add(new ReportingBaseDTO(dto.getType(), dto.getTotal()));
			}
		}
		// Personal Debts
		returnList.add(new ReportingBaseDTO("Posudbe", debServ.getAllBankIncome()));
		return returnList;
	}

	private List<ReportingBaseDTO> getIncomesYearSubtotals(List<TransactionYearReportDTO> in, Integer year) {
		List<ReportingBaseDTO> returnList = new ArrayList<>();
		// Transactions
		for (TransactionYearReportDTO dto : in) {
			if (dto.getCategory().equals("Ulaz") && dto.getYear().equals(year)) {
				returnList.add(new ReportingBaseDTO(dto.getType(), dto.getTotal()));
			}
		}
		// Personal Debts
		returnList.add(new ReportingBaseDTO("Posudbe", debServ.getAllBankIncomeForYear(year)));
		return returnList;
	}

	private List<ReportingBaseDTO> getIncomesMonthSubtotals(List<TransactionMonthReportDTO> in, Integer year,
			Integer month) {
		List<ReportingBaseDTO> returnList = new ArrayList<>();
		// Transactions
		for (TransactionMonthReportDTO dto : in) {
			if (dto.getCategory().equals("Ulaz") && dto.getYear().equals(year) && dto.getMonth().equals(month)) {
				returnList.add(new ReportingBaseDTO(dto.getType(), dto.getTotal()));
			}
		}
		// Personal Debts
		returnList.add(new ReportingBaseDTO("Posudbe", debServ.getAllBankIncomeForYearAndMonth(year, month)));
		return returnList;
	}

	private ReportingBaseDTO getExpensesTotal(List<TransactionTotalReportDTO> in) {
		Double sum = 0d;
		for (TransactionTotalReportDTO dto : in) {
			if ((!dto.getCategory().equals("Ulaz"))
					&& (!dto.getType().equals("RAZDUZENJE") && (!dto.getCategory().equals("Privatni")))) {
				sum += dto.getTotal();
			}
		}
		// Projects
		sum += pServ.getProjectsReportTotal().getTotal();
		// Personal debts
		sum += debServ.getAllBankExpense();
		// Savings
		sum += sServ.findBankSum();
		return new ReportingBaseDTO("Izlaz", sum);
	}

	private List<ReportingBaseDTO> getExpensesTotalSubtotals(List<TransactionTotalReportDTO> in) {
		List<ReportingBaseDTO> returnList = new ArrayList<>();
		for (TransactionTotalReportDTO dto : in) {
			if ((!dto.getCategory().equals("Ulaz"))
					&& (!dto.getType().equals("RAZDUZENJE") && (!dto.getCategory().equals("Privatni")))) {
				returnList.add(new ReportingBaseDTO(dto.getType(), dto.getTotal()));
			}
		}
		// Projects
		ReportingBaseDTO projekti = new ReportingBaseDTO("Projekti", pServ.getProjectsReportTotal().getTotal());
		returnList.add(projekti);
		// Personal debts
		ReportingBaseDTO dugovanja = new ReportingBaseDTO("Posudbe", debServ.getAllBankExpense());
		returnList.add(dugovanja);
		// Savings
		ReportingBaseDTO stednja = new ReportingBaseDTO("Štednja", sServ.findBankSum());
		returnList.add(stednja);
		return returnList;
	}

	private List<ReportingBaseDTO> getExpensesYearSubtotals(List<TransactionYearReportDTO> in, Integer year) {
		List<ReportingBaseDTO> returnList = new ArrayList<>();
		for (TransactionYearReportDTO dto : in) {
			if ((!dto.getCategory().equals("Ulaz")) && (!dto.getType().equals("RAZDUZENJE")
					&& (!dto.getCategory().equals("Privatni") && (dto.getYear().equals(year))))) {
				returnList.add(new ReportingBaseDTO(dto.getType(), dto.getTotal()));
			}
		}
		// Projects
		ReportingBaseDTO projekti = new ReportingBaseDTO("Projekti",
				pServ.getProjectsReportTotalForYear(year).getTotal());
		returnList.add(projekti);
		// Personal debts
		ReportingBaseDTO dugovanja = new ReportingBaseDTO("Posudbe", debServ.getAllBankExpenseForYear(year));
		returnList.add(dugovanja);
		// Savings
		ReportingBaseDTO stednja = new ReportingBaseDTO("Štednja", sServ.findBankSumForYear(year));
		returnList.add(stednja);
		return returnList;
	}

	private List<ReportingBaseDTO> getExpensesMonthSubtotals(List<TransactionMonthReportDTO> in, Integer year,
			Integer month) {
		List<ReportingBaseDTO> returnList = new ArrayList<>();
		for (TransactionMonthReportDTO dto : in) {
			if ((!dto.getCategory().equals("Ulaz"))
					&& (!dto.getType().equals("RAZDUZENJE") && (!dto.getCategory().equals("Privatni")
							&& (dto.getYear().equals(year) && (dto.getMonth().equals(month)))))) {
				returnList.add(new ReportingBaseDTO(dto.getType(), dto.getTotal()));
			}
		}
		// Projects
		ReportingBaseDTO projekti = new ReportingBaseDTO("Projekti",
				pServ.getProjectsReportTotalForYearMonth(year, month).getTotal());
		returnList.add(projekti);
		// Personal debts
		ReportingBaseDTO dugovanja = new ReportingBaseDTO("Posudbe",
				debServ.getAllBankExpenseForYearMonth(year, month));
		returnList.add(dugovanja);
		// Savings
		ReportingBaseDTO stednja = new ReportingBaseDTO("Štednja", sServ.findBankSumForYearMonth(year, month));
		returnList.add(stednja);
		return returnList;
	}

	public List<InOutMonthReportDTO> getInOutMonth() {
		List<InOutMonthReportDTO> returnList = new ArrayList<>();
		List<TransactionMonthReportDTO> allTrans = getTransactionMonth();
		returnList.addAll(getIncomesMonth(allTrans));
		returnList.addAll(getExpensesMonth(allTrans));
		return returnList;
	}

	private List<InOutMonthReportDTO> getIncomesMonth(List<TransactionMonthReportDTO> in) {
		Map<Pair<Integer, Integer>, Double> yearSumMap = new HashMap<Pair<Integer, Integer>, Double>();
		for (TransactionMonthReportDTO dto : in) {
			if (dto.getCategory().equals("Ulaz")) {
				Pair<Integer, Integer> keyMonthPair = Pair.of(dto.getMonth(), dto.getYear());
				if (yearSumMap.containsKey(keyMonthPair)) {
					Double sum = dto.getTotal();
					sum += yearSumMap.get(keyMonthPair);
					yearSumMap.put(Pair.of(dto.getMonth(), dto.getYear()), sum);
				} else {
					yearSumMap.put(Pair.of(dto.getMonth(), dto.getYear()), dto.getTotal());
				}
			}
		}
		for (Pair<Integer, Integer> key : yearSumMap.keySet()) {
			Double oldSum = yearSumMap.get(key);
			oldSum += debServ.getAllBankIncomeForYearAndMonth(key.getSecond(), key.getFirst());
			yearSumMap.put(Pair.of(key.getFirst(), key.getSecond()), oldSum);
		}

		List<InOutMonthReportDTO> returnList = new ArrayList<>();
		for (Pair<Integer, Integer> key : yearSumMap.keySet()) {
			returnList.add(new InOutMonthReportDTO("Ulaz", yearSumMap.get(key), key.getSecond(), key.getFirst()));
		}
		return returnList;
	}

	private List<InOutMonthReportDTO> getExpensesMonth(List<TransactionMonthReportDTO> in) {
		Map<Pair<Integer, Integer>, Double> yearSumMap = new HashMap<Pair<Integer, Integer>, Double>();
		for (TransactionMonthReportDTO dto : in) {
			if ((!dto.getCategory().equals("Ulaz"))
					&& (!dto.getType().equals("RAZDUZENJE") && (!dto.getCategory().equals("Privatni")))) {
				Pair<Integer, Integer> keyMonthPair = Pair.of(dto.getMonth(), dto.getYear());
				if (yearSumMap.containsKey(keyMonthPair)) {
					Double sum = dto.getTotal();
					sum += yearSumMap.get(keyMonthPair);
					yearSumMap.put(Pair.of(dto.getMonth(), dto.getYear()), sum);
				} else {
					yearSumMap.put(Pair.of(dto.getMonth(), dto.getYear()), dto.getTotal());
				}
			}
		}
		for (Pair<Integer, Integer> key : yearSumMap.keySet()) {
			Double oldSum = yearSumMap.get(key);
			oldSum += debServ.getAllBankExpenseForYearMonth(key.getSecond(), key.getFirst());
			oldSum += pServ.findBankSumForYearMonth(key.getSecond(), key.getFirst());
			oldSum += sServ.findBankSumForYearMonth(key.getSecond(), key.getFirst());
			yearSumMap.put(Pair.of(key.getFirst(), key.getSecond()), oldSum);
		}
		List<InOutMonthReportDTO> returnList = new ArrayList<>();
		for (Pair<Integer, Integer> key : yearSumMap.keySet()) {
			returnList.add(new InOutMonthReportDTO("Izlaz", yearSumMap.get(key), key.getSecond(), key.getFirst()));
		}
		return returnList;
	}

	public List<List<Object>> getIncomePercentageForUsers() {
		return tServ.getIncomePercentageForUsers();
	}

	public List<List<Object>> getExpensePercentageForUsers() {
		return tServ.getExpensePercentageForUsers();
	}

	public List<List<Object>> getInOut() {
		List<List<Object>> returnList = new ArrayList<>();
		for (ReportingBaseDTO dto : getInOutTotals()) {
			if (dto.getType().equals("Ulaz")) {
				List<Object> miniList = new ArrayList<>();
				miniList.add(0, "Ulaz");
				miniList.add(1, dto.getTotal());
				returnList.add(miniList);
			} else {
				List<Object> miniList2 = new ArrayList<>();
				miniList2.add(0, "Izlaz");
				miniList2.add(1, dto.getTotal());
				returnList.add(miniList2);
			}
		}
		return returnList;
	}

	public List<List<Object>> getExpenses() {
		List<List<Object>> returnList = new ArrayList<>();
		for (ReportingBaseDTO dto : tServ.findExpensesTotalReport()) {
			List<Object> miniList = new ArrayList<>();
			miniList.add(0, dto.getType());
			miniList.add(1, dto.getTotal());
			returnList.add(miniList);
		}
		return returnList;
	}

	public List<List<Object>> getInOutYearChartIncome() {
		List<List<Object>> returnList = new ArrayList<>();
		Double lastTotal = 0d;
		for (InOutYearReportDTO dto : getIncomesYear(getTransactionYear())) {
			List<Object> miniList = new ArrayList<>();
			miniList.add(0, dto.getYear().toString());
			miniList.add(1, dto.getTotal());
			if (lastTotal.equals(new Double(0d))) {
				miniList.add(2, 0d);
			} else {
				Double diff;
				diff = (dto.getTotal() - lastTotal) / Math.abs(lastTotal);
				miniList.add(2, diff);
			}
			lastTotal = dto.getTotal();
			returnList.add(miniList);
		}
		return returnList;
	}

	public List<List<Object>> getInOutYearChartExpense() {
		List<List<Object>> returnList = new ArrayList<>();
		Double lastTotal = 0d;
		for (InOutYearReportDTO dto : getExpensesYear(getTransactionYear())) {
			List<Object> miniList = new ArrayList<>();
			miniList.add(0, dto.getYear().toString());
			miniList.add(1, dto.getTotal());
			if (lastTotal.equals(new Double(0d))) {
				miniList.add(2, 0d);
			} else {
				Double diff;
				diff = (dto.getTotal() - lastTotal) / Math.abs(lastTotal);
				miniList.add(2, diff);
			}
			lastTotal = dto.getTotal();
			returnList.add(miniList);
		}
		return returnList;
	}

	public List<List<Object>> getInOutYearChartProfit() {
		List<List<Object>> returnList = new ArrayList<>();
		Map<Integer, Double> profitMap = new HashMap<Integer, Double>();
		for (InOutYearReportDTO incomeDto : getIncomesYear(getTransactionYear())) {
			profitMap.put(incomeDto.getYear(), incomeDto.getTotal());
		}
		for (InOutYearReportDTO expenseDto : getExpensesYear(getTransactionYear())) {
			if (profitMap.containsKey(expenseDto.getYear())) {
				Double sum = profitMap.get(expenseDto.getYear()) - expenseDto.getTotal();
				profitMap.put(expenseDto.getYear(), sum);

			} else {
				profitMap.put(expenseDto.getYear(), expenseDto.getTotal() * -1d);

			}
		}
		List<Integer> sortedKeySet = new ArrayList<>(profitMap.keySet());
		Collections.sort(sortedKeySet);
		Double lastTotal = 0d;
		for (Integer i : sortedKeySet) {
			List<Object> miniList = new ArrayList<>();
			miniList.add(0, i.toString());
			miniList.add(1, profitMap.get(i));
			if (lastTotal.equals(new Double(0d))) {
				miniList.add(2, 0d);
			} else {
				Double diff;
				diff = (profitMap.get(i) - lastTotal) / Math.abs(lastTotal);
				miniList.add(2, diff);
			}
			lastTotal = profitMap.get(i);

			returnList.add(miniList);
		}
		return returnList;
	}
}
