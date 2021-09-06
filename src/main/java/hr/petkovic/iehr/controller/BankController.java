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

@Controller
@RequestMapping("/bank")
public class BankController {

	BankService bankServ;

	public BankController(BankService bService) {
		bankServ = bService;
	}

	@GetMapping("/")
	public String getBankHome(Model model) {
		List<Transaction> list = bankServ.findAllBankTransactions();
		Set<Transaction> set = new HashSet<>(list);
		model.addAttribute("transactions", set);
		model.addAttribute("sum", bankServ.getSum(set));
		return "bank/list";
	}
}
