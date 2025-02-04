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
	
	public Card(Forme forme, Figure figure, int points, int force) {
		this.forme = forme;
		this.figure = figure;
		this.points = points;
		this.force = force;
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

	public void setPoints(int points) {
		this.points = points;
	}

	public void setForce(int force) {
		this.force = force;
	}

	public void setPointsAndForce(int value) {
		this.points = value;
		this.force = value;
	}
	
}
