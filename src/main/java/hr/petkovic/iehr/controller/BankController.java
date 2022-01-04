package hr.petkovic.iehr.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.entity.Transaction;
import hr.petkovic.iehr.service.BankService;
import hr.petkovic.iehr.service.PersonalDebtService;

@Controller
@RequestMapping("/bank")
public class BankController {

	BankService bankServ;
	PersonalDebtService debtServ;

	public BankController(BankService bService, PersonalDebtService dService) {
		bankServ = bService;
		debtServ = dService;
	}

	@GetMapping("/")
	public String getBankHome(Model model) {
		List<Transaction> list = bankServ.findAllBankTransactions();
		list.addAll(bankServ.findBankUserTransacations());
		Set<Transaction> set = new HashSet<>(list);
		model.addAttribute("transactions", set);
		model.addAttribute("sum", bankServ.getSum(set));
		model.addAttribute("debts", debtServ.getActivePersonalDebts());
		return "bank/list";
	}
}
