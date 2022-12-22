package hr.petkovic.iehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.service.ReportService;

@Controller
@RequestMapping("/report/chart")
public class ChartController {

	private ReportService reportServ;

	public ChartController(ReportService reportService) {
		reportServ = reportService;
	}

	@GetMapping("/user/income")
	public String getUserGraph(Model model) {
		model.addAttribute("userIncome", reportServ.getIncomePercentageForUsers());
		model.addAttribute("userExpense", reportServ.getExpensePercentageForUsers());
		return "report/chart/user";
	}

	@GetMapping("/inout")
	public String getInOutGraph(Model model) {
		model.addAttribute("inOut", reportServ.getInOut());
		model.addAttribute("expenses", reportServ.getExpenses());
		return "report/chart/inout";
	}
}
