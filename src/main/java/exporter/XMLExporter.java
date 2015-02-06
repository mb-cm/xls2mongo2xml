package exporter;

import injection.Extractor;
import injection.Injector;
import injection.MongoSingleton;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


public class XMLExporter {

	private static final String BANQUE_ADN = "BanqueADN";
	static final String YYYY_MM_DD = "yyyy-MM-dd";

	public void run() {
		DB db = MongoSingleton.getDb();
		DBCollection stockCollection = db.getCollection(Injector.STOCK_COLLECTION);
		DBCursor stocks = stockCollection.find();

		DBCollection patientCollection = db.getCollection(Injector.PATIENT_COLLECTION);
		List<DBObject> patients = patientCollection.find().toArray();

		Document document = DocumentHelper.createDocument();

		Element root = document.addElement("root");
		root.addAttribute("type", BANQUE_ADN);

		Element samples = root.addElement("samples");

		for (DBObject dbObject : stocks) {
			DBObject line = PatientFilter.getLine(patients, dbObject);
			if (line != null) {
				Element sample = samples.addElement("sample");

				Element notes = sample.addElement("notes");

				@SuppressWarnings("unchecked")
				List<BasicDBObject> list = (List<BasicDBObject>) line.get(Extractor.ATTRIBUTES);

				addElement(notes, list, Variables.id_donor, new InternalCodeTreatment());
				addElement(notes, list, Variables.birthName, new BirthNameTreatment());
				addElement(notes, list, Variables.useName, new SimpleTreatment());
				addElement(notes, list, Variables.firstName, new SimpleTreatment());
				addElement(notes, list, Variables.birthDate, new BirthDateTreatment());
				addElement(notes, list, Variables.gender, new GenderTreatment());
				addElement(notes, list, Variables.quantity_available, new QuantityTreatment());
				addElement(notes, list, Variables.collect_date, new CollectDateTreatment());
				addElement(notes, list, Variables.id_sample, new SimpleTreatment());
				addElement(notes, list, Variables.consent_ethical, new ConsentTreatment());
				addElement(notes, list, Variables.id_family, new SimpleTreatment());
				addElement(notes, list, Variables.age, new SimpleTreatment());
				addElement(notes, list, Variables.storage_conditions, new StorageConditionsTreatment());
				addElement(notes, list, Variables.pathology, new SimpleTreatment());
				addElement(notes, list, Variables.status_sample, new StatusTreatment());
				addElement(notes, list, Variables.consent, new ConsentTreatment());
			}
		}
		System.out.println(samples.elements().size()+" samples générés");

		new XMLPrettyWriter().write(document);
	}

	private void addElement(Element notes, List<BasicDBObject> list, Variables variable, AbstractTreatment treatment) {
		String value = treatment.get(list, variable);
		Element note = notes.addElement("note");
		note.addElement("key").addText(variable.toString());
		note.addElement("value").addText(value);
	}

}
