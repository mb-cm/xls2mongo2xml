package exporter;

/**
 * Correspondance entre l'item XML et le titre de colonne Excel
 * Quand le titre est vide, c'est que l'item ne peut être directement relié à une seule colonne Excel
 */
enum Variables {

	id_donor (""),
	birthName ("nom de jeune fille"),
	useName ("Nom"),
	firstName ("Prénom"),
	birthDate ("date naissance"),
	gender ("sexe"),
	quantity_available ("Volume (µl)"),
	quantity_available2 ("Concentration (ng/µl)"),
	collect_date ("date de prélèvement"),
	collect_date2 ("date de traitement"),
	id_sample ("genethon"),
	consent_ethical (""),
	id_family ("famille"),
	age ("âge au prélèvement"),
	storage_conditions ("Congélateur"),
	pathology ("patho"),
	status_sample ("statut"),
	consent (""),

	notfound ("");

	private final String title;

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
