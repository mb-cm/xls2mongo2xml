package exporter;

import java.util.List;

import com.mongodb.BasicDBObject;

/**
 * Temp�rature de stockage diff�rente selon le cong�lateur
 */
class StorageConditionsTreatment extends AbstractTreatment {

	@Override
	String get(List<BasicDBObject> list, Variables variable) {
		String containerName = getValue(list, Variables.storage_conditions);
		if (containerName.equals("2")) {
			return "-30C";
		} else if (containerName.equals("4")) {
			return "-20C";
		}
		return "-80C";
	}

}
