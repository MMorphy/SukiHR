package hr.petkovic.iehr.DTO;

public class ProjectDatabaseDTO {

	private Long id;

	private Double total;

	public ProjectDatabaseDTO() {
	}

	public ProjectDatabaseDTO(Long id, Double total) {
		this.id = id;
		this.total = total;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

}
