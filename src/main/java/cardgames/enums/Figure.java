package cardgames.enums;

public enum Figure {
	AS("A", "A", "Ace", "As", false),
	ROI("K", "R", "King", "Roi", true),
	DAME("Q", "D", "Queen", "Dame", true),
	VALET("J", "V", "Jack", "Valet", true),
	DIX("10", "10", "Ten", "Dix", false),
	NEUF("9", "9", "Nine", "Neuf", false),
	HUIT("8", "8", "Eight", "Huit", false),
	SEPT("7", "7", "Seven", "Sept", false),
	SIX("6", "6", "Six", "Six", false),
	CINQ("5", "5", "Five", "Cinq", false),
	QUATRE("4", "4", "Four", "Quatre", false),
	TROIS("3", "3", "Three", "Trois", false),
	DEUX("2", "2", "Two", "Deux", false);
	
	private String symbole_en;
	private String symbole_fr;
	private String name_en;
	private String name_fr;
	private boolean realFigure;
	
	private Figure(String symbole_en, String symbole_fr, String name_en, String name_fr, boolean realFigure) {
		this.symbole_en = symbole_en;
		this.symbole_fr = symbole_fr;
		this.name_en = name_en;
		this.name_fr = name_fr;
		this.realFigure = realFigure;
	}

	public String getSymbole_en() {
		return symbole_en;
	}

	public String getSymbole_fr() {
		return symbole_fr;
	}

	public String getName_en() {
		return name_en;
	}

	public String getName_fr() {
		return name_fr;
	}

	public boolean isRealFigure() {
		return realFigure;
	}
	
}
