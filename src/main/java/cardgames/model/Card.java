package cardgames.model;

import cardgames.enums.Figure;
import cardgames.enums.Forme;

public class Card implements Comparable<Card> {
		
	private Forme forme;
		
	private Figure figure;
		
	private int points;
	
	private int force;
	
	
	public Card(Forme forme, Figure figure) {
		this.forme = forme;
		this.figure = figure;
	}

	public boolean isFigure() {
		return this.figure.isRealFigure();
	}

	@Override
	public int compareTo(Card o) {
		return o.figure.compareTo(this.figure);
	}
	
	public String getSymbol_fr() {
		return this.forme.getSymbole().concat(this.figure.getSymbole_fr());
	}
	

	@Override
	public String toString() {
		return getSymbol_fr();
	}

	public Forme getForme() {
		return forme;
	}

	public Figure getFigure() {
		return figure;
	}

	public int getPoints() {
		return points;
	}

	public int getForce() {
		return force;
	}

	
	
}
