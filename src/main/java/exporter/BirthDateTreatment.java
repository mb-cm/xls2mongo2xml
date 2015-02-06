package exporter;

import injection.Extractor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;


class BirthDateTreatment extends AbstractTreatment {

	@Override
	String get(List<BasicDBObject> list, Variables variable) {
		String value = getValue(list, Variables.birthDate);

		try {
			Date date = new SimpleDateFormat(Extractor.DATE_FORMAT).parse(value);
			String formatted = new SimpleDateFormat(XMLExporter.YYYY_MM_DD).format(date);
			return formatted;
		} catch (ParseException e) {
			return "";
		}
	}

}
