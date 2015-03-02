package exporter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

class XMLPrettyWriter {

	private static final String ENCODING = "UTF-8";
	private static final String FILE_NAME = "CBSD_banqueADN_v0.5-1.xml";

	void write(Document document) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		try {

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME), ENCODING));
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			writer.close();

			writer = new XMLWriter(System.out, format);
			writer.write(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
