package hr.petkovic.iehr.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.DTO.TransactionDebtDTO;
import hr.petkovic.iehr.entity.Transaction;
import hr.petkovic.iehr.entity.TransactionType;
import hr.petkovic.iehr.service.SiteService;
import hr.petkovic.iehr.service.TransactionService;
import hr.petkovic.iehr.service.TransactionTypeService;
import hr.petkovic.iehr.service.UserService;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
	Logger logger = LoggerFactory.getLogger(UserService.class);

	private SiteService siteSer;
	private TransactionService transSer;
	private TransactionTypeService typeSer;

	public TransactionController(SiteService siteService, TransactionService transService,
			TransactionTypeService typeService) {
		siteSer = siteService;
		transSer = transService;
		typeSer = typeService;
	}

	@GetMapping("/income/add")
	public String getIncomeAdding(Model model) {
		model.addAttribute("addTrans", new TransactionDebtDTO());
		model.addAttribute("sites", siteSer.findAllSites());
		return "transaction/income";
	}

	@PostMapping("/income/add")
	public String addIncome(TransactionDebtDTO addTrans) {
		transSer.saveIncomeWithLoggedInUserAndAddDebtRepay(addTrans.getTrans(), addTrans.getDebtRepay());
		return "redirect:/";
	}

	@GetMapping("/expense/add")
	public String getExpenseAdding(Model model) {
		model.addAttribute("addTrans", new Transaction());
		List<TransactionType> types = typeSer.getAllExpenseTypes();
		if (types.isEmpty()) {
			logger.error("No expense types when adding expenses. Redirect to home page!");
			return "redirect:/";
		} else {
			model.addAttribute("types", types);
		}
		return "transaction/expense";
	}

	@PostMapping("/expense/add")
	public String addExpense(Transaction addTrans) {
		transSer.saveExpenseWithLoggedInUser(addTrans);
		return "redirect:/";
	}

	@GetMapping(value = { "/", "/{username}" })
	public String getAllTransactions(@PathVariable(required = false) String username, Model model) {
		if (username == null || username.isEmpty()) {
			model.addAttribute("transactions", transSer.findAllTransactions());
		} else {
			model.addAttribute("transactions", transSer.findAllTransactionsForUsername(username));
		}
		return "transaction/list";
	}

	//TODO edit transakcija
	
	
	
	@PostMapping("/delete/{id}")
	public String deleteTransaction(@PathVariable("id") Long id) {
		transSer.deleteTransById(id);
		return "redirect:/";
	}
}
