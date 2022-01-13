package hr.petkovic.iehr.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class TimeUtil {

	public Date getCurrentYearBreakpointDate() {
		Calendar c = Calendar.getInstance();
		c.set(2021, 11, 31);;
		return c.getTime();
	}

	public Date getStartPointYearBreakpointDate() {
		Calendar c = Calendar.getInstance();
		c.set(2021, 11, 31);;
		return c.getTime();
	}
}
