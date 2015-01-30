package exporter;

import java.util.List;

import com.mongodb.BasicDBObject;

public class StatusTreatment extends AbstractTreatment {

	@Override
	String get(List<BasicDBObject> list, Variables variable) {
		String value = getValue(list, Variables.status_sample);
		if (value.equals("A")) {
			return "A";
		} else if (value.equals("I")) {
			return "IS";
		}
		return "NA";
	}

}
