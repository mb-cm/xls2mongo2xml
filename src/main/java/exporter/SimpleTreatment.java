package exporter;

import java.util.List;

import com.mongodb.BasicDBObject;

class SimpleTreatment extends AbstractTreatment {

	@Override
	String get(List<BasicDBObject> list, Variables variable) {
		return getValue(list, variable);
	}

}

