package cardgames.model;

import cardgames.enums.Figure;

public class WeightedFigure {
	public Figure figure;
	public int points;
	public int force;
	public boolean atout;
	
	public WeightedFigure(Figure figure, int points, int force, boolean atout) {
		this.figure = figure;
		this.points = points;
		this.force = force;
		this.atout = atout;
	}
	
	
}
