package exporter;

/**
 * Correspondance entre l'item XML et le titre de colonne Excel
 */
enum Variables {

	id_donor ("?"),
	birthName ("nom de jeune fille"),
	useName ("Nom"),
	firstName ("Pr�nom"),
	birthDate ("date naissance"),
	id_depositor ("?"),
	id_family ("famille"),
	id_sample ("?"),
	consent_ethical ("?"),
	gender ("sexe"),
	age ("�ge au pr�l�vement"),
	pathology ("?"),
	status_sample ("?"),
	collect_date ("date de pr�l�vement"),
	collect_date2 ("date de traitement"),
	nature_sample_dna ("?"),
	storage_conditions ("?"),
	quantity ("Volume (�l)"),
	biobank_id ("?"),
	biobank_date_entry ("?"),
	biobank_collection_id ("?"),

	notfound ("");

	private String title;

	private Variables(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	static Variables getVariable(String t) {
		for (Variables v : Variables.values()) {
			if (v.getTitle().equals(t)) {
				return v;
			}
		}
		return notfound;
	}

}
