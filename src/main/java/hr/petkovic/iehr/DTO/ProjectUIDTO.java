package hr.petkovic.iehr.DTO;

import hr.petkovic.iehr.entity.Project;

public class ProjectUIDTO {

	private Project project;

	private Double total;

	public ProjectUIDTO() {
	}

	public ProjectUIDTO(Project project, Double total) {
		super();
		this.project = project;
		this.total = total;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

}
