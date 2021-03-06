package hr.petkovic.iehr.DTO;

import hr.petkovic.iehr.entity.User;

public class UserWithTotalDebtDTO {

	User user;

	Double totalDebt;

	public UserWithTotalDebtDTO() {
	}

	public UserWithTotalDebtDTO(User user, Double totalDebt) {
		super();
		this.user = user;
		this.totalDebt = totalDebt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Double getTotalDebt() {
		return totalDebt;
	}

	public void setTotalDebt(Double totalDebt) {
		this.totalDebt = totalDebt;
	}
}
