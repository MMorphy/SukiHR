package hr.petkovic.iehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.DTO.SavingsDTO;
import hr.petkovic.iehr.entity.Saving;
import hr.petkovic.iehr.service.SavingsService;

@Controller
@RequestMapping("/savings")
public class SavingsController {

	private SavingsService savSer;

	public SavingsController(SavingsService savingsService) {
		this.savSer = savingsService;
	}

	@GetMapping("/")
	public String getSavings(Model model) {
		model.addAttribute("savings", savSer.findAllDisplayDTOS());
		return "savings/list";
	}

	@GetMapping("/list/{id}")
	public String getSavingPayments(@PathVariable("id") Long id, Model model) {
		model.addAttribute("payments", savSer.getAllPaymentsForSavingsId(id));
		model.addAttribute("sign", savSer.getSignForId(id));
		return "savings/paymentList";
	}

	@GetMapping("/add")
	public String getSavingsAdding(Model model) {
		model.addAttribute("addSaving", savSer.getNewSaving());
		return "savings/add";
	}

	@PostMapping("/add")
	public String addSaving(Saving addSaving) {
		savSer.saveSaving(addSaving);
		return "redirect:/savings/";
	}

	@GetMapping("/add/{id}")
	public String getAddToSavings(@PathVariable("id") Long id, Model model) {
		model.addAttribute("addSaving", new SavingsDTO());
		model.addAttribute("id", id);
		return "savings/moneyAdd";
	}

	@PostMapping("/add/{id}")
	public String addToSaving(@PathVariable("id") Long id, SavingsDTO addSaving) {
		Saving sav = savSer.findSavingById(id);
		savSer.increaseSaving(sav, addSaving);
		return "redirect:/savings/";
	}

	@GetMapping("/substract/{id}")
	public String getASubstractFromSavings(@PathVariable("id") Long id, Model model) {
		model.addAttribute("addSaving", new SavingsDTO());
		model.addAttribute("id", id);
		return "savings/moneySubstract";
	}

	@PostMapping("/substract/{id}")
	public String substractFromSaving(@PathVariable("id") Long id, SavingsDTO addSaving) {
		Saving sav = savSer.findSavingById(id);
		savSer.decreaseSaving(sav, addSaving);
		return "redirect:/savings/";
	}
}
