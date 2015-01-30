package exporter;

import java.util.List;

import com.mongodb.BasicDBObject;

class BirthNameTreatment extends AbstractTreatment {

	@Override
	String get(List<BasicDBObject> list, Variables variable) {
		String value = getValue(list, Variables.birthName);
		if (value.isEmpty()) {
			value = getValue(list, Variables.useName);
		}
		return value;
	}

}
