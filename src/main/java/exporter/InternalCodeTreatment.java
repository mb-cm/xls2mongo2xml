package exporter;

import java.util.List;

import com.mongodb.BasicDBObject;


class InternalCodeTreatment extends AbstractTreatment {

	@Override
	String get(List<BasicDBObject> list, Variables variable) {
		String internalCode = InternalCodeConcatenateTool.getPatientInternalCode(list);
		return internalCode;
	}

}
