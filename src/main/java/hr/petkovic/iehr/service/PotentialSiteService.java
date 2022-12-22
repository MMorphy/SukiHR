package hr.petkovic.iehr.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.PotentialSite;
import hr.petkovic.iehr.entity.PotentialSiteState;
import hr.petkovic.iehr.repo.PotentialSiteRepo;
import hr.petkovic.iehr.repo.PotentialSiteStateRepo;

@Service
public class PotentialSiteService {

	private PotentialSiteRepo siteRepo;
	private PotentialSiteStateRepo stateRepo;
	private UserService uServ;

	public PotentialSiteService(PotentialSiteRepo potentialSiteRepository,
			PotentialSiteStateRepo potentialSiteStateRepository, UserService userService) {
		siteRepo = potentialSiteRepository;
		stateRepo = potentialSiteStateRepository;
		uServ = userService;
	}

	public List<PotentialSite> getAllActivePotentialSites() {
		return siteRepo.findAllByState_nameNot("NEZAINTERESIRAN");
	}

	public List<PotentialSite> getAllInactivePotentialSites() {
		return siteRepo.findAllByState_name("NEZAINTERESIRAN");
	}

	public PotentialSite getNewPotentialSite() {
		return new PotentialSite();
	}

	public List<PotentialSiteState> getStates() {
		return stateRepo.findAll();
	}

	public PotentialSite savePotentialSite(PotentialSite addPotentialSite) {
		addPotentialSite
				.setUser(uServ.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
		return siteRepo.save(addPotentialSite);
	}

	public PotentialSite findById(Long id) {
		try {
			return siteRepo.findById(id).get();
		} catch (Exception ex) {
			return null;
		}
	}

	public PotentialSite editPotentialSite(Long id, PotentialSite eds) {
		PotentialSite site = findById(id);
		site.setAddress(eds.getAddress());
		site.setContact(eds.getContact());
		site.setDescription(eds.getDescription());
		site.setEvaluation(eds.getEvaluation());
		site.setName(eds.getName());
		site.setOwner(eds.getOwner());
		site.setState(eds.getState());
		return savePotentialSite(site);
	}

	public void deletePotentialSite(Long id) {
		siteRepo.deleteById(id);
	}

	public List<PotentialSite> getAllActivePotentialSitesForUsername(String username) {
		return siteRepo.findAllByState_nameNotAndUser_username("NEZAINTERESIRAN", username);

	}

	public List<PotentialSite> getAllInactivePotentialSitesForUsername(String username) {
		return siteRepo.findAllByState_nameAndUser_username("NEZAINTERESIRAN", username);

	}
}
