package hr.petkovic.iehr.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import hr.petkovic.iehr.service.TransactionService;

@Component
public class FixedExpenseTask {

	private static final Logger log = LoggerFactory.getLogger(PayTask.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	private TransactionService tServ;

	// @Scheduled(cron = "*/30 * * * * *") //--Debugging schedule every 30 secs
	@Scheduled(cron = "0 0 5 1 * *") // Every first of the month at 5am server time
	public void generateFixedExpenses() {
		log.info("Starting fixed expense generation: ", dateFormat.format(new Date()));
		tServ.generateFixedExpenses();
		log.info("Ending fixed expense generation: ", dateFormat.format(new Date()));
	}
}
