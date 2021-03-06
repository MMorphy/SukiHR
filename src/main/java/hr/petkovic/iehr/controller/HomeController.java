package hr.petkovic.iehr.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.service.UserService;

@Controller
@RequestMapping("")
public class HomeController {

	Logger logger = LoggerFactory.getLogger(HomeController.class);

	UserService userSer;

	public HomeController(UserService userService) {
		userSer = userService;
	}

	@GetMapping
	public String getIndex(HttpSession session) {
		session.setAttribute("saldo", userSer.getSaldoForLoggedInUser());
		return "index";
	}

}
