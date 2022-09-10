package hr.petkovic.iehr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages= {"hr.petkovic.iehr.entity"})
@EnableJpaRepositories(basePackages= {"hr.petkovic.iehr.repo"})
@EnableScheduling
public class IncomeExpenseHrApplication {

	public static void main(String[] args) {
		SpringApplication.run(IncomeExpenseHrApplication.class, args);
	}

}
