package injection;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import extraction.Extractor;

public class Injector {

	public static final String PATIENT_COLLECTION = "patient";
	public static final String STOCK_COLLECTION = "stock";

	public void run() {
		String excelFilePaths = "src/main/resources/filepaths.properties";
		Properties excelFilesProperties = new Properties();
		try {
			FileInputStream excelProps = new FileInputStream(excelFilePaths);
			excelFilesProperties.load(excelProps);
			excelProps.close();

		} catch (IOException e) {
			System.out.println(e);
			System.out
			.println("Impossible de charger le fichier de configuration");
		}

		try {
			DB db = MongoSingleton.getDb();

			// patient
			DBCollection patients = db.getCollection(PATIENT_COLLECTION);
			patients.drop();
			String patientDirPath = excelFilesProperties.getProperty("patientDir");
			File[] patientFiles = new File(patientDirPath).listFiles();

			int count=0;
			for (File file : patientFiles) {
				List<BasicDBObject> list = Extractor.excelExtract(file);
				for (BasicDBObject basicDBObject : list) {
					patients.insert(basicDBObject);
					count++;
				}

			}
			System.out.println(count +" patients ins�r�s");

			count = 0;

			// stock
			DBCollection stocks = db.getCollection(STOCK_COLLECTION);
			stocks.drop();

			String stockDirPath = excelFilesProperties.getProperty("stockDir");
			File[] stockFiles = new File(stockDirPath).listFiles();

			for (File file : stockFiles) {
				List<BasicDBObject> list = Extractor.excelExtract(file);
				for (BasicDBObject basicDBObject : list) {
					stocks.insert(basicDBObject);
					count++;
				}
			}

			System.out.println(count +" stocks ins�r�s");

			JOptionPane.showMessageDialog(null, "Extraction effectuée avec succès.");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Une erreur est survenue, l'exportation a échouée.");
		}
	}

}
