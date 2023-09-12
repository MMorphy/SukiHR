package hr.petkovic.iehr.DTO.report;

public class InOutYearDeltaChartDTO {
	Integer year;
	String type;
	Double delta;

	public InOutYearDeltaChartDTO(Integer year, String type, Double delta) {
		super();
		this.year = year;
		this.type = type;
		this.delta = delta;
	}

	public InOutYearDeltaChartDTO() {
		super();
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getDelta() {
		return delta;
	}

	public void setDelta(Double delta) {
		this.delta = delta;
	}

}
