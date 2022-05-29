package hr.petkovic.iehr.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		session.setAttribute("sum", bankServ.getSum());
		session.setAttribute("saldo", tServ.getSaldoForLoggedInUser());
		session.setAttribute("dugovanja", debtSer.getDebtsForLoggedInUser());
		session.setAttribute("wallet", tServ.getAdminWallet());
		return "index";
	}

}
