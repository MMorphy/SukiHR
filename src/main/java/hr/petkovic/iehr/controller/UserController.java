package hr.petkovic.iehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.service.TransactionService;

@Controller
@RequestMapping("/user")
public class UserController {

	private TransactionService tServ;

	public UserController(TransactionService transService) {
		tServ = transService;
	}

	@GetMapping("/")
	public String getAllUserList(Model model) {
		model.addAttribute("users", tServ.updateSaldoForUserDTOs(tServ.findAllUsersAndDebt()));
		return "user/list";
	}
}
