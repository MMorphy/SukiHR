package hr.petkovic.iehr.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.entity.Transaction;
import hr.petkovic.iehr.entity.TransactionType;
import hr.petkovic.iehr.service.SiteService;
import hr.petkovic.iehr.service.TransactionService;
import hr.petkovic.iehr.service.TransactionTypeService;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
	Logger logger = LoggerFactory.getLogger(TransactionController.class);

	private TransactionService transSer;
	private TransactionTypeService typeSer;
	private SiteService siteSer;

	public TransactionController(TransactionService transService, TransactionTypeService typeService,
			SiteService siteService) {
		transSer = transService;
		typeSer = typeService;
		siteSer = siteService;
	}

	@GetMapping("/income/add")
	public String getIncomeAdding(Model model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("addTrans", new Transaction());
		model.addAttribute("sites", transSer.makeFullSiteListActive(siteSer.findAllSitesByUsernameRole(username),
				transSer.findAllSitesWithDebtForUsername(username)));
		return "transaction/income";
	}

	@PostMapping("/income/add")
	public String addIncome(Transaction addTrans) {
		transSer.saveIncomeWithLoggedInUserAndAddDebtRepay(addTrans, addTrans.getDebt());
		return "redirect:/";
	}

	@PostMapping("/income/edit/{id}")
	public String editIncome(@PathVariable Long id, Transaction editTrans) {
		transSer.editTransaction(id, editTrans);
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

	@PostMapping("/expense/edit/{id}")
	public String editExpense(@PathVariable Long id, Transaction editTrans) {
		transSer.editTransaction(id, editTrans);
		return "redirect:/";
	}

	@GetMapping("/edit/{id}")
	public String getExpenseEditing(@PathVariable Long id, Model model) {
		Transaction trans = transSer.findTransactionById(id);
		model.addAttribute("editTrans", trans);
		// Expenses
		if (trans.getSite() == null) {
			List<TransactionType> types = typeSer.getAllExpenseTypes();
			if (types.isEmpty()) {
				logger.error("No expense types when adding expenses. Redirect to home page!");
				return "redirect:/";
			} else {
				model.addAttribute("types", types);
			}
			return "transaction/expenseEdit";
			// Incomes
		} else {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			model.addAttribute("sites", transSer.makeFullSiteListActive(siteSer.findAllSitesByUsernameRole(username),
					transSer.findAllSitesWithDebtForUsername(username)));
			return "transaction/incomeEdit";
		}
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

	@PostMapping("/delete/{id}")
	public String deleteTransaction(@PathVariable("id") Long id) {
		transSer.deleteTransById(id);
		return "redirect:/";
	}
}
