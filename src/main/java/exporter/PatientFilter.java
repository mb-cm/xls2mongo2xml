package exporter;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import extraction.Extractor;

class PatientFilter {

	static DBObject getLine(List<DBObject> patients, DBObject stock) {
		@SuppressWarnings("unchecked")
		List<BasicDBObject> stockRow = (List<BasicDBObject>) stock.get(Extractor.ATTRIBUTES);
		String stockInternalCode = InternalCodeConcatenateTool.getStockInternalCode(stockRow);

		boolean patientFound = false;
		for (DBObject dbObject : patients) {

			@SuppressWarnings("unchecked")
			List<BasicDBObject> patientRow = (List<BasicDBObject>) dbObject.get(Extractor.ATTRIBUTES);
			String patientInternalCode = InternalCodeConcatenateTool.getPatientInternalCode(patientRow);

			if (patientInternalCode.equals(stockInternalCode)) {
				patientFound = true;
				BasicDBObject volumeTuple = stockRow.get(FilesStructureConstants.stockVolumeIndex);
				int volume = 0;
				try {
					if (volumeTuple.getString(Extractor.VALUE) != null) {
						volume = Integer.valueOf(volumeTuple.getString(Extractor.VALUE));
					}
				} catch (NumberFormatException e) {
				}

				if (volume > 0) {
					patientRow.add(volumeTuple);
					patientRow.add(stockRow.get(FilesStructureConstants.stockConcentrationIndex));
					patientRow.add(stockRow.get(FilesStructureConstants.stockContainerIndex));
					return dbObject;
				}
			}
		}
		if (!patientFound) {
			System.out.println("Patient "+stockInternalCode+" trouvé dans fichier Stock mais non retrouvé dans fichier Patient");
		}
		return null;
	}


}
