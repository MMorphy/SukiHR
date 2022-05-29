package hr.petkovic.iehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.service.BankService;
import hr.petkovic.iehr.service.PersonalDebtService;
import hr.petkovic.iehr.service.ProjectService;
import hr.petkovic.iehr.service.SavingsService;

@Controller
@RequestMapping("/bank")
public class BankController {

	BankService bankServ;
	PersonalDebtService debtServ;
	SavingsService saveServ;
	ProjectService projServ;

	public BankController(BankService bService, PersonalDebtService dService, SavingsService savingsService,
			ProjectService projectService) {
		bankServ = bService;
		debtServ = dService;
		saveServ = savingsService;
		projServ = projectService;
	}

	@GetMapping("/")
	public String getBankHome(Model model) {
		model.addAttribute("sum", bankServ.getSum());
		model.addAttribute("transactions", bankServ.getAllBankTransactions());
		model.addAttribute("projects", projServ.getAllProjects());
		model.addAttribute("savings", saveServ.findAllSavings());
		model.addAttribute("debts", debtServ.getActivePersonalDebts());

		return "bank/list";
	}
}
