package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public ReportService(TransactionService transactionService, UserService userService) {
		tServ = transactionService;
		uServ = userService;
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

	private ReportingBaseDTO getIncomesTotal(List<TransactionTotalReportDTO> in) {
		Double sum = 0d;
		for (TransactionTotalReportDTO dto : in) {
			if (dto.getCategory().equals("Ulaz")) {
				sum += dto.getTotal();
			}
		}
		return new ReportingBaseDTO("Ulaz", sum);
	}

	private ReportingBaseDTO getExpensesTotal(List<TransactionTotalReportDTO> in) {
		Double sum = 0d;
		for (TransactionTotalReportDTO dto : in) {
			if ((!dto.getCategory().equals("Ulaz"))
					&& (!dto.getType().equals("RAZDUZENJE") && (!dto.getCategory().equals("Privatni")))) {
				sum += dto.getTotal();
			}
		}
		return new ReportingBaseDTO("Izlaz", sum);
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
		List<InOutMonthReportDTO> returnList = new ArrayList<>();
		for (Pair<Integer, Integer> key : yearSumMap.keySet()) {
			returnList.add(new InOutMonthReportDTO("Izlaz", yearSumMap.get(key), key.getSecond(), key.getFirst()));
		}
		return returnList;
	}
}
