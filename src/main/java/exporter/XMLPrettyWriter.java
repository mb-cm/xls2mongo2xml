package exporter;

import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

class XMLPrettyWriter {

	private static final String FILE_NAME = "CBSD_banqueADN_v0.5-1.xml";

	void write(Document document) {
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
