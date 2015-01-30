package exporter;

import java.util.List;

import com.mongodb.BasicDBObject;

public class QuantityTreatment extends AbstractTreatment {

	@Override
	String get(List<BasicDBObject> list, Variables variable) {
		String volume = getValue(list, Variables.quantity_available);
		String concentration = getValue(list, Variables.quantity_available2);
		String value = volume + " ul";
		if (!concentration.isEmpty()) {
			value += ", " + concentration + " ng/ul";
		}
		return value;
	}

}
