package hr.petkovic.iehr.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Component;

@Component
public class TimeUtil {

	public Date getCurrentYearBreakpointDate() {
		Calendar c = Calendar.getInstance();
		c.set(2022, 11, 31);
		return c.getTime();
	}

	public Date getStartPointYearBreakpointDate() {
		Calendar c = Calendar.getInstance();
		c.set(2021, 11, 31);
		return c.getTime();
	}

	public Date getPayStartDateBoris() {
		Date d = new GregorianCalendar(2023, Calendar.JANUARY, 01).getTime();
		return d;
	}

	public Date getPayStartDateTino() {
		Date d = new GregorianCalendar(2022, Calendar.FEBRUARY, 01).getTime();
		return d;
	}
	public Date getPayExpenseStartDateTino() {

		Date d = new GregorianCalendar(2022, Calendar.FEBRUARY, 15).getTime();
		return d;
	}
}
