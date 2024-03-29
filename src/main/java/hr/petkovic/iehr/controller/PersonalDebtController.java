package hr.petkovic.iehr.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.DTO.PersonalDebtUIDTO;
import hr.petkovic.iehr.entity.PersonalDebt;
import hr.petkovic.iehr.entity.PersonalDebtPayments;
import hr.petkovic.iehr.service.PersonalDebtService;

@Controller
@RequestMapping("/personal_debt")
public class PersonalDebtController {

	PersonalDebtService debtServ;

	public PersonalDebtController(PersonalDebtService debtService) {
		debtServ = debtService;
	}

	@GetMapping("/")
	public String getPersonalDebtHome(Model model) {
		List<PersonalDebtUIDTO> in = debtServ.getActivePersonalDebtsToMe();
		model.addAttribute("in", in);
		model.addAttribute("inTotal", debtServ.getTotalAgreed(in));
		model.addAttribute("inPaid", debtServ.getTotalPaid(in));

		List<PersonalDebtUIDTO> out = debtServ.getMyActivePersonalDebts();
		model.addAttribute("out", out);
		model.addAttribute("outTotal", debtServ.getTotalAgreed(out));
		model.addAttribute("outPaid", debtServ.getTotalPaid(out));

		List<PersonalDebtUIDTO> debts = Stream.concat(in.stream(), out.stream()).collect(Collectors.toList());
		model.addAttribute("debts", debts);
		return "personalDebt/list";
	}

	@GetMapping("/inactive")
	public String getResolvedPersonalDebts(Model model) {
		List<PersonalDebtUIDTO> debs = new ArrayList<PersonalDebtUIDTO>();
		debs.addAll(debtServ.getResolvedPersonalDebtsToMe());
		debs.addAll(debtServ.getMyResolvedPersonalDebts());
		model.addAttribute("debts", debs);
		return "personalDebt/inactiveList";
	}
	@GetMapping("/mydebt/add")
	public String getMyPersonalDebtAdding(Model model) {
		model.addAttribute("addDebt", debtServ.getNewMyPersonalDebt());
		model.addAttribute("types", debtServ.getPersonalDebtTypes());
		return "personalDebt/addMyDebt";
	}

	@PostMapping("/mydebt/add")
	public String addMyPersonalDebt(PersonalDebt addDebt) {
		addDebt = debtServ.setMyDebt(addDebt);
		debtServ.saveDebt(addDebt);
		return "redirect:/personal_debt/";

	}
	@GetMapping("/debttome/add")
	public String getPersonalDebtToMeAdding(Model model) {
		model.addAttribute("addDebt", debtServ.getNewPersonalDebtToMe());
		model.addAttribute("types", debtServ.getPersonalDebtTypes());
		return "personalDebt/addDebtToMe";
	}

	@PostMapping("/debttome/add")
	public String addPersonalDebtToMe(PersonalDebt addDebt) {
		addDebt = debtServ.setDebtTome(addDebt);
		debtServ.saveDebt(addDebt);
		return "redirect:/personal_debt/";

	}
	@PostMapping("/delete/{id}")
	public String deletePersonalDebt(@PathVariable("id") Long id) {
		debtServ.deletePersonalDebtById(id);
		return "redirect:/personal_debt/";
	}

	@GetMapping("/edit/{id}")
	public String getPersonalDebtEditing(@PathVariable("id") Long id, Model model) {
		model.addAttribute("editDebt", debtServ.findById(id));
		return "personalDebt/edit";
	}

	@PostMapping("/edit/{id}")
	public String editPersonalDebt(@PathVariable("id") Long id, PersonalDebt editDebt) {
		debtServ.editDebt(id, editDebt);
		return "redirect:/personal_debt/";
	}

	@GetMapping("/payment/{id}")
	public String getPersonalDebtPayments(@PathVariable("id") Long id, Model model) {
		model.addAttribute("payments", debtServ.getPaymentsForId(id));
		model.addAttribute("pay", debtServ.findById(id));
		model.addAttribute("outstanding", debtServ.getOutstandingDebtForId(id));
		return "personalDebt/paymentList";
	}

	@GetMapping("/payment/add/{id}")
	public String getPaymentAdding(@PathVariable("id") Long id, Model model) {
		model.addAttribute("addPayment", debtServ.getNewPayment());
		model.addAttribute("id", id);
		return "personalDebt/paymentAdd";
	}

	@PostMapping("/payment/add/{id}")
	public String addPayment(@PathVariable("id") Long id, PersonalDebtPayments addPayment) {
		debtServ.addPayment(id, addPayment);
		return "redirect:/personal_debt/";
	}

	@GetMapping("/payment/edit/{id}")
	public String getPaymentEditing(@PathVariable("id") Long id, Model model) {
		model.addAttribute("editPayment", debtServ.findPaymentById(id));
		return "personalDebt/paymentEdit";

	}

	@PostMapping("/payment/edit/{id}")
	public String editPayment(@PathVariable("id") Long id, PersonalDebtPayments editPayment) {
		debtServ.editPayment(id, editPayment);
		return "redirect:/personal_debt/";
	}

	@PostMapping("/payment/delete/{id}")
	public String deletePayment(@PathVariable("id") Long id) {
		debtServ.deletePayment(id);
		return "redirect:/personal_debt/";

	}
}
