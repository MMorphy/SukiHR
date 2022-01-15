package hr.petkovic.iehr.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.entity.Transaction;
import hr.petkovic.iehr.service.BankService;
import hr.petkovic.iehr.service.DebtService;
import hr.petkovic.iehr.service.TransactionService;

@Controller
@RequestMapping("")
public class HomeController {

	Logger logger = LoggerFactory.getLogger(HomeController.class);

	private TransactionService tServ;
	private DebtService debtSer;
	private BankService bankServ;

	public HomeController(TransactionService transService, DebtService debtService, BankService bankService) {
		debtSer = debtService;
		tServ = transService;
		bankServ = bankService;
	}

	@GetMapping
	public String getIndex(HttpSession session) {
		List<Transaction> list = bankServ.findAllBankTransactions();
		list.addAll(bankServ.findBankUserTransacations());
		Set<Transaction> set = new HashSet<>(list);

		if (!bankServ.isAdmin(SecurityContextHolder.getContext().getAuthentication().getName())) {
			set = bankServ.filterOutOldYear(set);
			session.setAttribute("sum", bankServ.getSumFiltered(set));
		} else if (bankServ.isAdmin(SecurityContextHolder.getContext().getAuthentication().getName())
				|| bankServ.isBank(SecurityContextHolder.getContext().getAuthentication().getName())) {
			session.setAttribute("sum", bankServ.getSumUnfiltered(set));
		}
		session.setAttribute("saldo", tServ.getSaldoForLoggedInUser());
		session.setAttribute("dugovanja", debtSer.getDebtsForLoggedInUser());
		return "index";
	}

}
