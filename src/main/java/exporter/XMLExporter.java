package exporter;

import injection.Injector;
import injection.MongoSingleton;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import extraction.Extractor;
import extraction.PatientFilter;

public class XMLExporter {

	private static final String FILE_NAME = "CBSD_banqueADN_v0.5-1.xml";
	private static final String UNKNOWN_VALUE = "?";

	public void run() {
		DB db = MongoSingleton.getDb();
		DBCollection stockCollection = db.getCollection(Injector.STOCK_COLLECTION);
		DBCursor stocks = stockCollection.find();

		DBCollection patientCollection = db.getCollection(Injector.PATIENT_COLLECTION);
		List<DBObject> patients = patientCollection.find().toArray();

		Document document = DocumentHelper.createDocument();

		Element root = document.addElement("root");
		root.addAttribute("type", "BanqueADN");

		Element samples = root.addElement("samples");

		int count=0;
		for (DBObject dbObject : stocks) {
			DBObject dbO = getPatientAttributes(patients, dbObject);
			if (dbO != null) {
				count++;
				Element sample = samples.addElement("sample");

				Element notes = sample.addElement("notes");

				@SuppressWarnings("unchecked")
				List<BasicDBObject> list = (List<BasicDBObject>) dbO.get(Extractor.ATTRIBUTES);

				addElement(notes, Variables.id_donor, getInternalCode(list));
				addElement(notes, Variables.birthName, getBirthName(list));
				addElement(notes, Variables.useName, getValue(list, Variables.useName));
				addElement(notes, Variables.firstName, getValue(list, Variables.firstName));
				addElement(notes, Variables.birthDate, getValue(list, Variables.birthDate));
				addElement(notes, Variables.id_depositor, getValue(list, Variables.id_depositor));
				addElement(notes, Variables.id_family, getValue(list, Variables.id_family));
				addElement(notes, Variables.id_sample, getValue(list, Variables.id_sample));
				addElement(notes, Variables.consent_ethical, getValue(list, Variables.consent_ethical));
				addElement(notes, Variables.gender, getGender(list));
				addElement(notes, Variables.age, getValue(list, Variables.age));
				addElement(notes, Variables.pathology, getValue(list, Variables.pathology));
				addElement(notes, Variables.status_sample, getValue(list, Variables.status_sample));
				addElement(notes, Variables.collect_date, getCollectDate(list));
				addElement(notes, Variables.nature_sample_dna, getValue(list, Variables.nature_sample_dna));
				addElement(notes, Variables.storage_conditions, "80");
				addElement(notes, Variables.quantity, getValue(list, Variables.quantity));
				addElement(notes, Variables.biobank_id, getValue(list, Variables.biobank_id));
				addElement(notes, Variables.biobank_date_entry, getValue(list, Variables.biobank_date_entry));
				addElement(notes, Variables.biobank_collection_id, getValue(list, Variables.biobank_collection_id));
			}

		}
		System.out.println(count+" samples générés");
		write(document);
	}

	private String getBirthName(List<BasicDBObject> list) {
		String value = getValue(list, Variables.birthName);
		if (value.isEmpty()) {
			value = getValue(list, Variables.useName);
		}
		return value;
	}

	private static DBObject getPatientAttributes(List<DBObject> patients, DBObject stock) {
		DBObject line = PatientFilter.getLine(patients, stock);
		return line;
	}

	private static String getInternalCode(List<BasicDBObject> list) {
		String internalCode = PatientFilter.getInternalCode(list, PatientFilter.patientStartIndex);
		return internalCode;
	}

	private static String getGender(List<BasicDBObject> list) {
		List<String> asList = Arrays.asList("M", "F", "H", "U");
		String value = getValue(list, Variables.gender);
		if (!asList.contains(value)) {
			System.out.println("valeur non permise pour sexe : '"+value+"'");
			value = "U";
		}
		return value;
	}

	private static String getCollectDate(List<BasicDBObject> list) {
		String value = getValue(list, Variables.collect_date);
		if (value.isEmpty()) {
			value = getValue(list, Variables.collect_date2);
		}
		SimpleDateFormat inputFormat = new SimpleDateFormat(Extractor.DATE_FORMAT);
		Date date = null;
		try {
			date = inputFormat.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = outputFormat.format(date);

		return formatted;
	}

	private static String getValue(List<BasicDBObject> list, Variables variable) {
		for (BasicDBObject basicDBObject : list) {
			String title = getString(basicDBObject.get(Extractor.KEY));
			if (variable.equals(Variables.getVariable(title))) {
				return getString(basicDBObject.get(Extractor.VALUE));
			}
		}
		return UNKNOWN_VALUE;
	}

	private static void addElement(Element notes, Variables variable, String value) {
		Element note = notes.addElement("note");
		note.addElement("key").addText(variable.toString());
		note.addElement("value").addText(value);
	}

	private static String getString(Object basicDBObject) {
		if (basicDBObject == null) {
			return "";
		}
		return basicDBObject.toString();
	}

	public static void write(Document document) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		try {
			XMLWriter writer = new XMLWriter(new FileWriter(FILE_NAME), format);
			writer.write(document);
			writer.close();

			writer = new XMLWriter(System.out, format);
			writer.write(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
