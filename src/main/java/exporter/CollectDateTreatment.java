package exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;

import extraction.Extractor;

class CollectDateTreatment extends AbstractTreatment {

	@Override
	String get(List<BasicDBObject> list, Variables variable) {
		String value = getValue(list, Variables.collect_date);
		boolean unknownCollectDate = value.isEmpty();
		if (unknownCollectDate) {
			value = getValue(list, Variables.collect_date2);
		}
		SimpleDateFormat inputFormat = new SimpleDateFormat(Extractor.DATE_FORMAT);
		Date date = null;
		try {
			date = inputFormat.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (unknownCollectDate) {
			date = removeOneDay(date);
		}

		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = outputFormat.format(date);

		return formatted;
	}

	/**
	 * La date de prélèvement correspond généralement à la veille de la date de traitement
	 */
	private Date removeOneDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		date = cal.getTime();
		return date;
	}

}
