package exporter;

/**
 * Correspondance entre l'item XML et le titre de colonne Excel
 * Quand le titre est vide, c'est que l'item ne peut �tre directement reli� � une seule colonne Excel
 */
enum Variables {

	id_donor (""),
	birthName ("nom de jeune fille"),
	useName ("Nom"),
	firstName ("Pr�nom"),
	birthDate ("date naissance"),
	gender ("sexe"),
	quantity_available ("Volume (�l)"),
	quantity_available2 ("Concentration (ng/�l)"),
	collect_date ("date de pr�l�vement"),
	collect_date2 ("date de traitement"),
	id_sample ("genethon"),
	consent_ethical (""),
	id_family ("famille"),
	age ("�ge au pr�l�vement"),
	storage_conditions ("Cong�lateur"),
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
