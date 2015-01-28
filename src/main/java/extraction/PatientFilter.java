package extraction;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Creation 28 oct. 2014
 */
public class PatientFilter {

	public final static int patientStartIndex = 8;
	private final static int stockVolumeIndex = 18;
	private final static int stockStartIndex = 4;

	public static DBObject getLine(List<DBObject> patients, DBObject stock) {
		@SuppressWarnings("unchecked")
		List<BasicDBObject> stockRow = (List<BasicDBObject>) stock.get(Extractor.ATTRIBUTES);
		String stockInternalCode = PatientFilter.getInternalCode(stockRow, stockStartIndex);

		for (DBObject dbObject : patients) {

			@SuppressWarnings("unchecked")
			List<BasicDBObject> patientRow = (List<BasicDBObject>) dbObject.get(Extractor.ATTRIBUTES);
			String patientInternalCode = PatientFilter.getInternalCode(patientRow, patientStartIndex);

			if (patientInternalCode.equals(stockInternalCode)) {

				BasicDBObject volumeTuple = stockRow.get(stockVolumeIndex);
				int volume = 0;
				try {
					if (volumeTuple.getString(Extractor.VALUE) != null) {
						volume = Integer.valueOf(volumeTuple.getString(Extractor.VALUE));
					}
				} catch (NumberFormatException e) {
					System.out.println("Impossible de lire le volume "+volumeTuple.getString(Extractor.VALUE));
				}

				if (volume > 0) {
					patientRow.add(volumeTuple);
					return dbObject;
				}
			}
		}
		return null;
	}

	public static String getInternalCode(List<BasicDBObject> row, int startIndex) {
		String internalCode = "";
		for (int i=0; i<5; i++) {
			internalCode += row.get(startIndex+i).getString("value")+"-";
		}
		internalCode = internalCode.substring(0, internalCode.length()-1);
		return internalCode;
	}

}
