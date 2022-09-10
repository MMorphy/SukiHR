package hr.petkovic.iehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.service.ReportService;

@Controller
@RequestMapping("/report")
public class ReportController {

	ReportService reportServ;

	public ReportController(ReportService reportService) {
		this.reportServ = reportService;
	}

	@GetMapping("/")
	public String getReportHome() {
		return "report/home";
	}

	@GetMapping("/total")
	public String getTotalList(Model model) {
		model.addAttribute("userTotal", reportServ.getUserTotals());
		model.addAttribute("siteTotal", reportServ.getSiteTotals());
		model.addAttribute("transTotal", reportServ.getTransactionTotals());
		model.addAttribute("inOutTotal", reportServ.getInOutTotals());
		return "report/total";
	}

	@GetMapping("/year")
	public String getYearList(Model model) {
		model.addAttribute("userYear", reportServ.getUserYear());
		model.addAttribute("siteYear", reportServ.getSiteYear());
		model.addAttribute("transYear", reportServ.getTransactionYear());
		model.addAttribute("inOutYear", reportServ.getInOutYear());
		return "report/year";
	}

	@GetMapping("/month")
	public String getMonthList(Model model) {
		model.addAttribute("userMonth", reportServ.getUserMonth());
		model.addAttribute("siteMonth", reportServ.getSiteMonth());
		model.addAttribute("transMonth", reportServ.getTransactionMonth());
		model.addAttribute("inOutMonth", reportServ.getInOutMonth());
		return "report/month";
	}
}
