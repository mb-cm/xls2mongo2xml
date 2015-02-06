package exporter;

import java.util.List;

import com.mongodb.BasicDBObject;

class ConsentTreatment extends AbstractTreatment {

	@Override
	String get(List<BasicDBObject> list, Variables variable) {
		return "Y";
	}

}
