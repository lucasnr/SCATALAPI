package br.edu.ifrn.scatalapi.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CalendarUtil {

	public static Date now() {
		return Calendar.getInstance(TimeZone.getTimeZone("America/Fortaleza")).getTime();
	}
}
