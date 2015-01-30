package exporter;

import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;

class GenderTreatment extends AbstractTreatment {

	@Override
	String get(List<BasicDBObject> list, Variables variable) {
		List<String> asList = Arrays.asList("M", "F", "H", "U");
		String value = getValue(list, Variables.gender);
		if (!asList.contains(value)) {
			value = "U";
		}
		return value;
	}

}
