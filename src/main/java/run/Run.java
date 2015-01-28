package run;

import injection.Injector;
import exporter.XMLExporter;

public class Run {

	public static void main(String[] args) {
		new Injector().run();
		new XMLExporter().run();
	}

}
