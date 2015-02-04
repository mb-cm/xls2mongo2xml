package exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;

import extraction.Extractor;

class BirthDateTreatment extends AbstractTreatment {

	@Override
	String get(List<BasicDBObject> list, Variables variable) {
		String value = getValue(list, Variables.birthDate);

		try {
			Date date = new SimpleDateFormat(Extractor.DATE_FORMAT).parse(value);
			String formatted = new SimpleDateFormat("yyyy-MM-dd").format(date);
			return formatted;
		} catch (ParseException e) {
			return "";
		}
	}

}
