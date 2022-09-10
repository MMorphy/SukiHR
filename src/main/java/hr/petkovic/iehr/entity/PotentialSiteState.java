package hr.petkovic.iehr.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "potential_site_states")
public class PotentialSiteState {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name = "";

	@OneToMany(mappedBy = "state")
	private List<PotentialSite> potentialSites = new ArrayList<PotentialSite>();

	public PotentialSiteState() {
	}

	public PotentialSiteState(Long id, String name, List<PotentialSite> potentialSites) {
		super();
		this.id = id;
		this.name = name;
		this.potentialSites = potentialSites;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PotentialSite> getPotentialSites() {
		return potentialSites;
	}

	public void setPotentialSites(List<PotentialSite> potentialSites) {
		this.potentialSites = potentialSites;
	}
}
