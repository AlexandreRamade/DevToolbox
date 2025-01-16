package cardgames.enums;

public enum Forme {
	PIQUE("♠", "Spades", "Pique", Couleur.NOIR),
	TREFFLE("♣", "Clubs", "Treffle", Couleur.NOIR),
	CARREAU("♦", "Diamonds", "Carreau", Couleur.ROUGE),
	COEUR("♥", "Hearts", "Coeur", Couleur.ROUGE);

	private String symbole;
	private String name_en;
	private String name_fr;
	private Couleur couleur;
	
	private Forme(String symbole, String name_en, String name_fr, Couleur couleur) {
		this.symbole = symbole;
		this.name_en = name_en;
		this.name_fr = name_fr;
		this.couleur = couleur;
	}

	public String getSymbole() {
		return symbole;
	}

	public String getName_en() {
		return name_en;
	}

	public String getName_fr() {
		return name_fr;
	}

	public Couleur getCouleur() {
		return couleur;
	}
	
	
}
