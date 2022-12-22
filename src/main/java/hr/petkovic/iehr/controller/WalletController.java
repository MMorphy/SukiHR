package hr.petkovic.iehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.service.TransactionService;

@Controller
@RequestMapping("/wallet")
public class WalletController {

	private TransactionService transServ;

	public WalletController(TransactionService transactionService) {
		transServ = transactionService;
	}

	@GetMapping("/")
	public String getWallet(Model model) {
		model.addAttribute("transactions", transServ.getWalletTransactions());
		return "transaction/wallet";
	}
}
