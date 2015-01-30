package exporter;

import java.util.List;

import com.mongodb.BasicDBObject;

/**
 * Le code interne patient est la concaténation de X colonnes dans le fichier Excel
 */
class InternalCodeConcatenateTool {

	static String getPatientInternalCode(List<BasicDBObject> patientRow) {
		return getInternalCode(patientRow, FilesStructureConstants.patientInternalCodeStartIndex);
	}

	static String getStockInternalCode(List<BasicDBObject> stockRow) {
		return getInternalCode(stockRow, FilesStructureConstants.stockInternalCodeStartIndex);
	}

	private static String getInternalCode(List<BasicDBObject> row, int startIndex) {
		String internalCode = "";
		for (int i=0; i<FilesStructureConstants.internalCodeNumberOfColumns; i++) {
			internalCode += row.get(startIndex+i).getString("value")+"-";
		}
		internalCode = internalCode.substring(0, internalCode.length()-1);
		return internalCode;
	}

}
