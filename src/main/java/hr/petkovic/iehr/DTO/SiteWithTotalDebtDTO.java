package hr.petkovic.iehr.DTO;

import hr.petkovic.iehr.entity.Site;

public class SiteWithTotalDebtDTO {

	Site site;

	Double debtTotal;

	public SiteWithTotalDebtDTO(Site site, Double debtTotal) {
		super();
		this.site = site;
		this.debtTotal = debtTotal;
	}

	public SiteWithTotalDebtDTO() {
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Double getDebtTotal() {
		return debtTotal;
	}

	public void setDebtTotal(Double debtTotal) {
		this.debtTotal = debtTotal;
	}

}
