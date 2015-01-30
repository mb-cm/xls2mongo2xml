package exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.mongodb.BasicDBObject;

class BirthDateTreatment extends AbstractTreatment {

	@Override
	String get(List<BasicDBObject> list, Variables variable) {
		String value = getValue(list, Variables.birthDate);

		try {
			new SimpleDateFormat("dd/MM/yyyy").parse(value);
		} catch (ParseException e) {
			return "";
		}

		return value;
	}

}
