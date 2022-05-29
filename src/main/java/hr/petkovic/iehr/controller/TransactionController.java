package hr.petkovic.iehr.controller;

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
		if (transSer.isAdmin(username)) {
			model.addAttribute("sites", transSer.makeFullSiteListActive(siteSer.findAllSitesByUsernameRole(username),
					transSer.findAllSitesWithDebt()));
		} else {
			model.addAttribute("sites", transSer.makeFullSiteListActive(siteSer.findAllSitesByUsernameRole(username),
					transSer.findAllSitesWithDebtForUsername(username)));
		}
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
		//Admin user ivans
		if (transSer.isAdmin(SecurityContextHolder.getContext().getAuthentication().getName())) {
			model.addAttribute("private", typeSer.getPrivateExpenseTypes());
			model.addAttribute("operative", typeSer.getOperativeExpenseTypes());
		}
		//Bank user banka
		else if(transSer.isOnlyBank(SecurityContextHolder.getContext().getAuthentication().getName())) {
			model.addAttribute("operative", typeSer.getOperativeExpenseTypes());
			model.addAttribute("business", typeSer.getBusinessExpenseTypes());
		} 
		//Operative users
		else {
			model.addAttribute("operative", typeSer.getOperativeExpenseTypes());
		}
		model.addAttribute("addTrans", new Transaction());
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
	public String getTransactionEditing(@PathVariable Long id, Model model) {
		Transaction trans = transSer.findTransactionById(id);
		model.addAttribute("editTrans", trans);
		// Expenses
		if (trans.getSite() == null) {
			//Admin user ivans
			if (transSer.isAdmin(SecurityContextHolder.getContext().getAuthentication().getName())) {
				model.addAttribute("private", typeSer.getPrivateExpenseTypes());
				model.addAttribute("operative", typeSer.getOperativeExpenseTypes());
			}
			//Bank user banka
			else if(transSer.isOnlyBank(SecurityContextHolder.getContext().getAuthentication().getName())) {
				model.addAttribute("operative", typeSer.getOperativeExpenseTypes());
				model.addAttribute("business", typeSer.getBusinessExpenseTypes());
			} 
			//Operative users
			else {
				model.addAttribute("operative", typeSer.getOperativeExpenseTypes());
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
			model.addAttribute("transactions", transSer.findNonNullTransactions());
		} else {
			model.addAttribute("transactions", transSer.findNonNullTransactionsForUsername(username));
		}
		return "transaction/list";
	}

	@PostMapping("/delete/{id}")
	public String deleteTransaction(@PathVariable("id") Long id) {
		transSer.deleteTransById(id);
		return "redirect:/";
	}

	@GetMapping("/bank/income/add")
	public String getBankIncomeAdding(Model model) {
		model.addAttribute("addTrans", new Transaction());
		return "transaction/bankIncome";
	}

	@PostMapping("/bank/income/add")
	public String addBankIncome(Transaction addTrans) {
		transSer.addBankIncome(addTrans);
		return "redirect:/";
	}

	@GetMapping("/bank/edit/{id}")
	public String getBankIncomeEdit(@PathVariable Long id, Model model) {
		Transaction trans = transSer.findTransactionById(id);
		model.addAttribute("editTrans", trans);
		return "transaction/bankIncomeEdit";

	}

	@PostMapping("/bank/income/edit/{id}")
	public String editBankIncome(@PathVariable Long id, Transaction editTrans) {
		transSer.editBankIncome(id, editTrans);
		return "redirect:/";
	}

	@GetMapping("/admin/income/add")
	public String getAdminIncomeAdding(Model model) {
		model.addAttribute("addTrans", new Transaction());
		return "transaction/adminIncome";
	}

	@PostMapping("/admin/income/add")
	public String addAdminIncome(Transaction addTrans) {
		transSer.addAdminIncome(addTrans);
		return "redirect:/";
	}

	@GetMapping("/admin/edit/{id}")
	public String getAdminIncomeEdit(@PathVariable Long id, Model model) {
		Transaction trans = transSer.findTransactionById(id);
		model.addAttribute("editTrans", trans);
		return "transaction/adminIncomeEdit";

	}

	@PostMapping("/admin/income/edit/{id}")
	public String editAdminIncome(@PathVariable Long id, Transaction editTrans) {
		transSer.editAdminIncome(id, editTrans);
		return "redirect:/";
	}
}
