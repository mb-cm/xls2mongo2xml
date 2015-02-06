package exporter;

import injection.Extractor;

import java.util.List;

import com.mongodb.BasicDBObject;


abstract class AbstractTreatment {

	abstract String get(List<BasicDBObject> list, Variables variable);

	final String getValue(List<BasicDBObject> list, Variables variable) {
		for (BasicDBObject basicDBObject : list) {
			String title = getString(basicDBObject.get(Extractor.KEY));
			if (variable.equals(Variables.getVariable(title))) {
				return getString(basicDBObject.get(Extractor.VALUE));
			}
		}
		throw new RuntimeException("Impossible de trouver la colonne '"+variable+"'");
	}

	private String getString(Object basicDBObject) {
		if (basicDBObject == null) {
			return "";
		}
		return basicDBObject.toString();
	}


}
